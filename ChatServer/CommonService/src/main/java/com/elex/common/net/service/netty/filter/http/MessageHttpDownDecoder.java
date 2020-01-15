package com.elex.common.net.service.netty.filter.http;

import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;

/**
 * http下行解码(客户端)
 * 
 * @author mausmars
 *
 */
public class MessageHttpDownDecoder extends ChannelInboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	protected NettyNetConfig config;

	public MessageHttpDownDecoder(NettyNetConfig config) {
		this.config = config;
	}

	// 接受到客户端消息
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
		if (message instanceof HttpRequest) {
			// 服务器端处理这里HttpRequest
			if (logger.isDebugEnabled()) {
				logger.debug("[MessageHttpDown Decoder] HttpRequest");
			}
			HttpRequest req = (HttpRequest) message;
			// System.out.println("ProtocolVersion=" + req.protocolVersion());
			// System.out.println("Method=" + req.method());
			// System.out.println("Uri=" + req.uri());
			// System.out.println("DecoderResult=" + req.decoderResult());
			if (req.method().name().equals("POST")) {
				// boolean isKeepAlive = HttpUtil.isKeepAlive(req);
				// System.out.println("isKeepAlive=" + isKeepAlive);

				// String value =
				// req.headers().get(HttpHeaderNames.CONTENT_LENGTH);
			} else if (req.method().name().equals("GET")) {
				ctx.close();
			} else {
				ctx.close();
			}
//		} else if (message instanceof HttpResponse) {
//			// 客户端处理这里HttpResponse
//			if (logger.isDebugEnabled()) {
//				logger.debug("[MessageHttpDown Decoder] HttpResponse");
//			}
//			ctx.write(message);
		} else if (message instanceof HttpContent) {
			if (logger.isDebugEnabled()) {
				logger.debug("[MessageHttpDown Decoder] HttpContent");
			}
			if (message.toString().equals("EmptyLastHttpContent")) {
				if (logger.isDebugEnabled()) {
					logger.debug("[MessageHttpUp Decoder] EmptyLastHttpContent! return ");
				}
				return;
			}
			HttpContent content = (HttpContent) message;
			ByteBuf buf = content.content();
			byte[] datas = new byte[buf.readableBytes()];
			buf.readBytes(datas);
			// 释放资源
			content.release();

			ICommandMessage msg = config.getCommandMessageFactory().createMessage(datas);

			ctx.fireChannelRead(msg);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[MessageHttpDown Decoder] Other");
			}
		}
	}
}
