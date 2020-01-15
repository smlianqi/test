package com.elex.im.module.servertest.test.ignite_mysql.chatmessage;

public class ChatMessageKey {
	/** 房间id(全服固定id，用户组合id，工会id) */
	private String roomId;

	private long orderId;

	public ChatMessageKey() {
	}

	public ChatMessageKey(String roomId, long orderId) {
		this.roomId = roomId;
		this.orderId = orderId;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		ChatMessageKey key = (ChatMessageKey) o;
		return roomId.equals(key.roomId) && orderId == key.orderId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (orderId ^ (orderId >>> 32));
		result = prime * result + ((roomId == null) ? 0 : roomId.hashCode());
		return result;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "ChatMessageKey [roomId=" + roomId + ", orderId=" + orderId + "]";
	}
}
