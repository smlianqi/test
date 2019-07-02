package com.elex.common.net.service.netty.heartbeat;

import com.elex.common.net.message.IMessage;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

/**
 * 心跳
 * 
 * @author mausmars
 *
 */

public class DefaultClientHeartBeatTimeoutHandler extends ChannelInboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	public DefaultClientHeartBeatTimeoutHandler() {
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("[>>> DefaultClientHeartBeatTimeout userEventTriggered]");
		}
		super.userEventTriggered(ctx, evt);
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				if (logger.isWarnEnabled()) {
					logger.warn("[>>> DefaultClientHeartBeatTimeout userEventTriggered] READER_IDLE !");
				}
				// 发送ping消息
				sendPingMessage(ctx);
			} else if (event.state() == IdleState.WRITER_IDLE) {
				if (logger.isWarnEnabled()) {
					logger.warn("[>>> DefaultClientHeartBeatTimeout userEventTriggered] WRITER_IDLE !");
				}
				// 发送ping消息
				sendPingMessage(ctx);
			} else if (event.state() == IdleState.ALL_IDLE) {
				if (logger.isWarnEnabled()) {
					logger.warn("[>>> DefaultClientHeartBeatTimeout userEventTriggered] ALL_IDLE !");
				}
			}
		}
	}

	private void sendPingMessage(ChannelHandlerContext ctx) {
		ISession session = (ISession) ctx.channel()
				.attr(AttributeKey.valueOf(SessionAttachType.ISession_Key.toString())).get();
		SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
		IMessage msg = sessionBox.getMessageCreater().createPingCheckUpMessage();
		if (msg != null) {
			// 发送ping消息
			session.send(msg);
		}
	}
}
