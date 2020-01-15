package com.elex.im.module.serverclient.request;

import com.elex.im.module.serverchat.module.chat.type.ContentType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;

public class SendChatReq {
	protected long uid;
	protected ContentType contentType;
	protected String content;
	protected long sendedTime;
	protected String clientExt;
	protected String serverExt;
	protected RoomType roomType;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getSendedTime() {
		return sendedTime;
	}

	public void setSendedTime(long sendedTime) {
		this.sendedTime = sendedTime;
	}

	public String getClientExt() {
		return clientExt;
	}

	public void setClientExt(String clientExt) {
		this.clientExt = clientExt;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public String getServerExt() {
		return serverExt;
	}

	public void setServerExt(String serverExt) {
		this.serverExt = serverExt;
	}
}
