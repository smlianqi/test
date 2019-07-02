package com.elex.im.module.serverchat.module.chat.room.result;

import com.elex.im.module.serverchat.module.chat.type.ResultStateType;

public class RoomResult {
	protected ResultStateType stateType;// 返回状态
	protected OnlineUserInfo onlineUserInfo;

	public RoomResult() {
		stateType = ResultStateType.Success;
	}

	public ResultStateType getStateType() {
		return stateType;
	}

	public void setStateType(ResultStateType stateType) {
		this.stateType = stateType;
	}

	public OnlineUserInfo getOnlineUserInfo() {
		return onlineUserInfo;
	}

	public void setOnlineUserInfo(OnlineUserInfo onlineUserInfo) {
		this.onlineUserInfo = onlineUserInfo;
	}
}
