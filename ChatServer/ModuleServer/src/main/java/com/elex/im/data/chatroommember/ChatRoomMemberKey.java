package com.elex.im.data.chatroommember;

public class ChatRoomMemberKey {
	private String uid;

	private String roomId;

	public ChatRoomMemberKey() {
	}

	public ChatRoomMemberKey(String roomId, String uid) {
		this.roomId = roomId;
		this.uid = uid;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ChatRoomMemberKey key = (ChatRoomMemberKey) o;
		return uid.equals(key.uid) && roomId.equals(key.roomId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result + ((roomId == null) ? 0 : roomId.hashCode());
		return result;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
}
