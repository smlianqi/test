package com.elex.im.data.chatroommember;

import java.util.List;

/**
 * 聊天房间成员关系信息
 * 
 * @author mausmars
 *
 */
public class ChatRoomMember {
	/** 用户id */
	private String uid;

	/** 房间id(全服固定id，用户组合id，工会id) */
	private String roomId;

	/** 最后的序列 */
	private long lastOrder;

	/** AT的消息序列，看完就清掉 */
	private List<Long> atOrders;

	/** 状态 0正常 1移除 */
	private int state;

	public ChatRoomMember() {
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

	public long getLastOrder() {
		return lastOrder;
	}

	public void setLastOrder(long lastOrder) {
		this.lastOrder = lastOrder;
	}

	public List<Long> getAtOrders() {
		return atOrders;
	}

	public void setAtOrders(List<Long> atOrders) {
		this.atOrders = atOrders;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
