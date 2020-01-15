package com.elex.im.module.serverchat.module.chat.room.request.gainCondition;

import com.elex.im.module.serverchat.module.chat.type.GainType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;

public class RoomPageCondition extends RoomCondition implements IGainCondition {
	private long order; // 指定需要（2类型翻页查询是1个值）
	private int count; // 每页数量（小于等于10）

	public RoomPageCondition() {
	}

	public RoomPageCondition(com.elex.im.message.proto.ChatMessage.RoomPageCondition message) {
		this.roomId = message.getRoomId();
		this.order = message.getOrder();
		this.count = message.getCount();
		this.roomType = RoomType.valueOf(message.getRoomType());
		boolean isNewest = message.getIsNewest();
		if (!isNewest) {
			this.order -= this.count;
			if (this.order < 0) {
				this.order = 0;
			}
		}
	}

	public RoomPageCondition(com.elex.im.message.flat.RoomPageCondition message) {
		this.roomId = message.roomId();
		this.order = (int) message.order();
		this.count = (int) message.count();
		this.roomType = RoomType.valueOf((int) message.roomType());

		boolean isNewest = message.isNewest();
		if (!isNewest) {
			this.order -= this.count;
			if (this.order < 0) {
				this.order = 0;
			}
		}
	}

	@Override
	public GainType getGainType() {
		return GainType.RoomPage;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
