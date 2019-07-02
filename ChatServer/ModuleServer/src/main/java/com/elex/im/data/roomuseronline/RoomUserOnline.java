package com.elex.im.data.roomuseronline;

/**
 * 房间用户在线，为了快速查询房间用户是否在线
 * 
 * @author mausmars
 *
 */
public class RoomUserOnline {
	/** 用户id */
	private String uid;

	/** 房间id(全服固定id，用户组合id，工会id) */
	private String roomId;

	public RoomUserOnline() {
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
