package com.elex.common.net.service.netty.filter.tcp;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码消息对象
 * 
 * @author mausmars
 *
 */

public class MessageTcpEncoder extends MessageToByteEncoder<Object> {
	protected static final ILogger logger = XLogUtil.logger();

	public MessageTcpEncoder() {
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf bb) throws Exception {
		if (message instanceof NettyMessage) {
			NettyMessage msg = (NettyMessage) message;
			msg.write(bb);
			ctx.writeAndFlush(msg);
		} else {
			if (logger.isErrorEnabled()) {
				logger.error("[MessageTcp Encoder] unknown type!!! class=" + message.getClass());
			}
		}
	}
}
