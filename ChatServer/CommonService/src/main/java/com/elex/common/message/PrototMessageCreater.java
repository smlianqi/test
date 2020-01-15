package com.elex.common.message;

import java.util.List;

import com.elex.common.message.proto.InnerMessage.BindingSocketUp;
import com.elex.common.message.proto.InnerMessage.BroadcastUp;
import com.elex.common.message.proto.InnerMessage.PingCheckUp;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.common.net.type.MegProtocolType;
import com.google.protobuf.ByteString;

public class PrototMessageCreater implements IMessageCreater {
	@Override
	public MegProtocolType getMegProtocolType() {
		return MegProtocolType.proto;
	}

	public IMessage createBindingSocketUpMessage(String serverId, String token, int index) {
		BindingSocketUp.Builder builder = BindingSocketUp.newBuilder();
		builder.setServerId(serverId);
		builder.setToken(token);
		builder.setIndex(index);

		IMessage msg = Message.createProtoMessage(builder.build());

		return msg;
	}

	public IMessage createPingCheckUpMessage() {
		PingCheckUp.Builder builder = PingCheckUp.newBuilder();
		IMessage msg = Message.createProtoMessage(builder.build());
		return msg;
	}

	public IMessage createBroadcastUpMessage(BroadcastType type, int commandId, byte[] content, List<String> uids) {
		BroadcastUp.Builder builder = BroadcastUp.newBuilder();

		builder.setBroadcastType(type.getValue());
		ByteString c = ByteString.copyFrom(content);
		builder.setContent(c);
		builder.setCommandId(commandId);

		switch (type) {
		case Users:
			builder.addAllTargetUids(uids);
			break;
		default:
			break;
		}
		IMessage msg = Message.createProtoMessage(builder.build());
		return msg;
	}
}
