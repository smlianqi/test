package com.elex.common.net.service.netty.filter.websocket;

import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * http上行编码(客户端用)
 * 
 * @author mausmars
 *
 */
public class MessageWebSocketUpEncoder extends ChannelOutboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	public MessageWebSocketUpEncoder() {
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object message, ChannelPromise promise) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("[MessageWebSocketUp Encoder] class= " + message.getClass());
		}
		if (message instanceof FullHttpRequest) {
			ctx.writeAndFlush(message, promise);
		} else if (message instanceof CloseWebSocketFrame) {
			// socket关闭
			ctx.writeAndFlush(message, promise);
		} else if (message instanceof ICommandMessage) {
			ICommandMessage msg = (ICommandMessage) message;

			ByteBuf byteBuf = Unpooled.directBuffer();
			byteBuf.writeBytes(msg.toByteArray());

			// websocket写二进制消息
			WebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
			ctx.writeAndFlush(frame);
		} else if (message instanceof String) {
			// websocket写文本消息
			WebSocketFrame frame = new TextWebSocketFrame((String) message);
			ctx.writeAndFlush(frame);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[MessageWebSocketUp Encoder] unknown type!!! class=" + message.getClass());
			}
		}
	}
}
