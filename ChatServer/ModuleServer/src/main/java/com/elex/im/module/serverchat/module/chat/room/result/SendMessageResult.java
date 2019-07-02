package com.elex.im.module.serverchat.module.chat.room.result;

import java.util.Map;

import com.elex.im.data.chatmessage.ChatMessage;
import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.module.serverchat.module.chat.content.IContent;
import com.elex.im.module.serverchat.module.chat.type.RoomType;

public class SendMessageResult extends RoomResult {
	private ChatRoom chatRoom;
	private ChatMessage chatMessage;
	private IContent content;
	private Map<String, String> translateResult;
	private RoomType roomType;

	public SendMessageResult() {
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	public ChatMessage getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}

	public Map<String, String> getTranslateResult() {
		return translateResult;
	}

	public void setTranslateResult(Map<String, String> translateResult) {
		this.translateResult = translateResult;
	}

	public IContent getContent() {
		return content;
	}

	public void setContent(IContent content) {
		this.content = content;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
}
