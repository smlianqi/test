package com.elex.im.module.serverchat.module.chat.room.result;

import java.util.List;

import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.module.serverchat.module.chat.type.ResultStateType;

public class CreateRoomResult extends RoomResult {
	private ChatRoom chatRoom;
	private List<String> uids;

	public CreateRoomResult() {
	}

	public List<String> getUids() {
		return uids;
	}

	public void setUids(List<String> uids) {
		this.uids = uids;
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}
}
