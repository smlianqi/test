package com.elex.im.data.chatroom;

/**
 * 房间信息
 * 
 * @author mausmars
 *
 */
public class ChatRoom {
	/** 房间id(全服固定id，用户组合id，工会id) */
	private String roomId;

	/** 聊天室类型（应用级别的全服，单聊，群） */
	private int roomType;

	/** 管理员 */
	private String admin;

	/** 创建时间 */
	private long createTime;

	/** 名称 */
	private String name;

	public ChatRoom() {
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public int getRoomType() {
		return roomType;
	}

	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
