package com.elex.common.net.handler;

import com.elex.common.component.threadpool.IPoolExecutor;
import com.elex.common.component.threadpool.task.IHashTask;
import com.elex.common.net.message.IMessageConfigMgr;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.net.service.netty.filter.http.HttpGetMessage;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.google.flatbuffers.Table;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

/**
 * 业务逻辑处理
 * 
 * @author mausmars
 *
 */
public class BusinessHandler implements IMessageHandler<Object, Object> {
	protected static final ILogger logger = XLogUtil.logger();

	/**
	 * 超过检测100ms
	 */
	private int handleOutTimeCheck = 100;

	// 线程引擎
	protected IPoolExecutor poolExecutor;

	protected IMessageConfigMgr messageConfigMgr;

	public BusinessHandler() {
	}

	@Override
	public void inhandle(Object message, ISession session, Object attr) {
		if (message instanceof ICommandMessage) {
			ICommandMessage m = (ICommandMessage) message;

			int commandId = m.getCommandId();
			byte[] buf = m.getBodyByte();

			// 处理消息
			protoMessageHandler(commandId, buf, session);
		} else if (message instanceof HttpGetMessage) {
			HttpGetMessage m = (HttpGetMessage) message;
			// 处理消息
			httpGetMessageHandler(m, session);
		} else if (message instanceof String) {
			// 处理消息
			// TODO 直接回写，测试用（web socket 用的）
			session.write(message);
		}
	}

	private void httpGetMessageHandler(HttpGetMessage msg, ISession session) {
		MessageConfig messageConfig = messageConfigMgr.getMessageConfig(msg.getPath());
		if (messageConfig == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("None command handler! path=" + msg.getPath());
			}
			return;
		}
		try {
			handler(msg, messageConfig, session, null);
		} catch (Exception e) {
			logger.error("receive Error message:-->" + msg.getPath(), e);
		}
	}

	private void protoMessageHandler(int commandId, byte[] buf, ISession session) {
		MessageConfig messageConfig = messageConfigMgr.getMessageConfig(String.valueOf(commandId));
		if (messageConfig == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("None command handler! command=" + commandId);
			}
			return;
		}
		Class<?> superclass = messageConfig.getMessage().getSuperclass();
		if (superclass == Table.class) {
			// flat
			try {
				Object msg = createMsgFlat(messageConfig, buf);
				handler(msg, messageConfig, session, null);
			} catch (Exception e) {
				logger.error("receive Error message:-->" + commandId, e);
			}
		} else if (superclass == GeneratedMessageV3.class) {
			// proto3
			try {
				Object msg = createMsgProto3(messageConfig, buf);
				handler(msg, messageConfig, session, null);
			} catch (Exception e) {
				logger.error("receive Error message:-->" + commandId, e);
			}
		} else if (superclass == GeneratedMessage.class) {
			// proto2
			try {
				Object msg = createMsgProto2(messageConfig, buf);
				handler(msg, messageConfig, session, null);
			} catch (Exception e) {
				logger.error("receive Error message:-->" + commandId, e);
			}
		}
	}

	protected void handler(Object msg, MessageConfig messageConfig, ISession session, Object attr) throws Exception {
		// 这里需要可以走不同的处理线程
		if (poolExecutor == null) {
			hander(msg, session, messageConfig, attr);
		} else {
			IHashTask task = new IHashTask() {
				@Override
				public void run() {
					try {
						hander(msg, session, messageConfig, attr);
					} catch (Exception e) {
						logger.error("", e);
					}
				}

				@Override
				public String getHashKey() {
					return String.valueOf(System.nanoTime());
				}

				@Override
				public boolean isDirectHash() {
					return false;
				}

				@Override
				public String getGroupKey() {
					SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
					String key = sessionBox.getHashKey();
					if (key != null) {
						// 这里依赖用户id做任务组key
						return key;
					} else {
						String sessionId = session.getSessionId();
						return sessionId;
					}
				}
			};
			// 执行
			poolExecutor.execute(task);
		}
	}

	protected void hander(Object message, ISession session, MessageConfig messageConfig, Object attr) throws Exception {
		IMessageInHandler<Object> messageHandler = messageConfig.getMessageHandler();

		long startTime = System.currentTimeMillis();
		// 处理逻辑
		messageHandler.inhandle(message, session, attr);

		long executeTime = System.currentTimeMillis() - startTime;
		if (executeTime > handleOutTimeCheck) {
			if (logger.isWarnEnabled()) {
				logger.warn("Perform Slow!!!: commandId=" + messageConfig.getKey() + ", time=" + executeTime + "ms!");
			}
		}
	}

	@Override
	public void outhandle(Object message, ISession session) {
	}

	private String Proto3ParserMethodName = "parser";
	private String Proto2ParserFieldName = "PARSER";
	private String FlatMethod_Template = "getRootAs%s";

	protected Object createMsgFlat(MessageConfig messageConfig, byte[] buf) throws Exception {
		Class<?> clazz = messageConfig.getMessage();
		String methodName = String.format(FlatMethod_Template, clazz.getSimpleName());
		Method method = clazz.getMethod(methodName, ByteBuffer.class);
		return method.invoke(null, ByteBuffer.wrap(buf));
	}

	// protobuf v3
	protected Object createMsgProto3(MessageConfig messageConfig, byte[] buf) throws Exception {
		Class<?> cls = messageConfig.getMessage();
		Method method = cls.getDeclaredMethod(Proto3ParserMethodName, null);
		Parser<?> parser = (Parser<?>) method.invoke(null, null);

		// 用InputStream，避免拷贝字节数组
		InputStream is = new ByteArrayInputStream(buf);
		return parser.parseFrom(is);
	}

	// protobuf v2
	protected Object createMsgProto2(MessageConfig messageConfig, byte[] buf) throws Exception {
		Class<?> cls = messageConfig.getMessage();
		Field field = cls.getField(Proto2ParserFieldName);
		Parser<?> parser = (Parser<?>) field.get(cls);
		InputStream is = new ByteArrayInputStream(buf);
		return parser.parseFrom(is);
	}

	// ----------------------------------------
	public void setPoolExecutor(IPoolExecutor poolExecutor) {
		this.poolExecutor = poolExecutor;
	}

	public void setMessageConfigMgr(IMessageConfigMgr messageConfigMgr) {
		this.messageConfigMgr = messageConfigMgr;
	}
}
