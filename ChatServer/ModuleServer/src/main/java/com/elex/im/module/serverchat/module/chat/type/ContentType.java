package com.elex.im.module.serverchat.module.chat.type;

public enum ContentType {
	Text(0), // 0 表示文本消息
	Battle(1), // 1 战报
	AllianceOperation(2),// 2 联盟操作
	;
	int value;

	ContentType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ContentType valueOf(int value) {
		if (value < 0 || value > 2) {
			return null;
		}
		return ContentType.values()[value];
	}

}
