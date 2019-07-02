package com.elex.im.module.serverclient.request;

import com.elex.im.module.serverchat.module.chat.type.RoomType;

public class SendSingleChatReq extends SendChatReq {
	private long targetUid;

	public SendSingleChatReq() {
		this.roomType = RoomType.Single;
	}

	public long getTargetUid() {
		return targetUid;
	}

	public void setTargetUid(long targetUid) {
		this.targetUid = targetUid;
	}
}
