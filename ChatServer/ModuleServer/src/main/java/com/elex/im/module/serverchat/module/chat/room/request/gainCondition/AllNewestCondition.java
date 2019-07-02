package com.elex.im.module.serverchat.module.chat.room.request.gainCondition;

import com.elex.im.module.serverchat.module.chat.type.GainType;

public class AllNewestCondition implements IGainCondition {
	private String regionRoomId; // 区房间id
	private String unionRoomId; // 联盟房间id

	public int getSingleRoomFetchSize() {
		return singleRoomFetchSize;
	}

	public void setSingleRoomFetchSize(int singleRoomFetchSize) {
		this.singleRoomFetchSize = singleRoomFetchSize;
	}

	private int singleRoomFetchSize;  //单聊房间一次获取的最新纪录条数，值不能超过20，否则以20算。如果小于等于0，则也以20算

	public AllNewestCondition() {
	}

	public AllNewestCondition(com.elex.im.message.proto.ChatMessage.AllNewestCondition message) {
		this.regionRoomId = message.getRegionRoomId();
		this.unionRoomId = message.getUnionRoomId();
		this.singleRoomFetchSize = message.getSingleRoomFetchSize();
	}

	public AllNewestCondition(com.elex.im.message.flat.AllNewestCondition message) {
		this.regionRoomId = message.regionRoomId();
		this.unionRoomId = message.unionRoomId();
	}

	@Override
	public GainType getGainType() {
		return GainType.AllNewest;
	}

	public String getRegionRoomId() {
		return regionRoomId;
	}

	public void setRegionRoomId(String regionRoomId) {
		this.regionRoomId = regionRoomId;
	}

	public String getUnionRoomId() {
		return unionRoomId;
	}

	public void setUnionRoomId(String unionRoomId) {
		this.unionRoomId = unionRoomId;
	}
}
