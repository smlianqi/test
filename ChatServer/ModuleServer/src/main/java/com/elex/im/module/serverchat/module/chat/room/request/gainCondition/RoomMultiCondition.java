package com.elex.im.module.serverchat.module.chat.room.request.gainCondition;

import java.util.LinkedList;
import java.util.List;

import com.elex.im.module.serverchat.module.chat.type.GainType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;

public class RoomMultiCondition extends RoomCondition implements IGainCondition {
	private List<Long> order; // 指定需要（2类型翻页查询是1个值）

	public RoomMultiCondition() {
	}

	public RoomMultiCondition(com.elex.im.message.proto.ChatMessage.RoomMultiCondition message) {
		this.roomId = message.getRoomId();
		this.order = message.getOrderList();
		this.roomType = RoomType.valueOf(message.getRoomType());
	}

	public RoomMultiCondition(com.elex.im.message.flat.RoomMultiCondition message) {
		this.roomId = message.roomId();
		this.order = new LinkedList<>();

		for (int i = 0; i < message.orderLength(); i++) {
			this.order.add(message.order(i));
		}
		this.roomType = RoomType.valueOf((int) message.roomType());
	}

	@Override
	public GainType getGainType() {
		return GainType.RoomMulti;
	}

	public List<Long> getOrder() {
		return order;
	}

	public void setOrder(List<Long> order) {
		this.order = order;
	}
}
