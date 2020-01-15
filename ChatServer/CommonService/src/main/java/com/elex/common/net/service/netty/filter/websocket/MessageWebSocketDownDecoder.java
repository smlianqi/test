package com.elex.common.net.service.netty.filter.websocket;

import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * WebSocket下行解码（客户端）
 * 
 * @author mausmars
 *
 */
public class MessageWebSocketDownDecoder extends ChannelInboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	protected NettyNetConfig config;

	public MessageWebSocketDownDecoder(NettyNetConfig config) {
		this.config = config;
	}

	// 接受到消息
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
		if (message instanceof HttpRequest) {
			if (logger.isDebugEnabled()) {
				logger.debug("[MessageWebSocketDown Decoder] HttpRequest");
			}
			// HttpRequest req = (HttpRequest) message;
			// System.out.println("ProtocolVersion=" + req.protocolVersion());
			// System.out.println("Method=" + req.method());
			// System.out.println("Uri=" + req.uri());
			// System.out.println("DecoderResult=" + req.decoderResult());
			// if (req.method().name().equals("POST")) {

			// } else if (req.method().name().equals("GET")) {
			// ctx.close();
			// } else {
			// ctx.close();
			// }
		} else if (message instanceof BinaryWebSocketFrame) {
			BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) message;
			ByteBuf buf = binaryWebSocketFrame.content();
			byte[] datas = new byte[buf.readableBytes()];
			buf.readBytes(datas);
			// 释放资源
			binaryWebSocketFrame.release();

			ICommandMessage msg = config.getCommandMessageFactory().createMessage(datas);

			ctx.fireChannelRead(msg);
		} else if (message instanceof CloseWebSocketFrame) {
			// 关闭
			ctx.close();
			if (logger.isDebugEnabled()) {
				logger.debug("WebSocket server received closing");
			}
		} else if (message instanceof HttpResponse) {
			if (logger.isDebugEnabled()) {
				logger.debug("[MessageWebSocketDown Decoder] HttpResponse");
			}
			HttpResponse req = (HttpResponse) message;

			ctx.fireChannelRead(req);
		} else if (message instanceof TextWebSocketFrame) {
			// TODO 这里是测试，如果用需要完善
			if (logger.isDebugEnabled()) {
				logger.debug("[MessageWebSocketDown Decoder] TextWebSocketFrame");
			}
			TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) message;
			String content = textWebSocketFrame.text();
			// 释放资源
			textWebSocketFrame.release();

			ctx.fireChannelRead(content);
		} else if (message instanceof HttpContent) {
			if (logger.isDebugEnabled()) {
				logger.debug("[MessageWebSocketDown Decoder] HttpContent");
			}
			// HttpContent content = (HttpContent) message;
			// byte[] datas = content.content().array();
			// // 释放资源
			// content.release();

			ctx.fireChannelRead(message);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[MessageWebSocketDown Decoder] Other class=" + message.getClass());
			}
			ctx.fireChannelRead(message);
		}
	}
}
