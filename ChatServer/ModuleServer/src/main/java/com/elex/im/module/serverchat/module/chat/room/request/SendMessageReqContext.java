package com.elex.im.module.serverchat.module.chat.room.request;

import java.util.HashSet;
import java.util.Set;

import com.elex.im.module.serverchat.module.chat.content.IContent;
import com.elex.im.module.serverchat.module.chat.type.ContentType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;

/**
 * 上下文
 * 
 * @author mausmars
 *
 */
public class SendMessageReqContext {
	private RoomType roomType;
	private String uid;
	private ContentType contentType;
	private String messageContent;
	private long clientSendedTime;
	private String clientExt;
	private String serverExt;
	// -------------------------
	private String targetUid;
	private String roomId;
	private Set<String> atUids;
	// -------------------------
	private IContent content;

	public SendMessageReqContext() {

	}

	public SendMessageReqContext(com.elex.im.message.proto.ChatMessage.SendChatMessageUp message) {
		this.roomType = RoomType.valueOf(message.getRoomType());
		this.uid = message.getUid();
		this.messageContent = message.getContent();
		this.clientSendedTime = message.getSendedTime();
		this.clientExt = message.getClientExt();
		this.serverExt = message.getServerExt();
		this.contentType = ContentType.valueOf(message.getContentType());

		switch (roomType) {
		case Single: {
			this.targetUid = message.getTargetUid();
			break;
		}
		case Region:
		case Union:
		case Group: {
			this.roomId = message.getRoomId();
			this.atUids = new HashSet<>();
			atUids.addAll(message.getAtUidsList());// 去重
			break;
		}
		default:
			break;
		}
	}

	public SendMessageReqContext(com.elex.im.message.flat.SendChatMessageUp message) {
		this.roomType = RoomType.valueOf((int) message.roomType());
		this.uid = message.uid();
		this.messageContent = message.content();
		this.clientSendedTime = message.sendedTime();
		this.clientExt = message.clientExt();
		this.serverExt = message.serverExt();
		this.contentType = ContentType.valueOf((int) message.contentType());

		switch (roomType) {
		case Single: {
			this.targetUid = message.targetUid();
			break;
		}
		case Region:
		case Union:
		case Group: {
			this.roomId = message.roomId();
			this.atUids = new HashSet<>();
			for (int i = 0; i < message.atUidsLength(); i++) {
				this.atUids.add(message.atUids(i));
			}
			break;
		}
		default:
			break;
		}
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public long getClientSendedTime() {
		return clientSendedTime;
	}

	public void setClientSendedTime(long clientSendedTime) {
		this.clientSendedTime = clientSendedTime;
	}

	public String getClientExt() {
		return clientExt;
	}

	public void setClientExt(String clientExt) {
		this.clientExt = clientExt;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public IContent getContent() {
		return content;
	}

	public void setContent(IContent content) {
		this.content = content;
	}

	public String getTargetUid() {
		return targetUid;
	}

	public void setTargetUid(String targetUid) {
		this.targetUid = targetUid;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public Set<String> getAtUids() {
		return atUids;
	}

	public void setAtUids(Set<String> atUids) {
		this.atUids = atUids;
	}

	public String getServerExt() {
		return serverExt;
	}

	public void setServerExt(String serverExt) {
		this.serverExt = serverExt;
	}
}
