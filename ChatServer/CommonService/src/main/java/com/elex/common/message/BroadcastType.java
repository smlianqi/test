package com.elex.common.message;

public enum BroadcastType {
	Users(1), // 1-指定用户转发
	Whole(2),// 2-全服转发
	;

	int value;

	BroadcastType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static BroadcastType valueOf(int value) {
		value--;

		if (value < 0 || value > 1) {
			return null;
		}
		return BroadcastType.values()[value];
	}
}
