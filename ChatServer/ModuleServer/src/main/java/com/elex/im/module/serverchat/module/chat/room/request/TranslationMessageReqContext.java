package com.elex.im.module.serverchat.module.chat.room.request;

public class TranslationMessageReqContext {
	private String roomId;
	private long order;
	private int roomType;

	public TranslationMessageReqContext() {
	}

	public TranslationMessageReqContext(com.elex.im.message.proto.ChatMessage.TranslationMessageUp message) {
		this.roomId = message.getRoomId();
		this.order = message.getOrder();
		this.roomType = message.getRoomType();
	}

	public TranslationMessageReqContext(com.elex.im.message.flat.TranslationMessageUp message) {
		this.roomId = message.roomId();
		this.order = message.order();
		this.roomType = (int) message.roomType();
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	public int getRoomType() {
		return roomType;
	}

	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}
}
