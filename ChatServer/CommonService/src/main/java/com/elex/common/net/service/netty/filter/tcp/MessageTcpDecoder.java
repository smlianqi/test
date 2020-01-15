package com.elex.common.net.service.netty.filter.tcp;

import java.util.List;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 解码消息对象
 * 
 * @author mausmars
 * 
 */
public class MessageTcpDecoder extends ByteToMessageDecoder {
	protected static final ILogger logger = XLogUtil.logger();

	protected NettyMessageType messageType = new NettyMessageType();

	public MessageTcpDecoder() {
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf bb, List<Object> list) throws Exception {
		if (bb.readableBytes() < messageType.getHeaderLen()) {
			// 连消息头都还没收到
			return;
		}
		// 检验标记
		if (!messageType.checkMark(bb)) {
			bb.clear();
			ctx.close();
			throw new RuntimeException("[Protocol error!] [Address=" + ctx.getClass().toString() + "]");
		}
		if (bb.readableBytes() < messageType.getHeaderLen()) {
			bb.readerIndex(bb.readerIndex() - messageType.getMarkLength());
			return;
		}
		final long bodyLength = bb.getUnsignedInt(bb.readerIndex() + messageType.getPackSizePosition());
		// 消息是否完整到达？
		if (bb.readableBytes() >= messageType.getHeaderLen() + bodyLength) {
			bb.readerIndex(bb.readerIndex() - messageType.getMarkLength());

			NettyMessage msg = messageType.createPackage();
			msg.readFields(bb);
			list.add(msg);
		} else {
			bb.readerIndex(bb.readerIndex() - messageType.getMarkLength());
		}
	}
}
