package com.elex.common.net.service.netty.filter.websocket;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.util.CharsetUtil;

/**
 * 握手处理(客户端)
 * 
 * @author mausmars
 *
 */
public class WebSocketHandshakerHandler extends ChannelInboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	private final WebSocketClientHandshaker handshaker;
	private ChannelPromise handshakeFuture;

	public WebSocketHandshakerHandler(WebSocketClientHandshaker handshaker) {
		this.handshaker = handshaker;
	}

	public ChannelFuture handshakeFuture() {
		return handshakeFuture;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		handshakeFuture = ctx.newPromise();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		if (logger.isDebugEnabled()) {
			logger.debug("[handshaker] channelActive start!");
		}
		// websocket握手
		handshaker.handshake(ctx.channel());
		if (logger.isDebugEnabled()) {
			logger.debug("[handshaker] channelActive finished!");
		}
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		if (logger.isDebugEnabled()) {
			logger.debug("[handshaker] WebSocket Client disconnected!");
		}
		ctx.fireChannelInactive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("[handshaker] channelRead!");
		}
		Channel ch = ctx.channel();
		if (!handshaker.isHandshakeComplete()) {
			handshaker.finishHandshake(ch, (FullHttpResponse) msg);
			if (logger.isDebugEnabled()) {
				logger.debug("[handshaker] WebSocket Client connected!");
			}
			handshakeFuture.setSuccess();
			return;
		}
		if (msg instanceof FullHttpResponse) {
			FullHttpResponse response = (FullHttpResponse) msg;
			throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content="
					+ response.content().toString(CharsetUtil.UTF_8) + ')');
		}
		ctx.fireChannelRead(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		if (!handshakeFuture.isDone()) {
			handshakeFuture.setFailure(cause);
		}
		ctx.fireExceptionCaught(cause);
	}
}
