package com.elex.common.net.service.netty.filter.udp;

import java.nio.ByteBuffer;

import com.elex.common.net.handler.BusinessHandler;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.MessageAttachType;
import com.elex.common.util.ByteBufferUtil;
import com.google.flatbuffers.Table;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.MessageLite;

/**
 * 消息进入处理（消息转换）
 * 
 * @author mausmars
 *
 */
public class MessageUdpHandler extends BusinessHandler {
	protected NettyNetConfig config;

	public MessageUdpHandler(NettyNetConfig config) {
		this.config = config;
	}

	@Override
	public void outhandle(Object message, ISession session) {
		if ((message instanceof GeneratedMessage) || (message instanceof GeneratedMessageV3)) {
			// 现在处理的都是proto的
			String msgId = messageConfigMgr.getKey(message.getClass());

			byte[] data = ((MessageLite) message).toByteArray();

			ICommandMessage msg = config.getCommandMessageFactory().createMessage(Integer.parseInt(msgId), data);
			// 发送
			session.write(msg);
		} else if (message instanceof IMessage) {
			IMessage m = (IMessage) message;

			Class<?> clz = (Class<?>) m.getAttach(MessageAttachType.MessageClass);
			String msgId = messageConfigMgr.getKey(clz);

			ByteBuffer bb = (ByteBuffer) m.getMessage();
			byte[] data = ByteBufferUtil.byteBuffer2Bytes(bb);

			ICommandMessage msg = config.getCommandMessageFactory().createMessage(Integer.parseInt(msgId), data);
			// 发送
			session.write(msg);
		} else if (message instanceof Table) {
			String msgId = messageConfigMgr.getKey(message.getClass());

			ByteBuffer bb = ((Table) message).getByteBuffer();
			byte[] data = ByteBufferUtil.byteBuffer2Bytes(bb);

			ICommandMessage msg = config.getCommandMessageFactory().createMessage(Integer.parseInt(msgId), data);
			// 发送
			session.write(msg);
		} else if (message instanceof ICommandMessage) {
			session.write(message);
		} else if (message instanceof String) {
			// 直接发送
			session.write(message);
		}
	}

	@Override
	public void inhandle(Object message, ISession session, Object attr) {
		// 业务处理
		super.inhandle(message, session, null);
	}
}
