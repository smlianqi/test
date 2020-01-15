package com.elex.common.component.member.type;

/**
 * 成员信息类型
 * 
 * @author mausmars
 *
 */
public enum MemberAttachType {
	/** 用户类型 */
	UserType(1), //
	/** 语言类型 */
	LanguageType(2),//
	;
	int value;

	MemberAttachType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
