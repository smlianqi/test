package com.elex.im.module.serverchat.module.chat.room.result;

import java.util.List;

import com.elex.im.data.chatmessage.ChatMessage;
import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.data.chatroommember.ChatRoomMember;
import com.elex.im.data.chatuser.ChatUser;

public class GainMessageResult extends RoomResult {
	private ChatRoom chatRoom;// 房间信息

	private ChatRoomMember self;//
	private List<ChatMessage> chatMessages;// 聊天信息（被移除就没有信息）
	private List<ChatUser> chatUsers; // 全部成员信息（被移除就没有信息，区、工会，都不给）
	private long lastOrder = 0;
	private int roomType;

	public GainMessageResult() {
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public List<ChatMessage> getChatMessages() {
		return chatMessages;
	}

	public long getLastOrder() {
		return lastOrder;
	}

	public void setLastOrder(long lastOrder) {
		this.lastOrder = lastOrder;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	public void setChatMessages(List<ChatMessage> chatMessages) {
		this.chatMessages = chatMessages;
	}

	public ChatRoomMember getSelf() {
		return self;
	}

	public void setSelf(ChatRoomMember self) {
		this.self = self;
	}

	public List<ChatUser> getChatUsers() {
		return chatUsers;
	}

	public void setChatUsers(List<ChatUser> chatUsers) {
		this.chatUsers = chatUsers;
	}

	public int getRoomType() {
		return roomType;
	}

	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}
}
