package com.elex.common.message;

import java.util.List;

import com.elex.common.message.flat.BindingSocketUp;
import com.elex.common.message.flat.BroadcastUp;
import com.elex.common.message.flat.PingCheckUp;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.common.net.type.MegProtocolType;
import com.google.flatbuffers.FlatBufferBuilder;

public class FlatMessageCreater implements IMessageCreater {
	@Override
	public MegProtocolType getMegProtocolType() {
		return MegProtocolType.flat;
	}

	public IMessage createBindingSocketUpMessage(String serverId, String token, int index) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int serverIdOffset = builder.createString(serverId);
		int tokenOffset = builder.createString(token);

		BindingSocketUp.startBindingSocketUp(builder);
		BindingSocketUp.addIndex(builder, index);
		BindingSocketUp.addServerId(builder, serverIdOffset);
		BindingSocketUp.addToken(builder, tokenOffset);
		int john = BindingSocketUp.endBindingSocketUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), BindingSocketUp.class);

		return msg;
	}

	public IMessage createPingCheckUpMessage() {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		PingCheckUp.startPingCheckUp(builder);
		int john = PingCheckUp.endPingCheckUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), PingCheckUp.class);
		return msg;
	}

	public IMessage createBroadcastUpMessage(BroadcastType type, int commandId, byte[] content, List<String> uids) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int contentOffset = BroadcastUp.createContentVector(builder, content);

		int targetUidsOffset = 0;
		switch (type) {
		case Users:
			int[] data = new int[uids.size()];
			int index = 0;
			for (String uid : uids) {
				data[index] = builder.createString(uid);
				index++;
			}
			targetUidsOffset = BroadcastUp.createTargetUidsVector(builder, data);
			break;
		default:
			break;
		}

		BroadcastUp.startBroadcastUp(builder);
		BroadcastUp.addBroadcastType(builder, type.getValue());
		BroadcastUp.addCommandId(builder, commandId);
		BroadcastUp.addContent(builder, contentOffset);
		if (targetUidsOffset > 0) {
			BroadcastUp.addTargetUids(builder, targetUidsOffset);
		}
		int john = BroadcastUp.endBroadcastUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), BroadcastUp.class);
		return msg;
	}
}
