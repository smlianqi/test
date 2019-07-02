package com.elex.im.module.serverchat.module.chat.room.result;

import java.util.List;

import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.module.serverchat.module.chat.type.MemberModifyType;

public class ModifyMemberResult extends RoomResult {
	private MemberModifyType modifyType;
	private ChatRoom chatRoom;
	private List<String> uids;

	public ModifyMemberResult() {
	}

	public MemberModifyType getModifyType() {
		return modifyType;
	}

	public void setModifyType(MemberModifyType modifyType) {
		this.modifyType = modifyType;
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	public List<String> getUids() {
		return uids;
	}

	public void setUids(List<String> uids) {
		this.uids = uids;
	}
}
