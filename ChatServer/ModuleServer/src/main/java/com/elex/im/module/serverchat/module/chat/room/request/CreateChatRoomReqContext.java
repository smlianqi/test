package com.elex.im.module.serverchat.module.chat.room.request;

import java.util.LinkedList;
import java.util.List;

import com.elex.im.module.serverchat.module.chat.type.RoomType;

public class CreateChatRoomReqContext {
	private RoomType roomType;
	private List<String> uids;
	private String admin;

	private String roomId;

	public CreateChatRoomReqContext() {
	}

	public CreateChatRoomReqContext(com.elex.im.message.proto.ChatMessage.CreateChatRoomUp message) {
		this.roomType = RoomType.valueOf(message.getRoomType());
		this.uids = message.getUidList();
		this.admin = message.getAdmin();

		switch (roomType) {
		case Group: {
			break;
		}
		case Union:
		case Region: {
			this.roomId = message.getRoomId();
			break;
		}
		default:
			// TODO 返回错误
			break;
		}
	}

	public CreateChatRoomReqContext(com.elex.im.message.flat.CreateChatRoomUp message) {
		this.roomType = RoomType.valueOf((int) message.roomType());
		this.admin = message.admin();
		this.uids = new LinkedList<>();
		for (int i = 0; i < message.uidLength(); i++) {
			uids.add(message.uid(i));
		}
		switch (roomType) {
		case Group: {
			break;
		}
		case Union:
		case Region: {
			this.roomId = message.roomId();
			break;
		}
		default:
			// TODO 返回错误
			break;
		}
	}

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

	public List<String> getUids() {
		return uids;
	}

	public void setUids(List<String> uids) {
		this.uids = uids;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}
}
