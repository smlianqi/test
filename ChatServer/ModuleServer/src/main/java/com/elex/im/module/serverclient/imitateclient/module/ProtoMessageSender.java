package com.elex.im.module.serverclient.imitateclient.module;

import com.elex.common.component.player.IPlayer;
import com.elex.im.message.proto.ChatMessage.GainChatMessageUp;
import com.elex.im.message.proto.ChatMessage.SendChatMessageUp;
import com.elex.im.module.serverchat.module.chat.type.RoomType;

public class ProtoMessageSender {
	public static void sendGainChatMessageUp(IPlayer player) {
		GainChatMessageUp.Builder builder = GainChatMessageUp.newBuilder();
		builder.setUid(String.valueOf(player.getUserId()));
		player.send(builder.build());
	}

	public static void sendSendChatMessageUp(IPlayer player) {
		long targetUid = 10001;
		if (player.getUserId() == 10001) {
			targetUid = 10002;
		}
		SendChatMessageUp.Builder builder = SendChatMessageUp.newBuilder();
		builder.setContent("Hello World!!!!!!");
		builder.setRoomType(RoomType.Single.getValue());
		builder.setSendedTime(System.currentTimeMillis());
		builder.setUid(String.valueOf(player.getUserId()));
		builder.setTargetUid(String.valueOf(targetUid));

		player.send(builder.build());
	}
}
