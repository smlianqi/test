package com.elex.im.module.serverchat.module.chat.type;

/**
 * room 类型
 * 
 * @author mausmars
 *
 */
public enum RoomType {
	/** 1 全服 */
	World(1), //
	/** 2 单聊 */
	Single(2), //
	/** 3 自己的聊天 */
	Group(3), //
	/** 4 工会 */
	Union(4), //
	/** 5 区聊 */
	Region(5),//
	;

	int value;

	public static RoomType valueOf(int value) {
		if (value < 1 || value > 5) {
			return null;
		}
		value--;
		return RoomType.values()[value];
	}

	RoomType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
