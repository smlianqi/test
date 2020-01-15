package com.elex.common.net.message.protocol.proto;

import com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody;
import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.google.protobuf.ByteString;

/**
 * proto实现的体消息
 * 
 * @author mausmars
 *
 */
public class ProtoCommandMessage implements ICommandMessage {
	protected static final ILogger logger = XLogUtil.logger();

	private CommonMsgBody.Builder builder;
	private CommonMsgBody commonMsgBody;

	public ProtoCommandMessage() {
		builder = CommonMsgBody.newBuilder();
	}

	public ProtoCommandMessage(byte[] bodyByte) {
		try {
			commonMsgBody = CommonMsgBody.parseFrom(bodyByte);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public int getCommandId() {
		if (commonMsgBody != null) {
			return commonMsgBody.getCommandId();
		}
		return builder.getCommandId();
	}

	public void setCommandId(int commandId) {
		changToBuiler();
		builder.setCommandId(commandId);
	}

	public byte[] getBodyByte() {
		if (commonMsgBody != null) {
			return commonMsgBody.getContent().toByteArray();
		}
		return builder.getContent().toByteArray();
	}

	public void setBodyByte(byte[] content) {
		changToBuiler();
		ByteString c = ByteString.copyFrom(content);
		builder.setContent(c);
	}

	public int getVersion() {
		if (commonMsgBody != null) {
			return commonMsgBody.getVersion();
		}
		return builder.getVersion();
	}

	public void setVersion(int version) {
		changToBuiler();
		builder.setVersion(version);
	}

	public byte[] toByteArray() {
		return builder.build().toByteArray();
	}

	private void changToBuiler() {
		// TODO 尽量避免此操作
		if (builder == null) {
			builder = commonMsgBody.toBuilder();
		}
	}
}
