package com.elex.im.data.chatroom;

/**
 * 房间信息
 * 
 * @author mausmars
 *
 */
public class ChatRoomKey {
	private String roomId;

	public ChatRoomKey() {
	}

	public ChatRoomKey(String roomId) {
		this.roomId = roomId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roomId == null) ? 0 : roomId.hashCode());
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ChatRoomKey key = (ChatRoomKey) o;
		return roomId.equals(key.roomId);
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
}
