package com.elex.im.module.serverchat.module.chat.type;

/**
 * 成员修改类型
 * 
 * @author mausmars
 *
 */
public enum MemberModifyType {
	/** 0添加 */
	Insert(0), //
	/** 1移除 */
	Remove(1),//
	;
	int value;

	MemberModifyType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static MemberModifyType valueOf(int value) {
		if (value < 0 || value > 1) {
			return null;
		}
		return MemberModifyType.values()[value];
	}
}
