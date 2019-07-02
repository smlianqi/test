package com.elex.im.module.serverchat.module.chat.room.request.gainCondition;

import com.elex.im.module.serverchat.module.chat.type.RoomType;

public class RoomCondition {
	protected String roomId; // 房间id
	protected RoomType roomType; // 房间类型

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

}
