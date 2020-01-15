package com.elex.common.net.service.netty.filter.http;

import java.util.List;
import java.util.Map;

import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * http上行解码(服务器端)
 * 
 * @author mausmars
 *
 */
public class MessageHttpUpDecoder extends ChannelInboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	protected NettyNetConfig config;
	private boolean isKeepAlive;

	public MessageHttpUpDecoder(NettyNetConfig config) {
		this.config = config;
	}

	// 接受到客户端消息
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
		if (message instanceof HttpRequest) {
			if (logger.isDebugEnabled()) {
				logger.debug("[MessageHttpUp Decoder] HttpRequest");
			}
			HttpRequest req = (HttpRequest) message;
			// System.out.println("ProtocolVersion=" + req.protocolVersion());
			// System.out.println("Method=" + req.method());
			// System.out.println("Uri=" + req.uri());
			// System.out.println("DecoderResult=" + req.decoderResult());

			if (req.method().name().equals("POST")) {
				isKeepAlive = HttpUtil.isKeepAlive(req);
				// System.out.println("isKeepAlive=" + isKeepAlive);

			} else if (req.method().name().equals("GET")) {
				isKeepAlive = HttpUtil.isKeepAlive(req);

				QueryStringDecoder encoder = new QueryStringDecoder(req.uri());
				String path = encoder.path();

				if (path.equals("/favicon.ico")) {
					// 不处理这个path
					return;
				}

				Map<String, List<String>> params = encoder.parameters();
				if (logger.isDebugEnabled()) {
					logger.debug("path=" + path + ",params=" + params);
				}

				// get请求
				HttpGetMessage msg = new HttpGetMessage();
				msg.setPath(path);
				msg.setParams(params);

				ctx.fireChannelRead(msg);
				// ctx.close();
			} else {
				ctx.close();
			}
		} else if (message instanceof HttpResponse) {
			if (logger.isDebugEnabled()) {
				logger.debug("[MessageHttpUp Decoder] HttpResponse");
			}
			HttpResponse req = (HttpResponse) message;

			if (!isKeepAlive) {
				// 关闭
				ctx.write(req).addListener(ChannelFutureListener.CLOSE);
			} else {
				req.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
				ctx.write(req);
			}
		} else if (message instanceof HttpContent) {
			if (logger.isDebugEnabled()) {
				logger.debug("[MessageHttpUp Decoder] HttpContent");
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
				logger.warn("[MessageHttpUp Decoder] Other");
			}
		}
	}
}
