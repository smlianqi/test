package com.elex.im.module.serverchat.module.chat.type;

public enum GainType {
	AllNewest(0), // 0-全部最新数据
	RoomMulti(1), // 1-指定房间的多个查询
	RoomPage(2), // 2-指定房间的翻页查询
	RoomNewest(3),// 3-指定房间的最新数据
	;

	int value;

	GainType(int value) {
		this.value = value;
	}

	public static GainType valueOf(int value) {
		if (value < 0 || value > 3) {
			return null;
		}
		return GainType.values()[value];
	}

	public int getValue() {
		return value;
	}
}
