package com.elex.common.net.service.netty.filter.udp;

import java.net.InetSocketAddress;

import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;

/**
 * Udp（上行）编码（客户端）
 * 
 * @author mausmars
 *
 */
public class MessageUdpUpEncoder extends ChannelOutboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	public MessageUdpUpEncoder() {
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object message, ChannelPromise promise) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("[MessageUdpEncoder ] class=" + message.getClass());
		}
		if (message instanceof ICommandMessage) {
			ICommandMessage msg = (ICommandMessage) message;
			// 回复消息
			// udp写二进制消息
			DatagramPacket packet = createDatagramPacket(msg.toByteArray(), ctx);
			ctx.writeAndFlush(packet);
		} else if (message instanceof String) {
			// 回复消息
			DatagramPacket packet = createDatagramPacket(((String) message).getBytes("uft-8"), ctx);
			ctx.writeAndFlush(packet);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[MessageUdpUp Encoder] unknown type!!! class=" + message.getClass());
			}
		}
	}

	private DatagramPacket createDatagramPacket(byte[] bytes, ChannelHandlerContext ctx) {
		ByteBuf byteBuf = Unpooled.directBuffer();
		byteBuf.writeBytes(bytes);

		ISession session = (ISession) ctx.channel()
				.attr(AttributeKey.valueOf(SessionAttachType.ISession_Key.toString())).get();
		InetSocketAddress inetSocketAddress = session.getAttach(SessionAttachType.TargetSocketAddress);

		// udp写二进制消息
		DatagramPacket packet = new DatagramPacket(byteBuf, inetSocketAddress);
		return packet;
	}
}
