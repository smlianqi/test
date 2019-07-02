package com.elex.common.net.service.netty.filter.tcp;

import java.nio.ByteBuffer;

import com.elex.common.net.handler.BusinessHandler;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.rounter.IForwardService;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.MessageAttachType;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.util.ByteBufferUtil;
import com.google.flatbuffers.Table;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.MessageLite;

/**
 * 消息进入处理（消息转换）
 * 
 * @author mausmars
 *
 */
public class MessageTcpHandler extends BusinessHandler {
	private IForwardService forwardService;

	public MessageTcpHandler() {
	}

	@Override
	public void outhandle(Object message, ISession session) {
		if ((message instanceof GeneratedMessage) || (message instanceof GeneratedMessageV3)) {
			// session为client可以这样发送
			NettyMessage msg = new NettyMessage();

			String msgId = messageConfigMgr.getKey(message.getClass());
			msg.setCommandId(Integer.parseInt(msgId));
			msg.setBodyByte(((MessageLite) message).toByteArray());

			// 如果session是pool连接这里不是userId，避免直接发送，用IMessage
			SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
			Long value = sessionBox.getUserId();
			if (value != null) {
				msg.setExtend(value);
			}
			// 发送
			session.write(msg);
		} else if (message instanceof Table) {
			// session为client可以这样发送
			NettyMessage msg = new NettyMessage();

			String msgId = messageConfigMgr.getKey(message.getClass());
			msg.setCommandId(Integer.parseInt(msgId));

			ByteBuffer bb = ((Table) message).getByteBuffer();
			byte[] data = ByteBufferUtil.byteBuffer2Bytes(bb);
			msg.setBodyByte(data);

			SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
			Long value = sessionBox.getUserId();
			if (value != null) {
				msg.setExtend(value);
			}
			// 发送
			session.write(msg);
		} else if (message instanceof IMessage) {
			// 转发session用这个消息，将userId通过消息带过来
			IMessage m = (IMessage) message;

			NettyMessage msg = new NettyMessage();
			switch (m.getMegProtocolType()) {
			case proto: {
				Object pm = m.getMessage();
				String msgId = messageConfigMgr.getKey(pm.getClass());
				msg.setCommandId(Integer.parseInt(msgId));

				msg.setBodyByte(((MessageLite) pm).toByteArray());
				break;
			}
			case flat: {
				Class<?> clz = (Class<?>) m.getAttach(MessageAttachType.MessageClass);
				String msgId = messageConfigMgr.getKey(clz);
				msg.setCommandId(Integer.parseInt(msgId));

				ByteBuffer bb = (ByteBuffer) m.getMessage();
				byte[] data = ByteBufferUtil.byteBuffer2Bytes(bb);
				msg.setBodyByte(data);
				break;
			}
			case bytes: {
				int commandId = m.getAttach(MessageAttachType.CommandId);

				msg.setCommandId(commandId);
				msg.setBodyByte((byte[]) m.getMessage());
				break;
			}
			default:
				break;
			}
			Long userId = m.getAttach(MessageAttachType.UserId);
			if (userId != null) {
				msg.setExtend(userId);
			}
			// 发送
			session.write(msg);
		} else if (message instanceof NettyMessage) {
			// 直接发送
			session.write(message);
		}
	}

	@Override
	public void inhandle(Object message, ISession session, Object attr) {
		if (message instanceof NettyMessage) {
			NettyMessage m = (NettyMessage) message;
			// 处理消息
			handleMsg(m, session);
		}
	}

	private void handleMsg(NettyMessage message, ISession session) {
		// 这里转发或处理
		int commandId = message.getCommandId();

		// 处理消息
		MessageConfig messageConfig = messageConfigMgr.getMessageConfig(String.valueOf(commandId));
		if (messageConfig != null) {
			// 如果处理器存在就处理
			handle2(messageConfig, message, session);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("None command handler! command=" + commandId);
			}
		}
		// 消息转发
		forwardService.forwardMsg(message, session);
	}

	private void handle2(MessageConfig messageConfig, NettyMessage message, ISession session) {
		int commandId = message.getCommandId();
		byte[] buf = message.getBodyByte();

		try {
			Object pm = null;
			Class<?> superclass = messageConfig.getMessage().getSuperclass();
			if (superclass == Table.class) {
				pm = createMsgFlat(messageConfig, buf);
			} else if (superclass == GeneratedMessageV3.class) {
				pm = createMsgProto3(messageConfig, buf);
			} else if (superclass == GeneratedMessage.class) {
				pm = createMsgProto2(messageConfig, buf);
			}

			long userId = 0;
			if (message.getExtend() > 0) {
				userId = message.getExtend();
			} else {
				SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
				Long uid = sessionBox.getUserId();
				if (uid != null) {
					userId = uid;
				}
			}
			handler(pm, messageConfig, session, userId);
		} catch (Exception e) {
			logger.error("receive Error message:-->" + commandId, e);
		}
	}

	// ----------------------------------------------------
	public void setForwardService(IForwardService forwardService) {
		this.forwardService = forwardService;
	}
}
