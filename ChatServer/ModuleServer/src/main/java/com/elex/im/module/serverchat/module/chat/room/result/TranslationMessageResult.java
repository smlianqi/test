package com.elex.im.module.serverchat.module.chat.room.result;

import com.elex.im.data.chatmessage.ChatMessage;

public class TranslationMessageResult {
	private ChatMessage chatMessage;
	private int roomType;

	public int getRoomType() {
		return roomType;
	}

	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}

	public ChatMessage getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}
}
