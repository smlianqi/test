package com.elex.im.module.serverchat.module.chat.room.request;

import java.util.LinkedList;
import java.util.List;

import com.elex.im.module.serverchat.module.chat.type.MemberModifyType;

public class ManagerChatRoomMemberReqContext {
	private MemberModifyType modifyType;
	private String roomId;
	private List<String> uids;

	public ManagerChatRoomMemberReqContext() {

	}

	public ManagerChatRoomMemberReqContext(com.elex.im.message.proto.ChatMessage.ManagerChatRoomMemberUp message) {
		this.modifyType = MemberModifyType.valueOf(message.getModifyType());
		this.roomId = message.getRoomId();
		this.uids = message.getUidList();
	}

	public ManagerChatRoomMemberReqContext(com.elex.im.message.flat.ManagerChatRoomMemberUp message) {
		this.modifyType = MemberModifyType.valueOf((int) message.modifyType());
		this.roomId = message.roomId();
		this.uids = new LinkedList<>();
		for (int i = 0; i < message.uidLength(); i++) {
			uids.add(message.uid(i));
		}
	}

	public MemberModifyType getModifyType() {
		return modifyType;
	}

	public void setModifyType(MemberModifyType modifyType) {
		this.modifyType = modifyType;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public List<String> getUids() {
		return uids;
	}

	public void setUids(List<String> uids) {
		this.uids = uids;
	}
}
