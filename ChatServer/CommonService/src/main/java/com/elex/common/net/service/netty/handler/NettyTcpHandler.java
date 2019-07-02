package com.elex.common.net.service.netty.handler;

import java.io.IOException;
import java.util.List;

import com.elex.common.net.handler.IClosedSessionHandler;
import com.elex.common.net.handler.ICreateSessionHandler;
import com.elex.common.net.handler.IMessageHandler;
import com.elex.common.net.session.ISession;
import com.elex.common.net.session.ISessionFactory;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * tcp逻辑处理
 * 
 * @author mausmars
 * 
 */
@Sharable
public class NettyTcpHandler extends ChannelInboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	private IMessageHandler<Object, Object> messageHandler;
	private List<IClosedSessionHandler> closeSessionHandlers;
	private List<ICreateSessionHandler> createSessionHandlers;

	private ISessionFactory sessionFactory;

	// 与客户端建立连接
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ISession session = (ISession) ctx.channel()
				.attr(AttributeKey.valueOf(SessionAttachType.ISession_Key.toString())).get();
		if (session == null) {
			// 创建session
			session = sessionFactory.createSession(ctx.channel());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("[Channel Active] sessionId=" + session.getSessionId());
		}
		sessionFactory.getSessionManager().insertSession(session);
		if (createSessionHandlers != null) {
			for (ICreateSessionHandler handler : createSessionHandlers) {
				// session建立连接的操作
				handler.execute(session);
			}
		}
	}

	// 与客户端断开连接
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ISession session = (ISession) ctx.channel()
				.attr(AttributeKey.valueOf(SessionAttachType.ISession_Key.toString())).get();
		if (logger.isDebugEnabled()) {
			logger.debug("[Channel Close] sessionId=" + session.getSessionId());
		}
		if (closeSessionHandlers != null) {
			for (IClosedSessionHandler handler : closeSessionHandlers) {
				// 清除本地缓存等操作
				handler.execute(session);
			}
		}
		// 移除session
		sessionFactory.getSessionManager().removeSession(session.getSessionId());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ISession session = (ISession) ctx.channel()
				.attr(AttributeKey.valueOf(SessionAttachType.ISession_Key.toString())).get();
		if (cause instanceof IOException) {
			logger.error("[Channel Exception] sessionId=" + session.getSessionId() + " " + cause.getMessage() + "----"
					+ cause.getClass().getName());
		} else {
			logger.error("[Channel Exception] sessionId=" + session.getSessionId(), cause);
		}
		ctx.close();
	}

	// 接受到客户端消息
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
		ISession session = (ISession) ctx.channel()
				.attr(AttributeKey.valueOf(SessionAttachType.ISession_Key.toString())).get();
		if (logger.isDebugEnabled()) {
			logger.debug("[Channel Read] sessionId=" + session.getSessionId());
		}
		messageHandler.inhandle(message, session, null);
	}

	// -------------------------
	public void setCloseSessionHandlers(List<IClosedSessionHandler> closeSessionHandlers) {
		this.closeSessionHandlers = closeSessionHandlers;
	}

	public void setCreateSessionHandlers(List<ICreateSessionHandler> createSessionHandlers) {
		this.createSessionHandlers = createSessionHandlers;
	}

	public void setMessageHandler(IMessageHandler<Object, Object> messageHandler) {
		this.messageHandler = messageHandler;
	}

	public void setSessionFactory(ISessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
