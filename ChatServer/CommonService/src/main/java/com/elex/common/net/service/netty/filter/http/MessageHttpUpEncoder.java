package com.elex.common.net.service.netty.filter.http;

import java.net.URI;

import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

/**
 * http上行编码(客户端用)
 * 
 * @author mausmars
 *
 */
public class MessageHttpUpEncoder extends ChannelOutboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	protected URI uri;

	public MessageHttpUpEncoder(URI uri) {
		this.uri = uri;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object message, ChannelPromise promise) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("[MessageHttpUp Encoder] class=" + message.getClass());
		}
		if (message instanceof FullHttpRequest) {
			ctx.write(message);
		} else if (message instanceof DefaultFullHttpResponse) {
			ctx.write(message);
		} else if (message instanceof ICommandMessage) {
			ICommandMessage msg = (ICommandMessage) message;
			// post请求
			FullHttpRequest request = createRequest(msg.toByteArray());
			ctx.writeAndFlush(request);
		} else if (message instanceof String) {
			// 回复消息
			FullHttpRequest request = createRequest(((String) message).getBytes());
			ctx.writeAndFlush(request);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[MessageHttpUp Encoder] unknown type!!! class=" + message.getClass());
			}
		}
	}

	private FullHttpRequest createRequest(byte[] bytes) {
		ByteBuf byteBuf = Unpooled.directBuffer();
		byteBuf.writeBytes(bytes);

		// 返回ok
		DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
				uri.toASCIIString(), Unpooled.wrappedBuffer(byteBuf));
		request.headers().set(HttpHeaderNames.HOST, uri.getHost());
		request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

		return request;
	}
}
