package com.elex.common.net.service.netty.handler;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.elex.common.net.handler.IMessageHandler;
import com.elex.common.net.session.ISession;
import com.elex.common.net.session.ISessionFactory;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * udp逻辑处理
 * 
 * @author mausmars
 * 
 */
public class NettyUdpHandler extends ChannelInboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	private IMessageHandler<Object, Object> messageHandler;

	private ISessionFactory sessionFactory;

	// 与客户端建立连接
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 初始化session
		ISession session = (ISession) ctx.channel()
				.attr(AttributeKey.valueOf(SessionAttachType.ISession_Key.toString())).get();
		if (session == null) {
			session = sessionFactory.createSession(ctx.channel());
		}
		if (logger.isInfoEnabled()) {
			logger.info("channelActive sessionId=" + session.getSessionId());
		}
	}

	// 接受到客户端消息
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
		// 这里都是按照发过来的地址算
		InetSocketAddress address = (InetSocketAddress) ctx.channel()
				.attr(AttributeKey.valueOf(SessionAttachType.SocketAddress_Key.toString())).get();

		ISession session = (ISession) ctx.channel()
				.attr(AttributeKey.valueOf(SessionAttachType.ISession_Key.toString())).get();
		// 设置地址
		session.setAttach(SessionAttachType.TargetSocketAddress, address);

		if (logger.isDebugEnabled()) {
			logger.debug("[channelRead] sessionId=" + session.getSessionId());
		}
		messageHandler.inhandle(message, session, null);
	}

	// 与客户端断开连接
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("channelInactive");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof IOException) {
			logger.error("[exceptionCaught] " + cause.getMessage() + "----" + cause.getClass().getName());
		} else {
			logger.error("[exceptionCaught] ", cause);
		}
		ctx.close();
	}

	public IMessageHandler<Object, Object> getMessageHandler() {
		return messageHandler;
	}

	// -------------------------
	public void setMessageHandler(IMessageHandler<Object, Object> messageHandler) {
		this.messageHandler = messageHandler;
	}
}
