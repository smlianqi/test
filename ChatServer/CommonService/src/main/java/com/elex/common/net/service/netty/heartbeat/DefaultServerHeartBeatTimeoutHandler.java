package com.elex.common.net.service.netty.heartbeat;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳
 * 
 * @author mausmars
 *
 */

public class DefaultServerHeartBeatTimeoutHandler extends ChannelInboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("[DefaultServerHeartBeatTimeout userEventTriggered]");
		}
		super.userEventTriggered(ctx, evt);
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				if (logger.isWarnEnabled()) {
					logger.warn("[>>> DefaultServerHeartBeatTimeout userEventTriggered] READER_IDLE !");
				}
				ctx.close();
			} else if (event.state() == IdleState.WRITER_IDLE) {
				if (logger.isWarnEnabled()) {
					logger.warn("[>>> DefaultServerHeartBeatTimeout userEventTriggered] WRITER_IDLE !");
				}
				// ctx.close();
			} else if (event.state() == IdleState.ALL_IDLE) {
				if (logger.isWarnEnabled()) {
					logger.warn("[>>> DefaultServerHeartBeatTimeout userEventTriggered] ALL_IDLE !");
				}
				// ctx.close();
			}
		}
	}
}
