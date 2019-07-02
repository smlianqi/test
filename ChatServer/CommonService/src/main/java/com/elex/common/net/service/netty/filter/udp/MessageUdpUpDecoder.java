package com.elex.common.net.service.netty.filter.udp;

import java.net.InetSocketAddress;

import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;

/**
 * Udp（上行）解码（服务器）
 * 
 * @author mausmars
 *
 */
public class MessageUdpUpDecoder extends ChannelInboundHandlerAdapter {
	protected static final ILogger logger = XLogUtil.logger();

	protected NettyNetConfig config;

	public MessageUdpUpDecoder(NettyNetConfig config) {
		this.config = config;
	}

	// 接受到消息
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("MessageUdpDecoder channelRead channelId=" + ctx.channel().id());
		}
		if (message instanceof DatagramPacket) {
			DatagramPacket packet = (DatagramPacket) message;
			ByteBuf buf = packet.content();
			byte[] datas = new byte[buf.readableBytes()];
			buf.readBytes(datas);
			// 释放资源
			packet.release();

			// 保存地址
			InetSocketAddress address = packet.sender();
			ctx.channel().attr(AttributeKey.valueOf(SessionAttachType.SocketAddress_Key.toString())).set(address);

			ICommandMessage msg = config.getCommandMessageFactory().createMessage(datas);

			ctx.fireChannelRead(msg);
		}
	}
}
