package com.elex.im.module.serverchat.module.chat.room.request.gainCondition;

import com.elex.im.module.serverchat.module.chat.type.GainType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;

public class RoomNewestCondition extends RoomCondition implements IGainCondition {
	public RoomNewestCondition() {
	}

	public RoomNewestCondition(com.elex.im.message.proto.ChatMessage.RoomNewestCondition message) {
		this.roomId = message.getRoomId();
		this.roomType = RoomType.valueOf(message.getRoomType());
	}

	public RoomNewestCondition(com.elex.im.message.flat.RoomNewestCondition message) {
		this.roomId = message.roomId();
		this.roomType = RoomType.valueOf((int) message.roomType());
	}

	@Override
	public GainType getGainType() {
		return GainType.RoomNewest;
	}
}
