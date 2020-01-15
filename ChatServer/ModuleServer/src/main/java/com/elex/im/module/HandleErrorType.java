package com.elex.im.module;

public enum HandleErrorType {
	/** 参数错误 */
	ParameterError(10001), //
	/** 不支持的操作 */
	UnsupportedOperations(10002), //

	/** 已经存在聊天室 */
	RoomExist(10101), //
	/** 不存在聊天室 */
	RoomNoExist(10102), //
	/** 成员不在房间中 */
	MemberNoInRoom(10103), //
	/** 成员数量错误 */
	MemberNumError(10104), //
	/** 消息不存在 */
	ChatMessageNoExist(10105), //
	;
	int value;

	HandleErrorType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
