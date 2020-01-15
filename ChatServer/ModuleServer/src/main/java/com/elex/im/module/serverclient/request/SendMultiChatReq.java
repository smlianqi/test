package com.elex.im.module.serverclient.request;

import java.util.List;

public class SendMultiChatReq extends SendChatReq {
	private String roomId;
	private List<Long> atUids;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public List<Long> getAtUids() {
		return atUids;
	}

	public void setAtUids(List<Long> atUids) {
		this.atUids = atUids;
	}
}
