package com.elex.im.data.chatuser;

/**
 * 房间信息
 * 
 * @author mausmars
 *
 */
public class ChatUserKey {
	private String uid;

	public ChatUserKey() {
	}

	public ChatUserKey(String uid) {
		this.uid = uid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ChatUserKey key = (ChatUserKey) o;
		return uid.equals(key.uid);
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
