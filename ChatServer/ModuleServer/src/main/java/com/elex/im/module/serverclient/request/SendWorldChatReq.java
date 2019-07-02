package com.elex.im.module.serverclient.request;

import com.elex.im.module.serverchat.module.chat.type.RoomType;

public class SendWorldChatReq extends SendChatReq {
	private String roomId;

	public SendWorldChatReq() {
		this.roomType = RoomType.World;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
}
