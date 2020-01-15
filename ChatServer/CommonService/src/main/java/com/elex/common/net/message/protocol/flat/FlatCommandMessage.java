package com.elex.common.net.message.protocol.flat;

import java.nio.ByteBuffer;

import com.elex.common.message.flat.CommonMsgBody;
import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.util.ByteBufferUtil;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.google.flatbuffers.FlatBufferBuilder;

/**
 * proto实现的体消息
 * 
 * @author mausmars
 *
 */
public class FlatCommandMessage implements ICommandMessage {
	protected static final ILogger logger = XLogUtil.logger();

	private CommonMsgBody commonMsgBody;
	private FlatBufferBuilder builder;

	public FlatCommandMessage(int commandId, byte[] content) {
		builder = new FlatBufferBuilder(0);

		int contentOffset = builder.createByteVector(content);

		CommonMsgBody.startCommonMsgBody(builder);
		// CommonMsgBody.addVersion(builder, 0);
		CommonMsgBody.addCommandId(builder, commandId);
		CommonMsgBody.addContent(builder, contentOffset);

		int john = CommonMsgBody.endCommonMsgBody(builder);
		builder.finish(john);
	}

	public FlatCommandMessage(byte[] bodyByte) {
		try {
			ByteBuffer bb = ByteBuffer.wrap(bodyByte);
			commonMsgBody = CommonMsgBody.getRootAsCommonMsgBody(bb);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public int getCommandId() {
		return (int) commonMsgBody.commandId();
	}

	@Override
	public byte[] toByteArray() {
		return ByteBufferUtil.byteBuffer2Bytes(builder.dataBuffer());
	}

	@Override
	public byte[] getBodyByte() {
		ByteBuffer bb = commonMsgBody.contentAsByteBuffer();
		return ByteBufferUtil.byteBuffer2Bytes(bb);
	}

	public int getVersion() {
		return (int) commonMsgBody.version();
	}
}
