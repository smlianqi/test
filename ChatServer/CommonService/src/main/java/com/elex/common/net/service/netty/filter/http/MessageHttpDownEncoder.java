package com.elex.common.net.service.netty.filter.http;

import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * http下行编码(服务器端用)
 * 
 * @author mausmars
 *
 */
public class MessageHttpDownEncoder extends ChannelOutboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	@Override
	public void write(ChannelHandlerContext ctx, Object message, ChannelPromise promise) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("[MessageHttpDown Encoder] class=" + message.getClass());
		}
		if (message instanceof FullHttpRequest) {
			ctx.write(message);
		} else if (message instanceof DefaultFullHttpResponse) {
			ctx.write(message);
		} else if (message instanceof ICommandMessage) {
			ICommandMessage msg = (ICommandMessage) message;
			// 回复消息
			FullHttpResponse response = createResponse(msg.toByteArray());
			ctx.writeAndFlush(response);
		} else if (message instanceof String) {
			// 回复消息
			FullHttpResponse response = createResponse(((String) message).getBytes());
			ctx.writeAndFlush(response);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[MessageHttpDown Encoder] unknown type!!! class=" + message.getClass());
			}
		}
	}

	private FullHttpResponse createResponse(byte[] bytes) {
		ByteBuf byteBuf = Unpooled.directBuffer();
		byteBuf.writeBytes(bytes);

		// 返回ok
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
		response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");// 跨域
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

		return response;
	}
}
