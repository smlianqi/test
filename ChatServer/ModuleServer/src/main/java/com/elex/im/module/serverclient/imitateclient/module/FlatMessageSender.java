package com.elex.im.module.serverclient.imitateclient.module;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.im.message.flat.GainChatMessageUp;
import com.elex.im.message.flat.SendChatMessageUp;
import com.elex.im.module.serverchat.module.chat.type.RoomType;
import com.google.flatbuffers.FlatBufferBuilder;

public class FlatMessageSender {
	public static void sendGainChatMessageUp(IPlayer player) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);
		int uidOffset = builder.createString(String.valueOf(player.getUserId()));

		GainChatMessageUp.startGainChatMessageUp(builder);
		GainChatMessageUp.addUid(builder, uidOffset);
		int john = GainChatMessageUp.endGainChatMessageUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), GainChatMessageUp.class);
		player.send(msg);
	}

	public static void sendSendChatMessageUp(IPlayer player) {
		long targetUid = 10001;
		if (player.getUserId() == 10001) {
			targetUid = 10002;
		}
		FlatBufferBuilder builder = new FlatBufferBuilder(0);
		int contentOffset = builder.createString("Hello World!!!!!!");
		int targetUidOffset = builder.createString(String.valueOf(targetUid));
		int uidOffset = builder.createString(String.valueOf(player.getUserId()));

		SendChatMessageUp.startSendChatMessageUp(builder);
		SendChatMessageUp.addContent(builder, contentOffset);
		SendChatMessageUp.addRoomType(builder, RoomType.Single.getValue());
		SendChatMessageUp.addSendedTime(builder, System.currentTimeMillis());
		SendChatMessageUp.addTargetUid(builder, targetUidOffset);
		SendChatMessageUp.addUid(builder, uidOffset);
		// SendChatMessageUp.addAtUids(builder, atUidsOffset);
		// SendChatMessageUp.addRoomId(builder, roomIdOffset);
		int john = SendChatMessageUp.endSendChatMessageUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), SendChatMessageUp.class);
		player.send(msg);
	}
}
