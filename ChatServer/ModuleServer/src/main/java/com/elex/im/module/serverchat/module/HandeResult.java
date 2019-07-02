package com.elex.im.module.serverchat.module;

import com.elex.im.module.HandleErrorType;
import com.elex.im.module.serverchat.module.chat.type.ResultStateType;

public class HandeResult {
	private ResultStateType stateType;// 返回状态
	private HandleErrorType errorType;// 错误码

	private Object attach;

	public static HandeResult errorResult(HandleErrorType errorType) {
		HandeResult result = new HandeResult();
		result.stateType = ResultStateType.Fail;
		result.errorType = errorType;
		return result;
	}

	public static HandeResult successResult(Object attach) {
		HandeResult result = new HandeResult();
		result.stateType = ResultStateType.Success;
		result.attach = attach;
		return result;
	}

	public static HandeResult successResult() {
		HandeResult result = new HandeResult();
		result.stateType = ResultStateType.Success;
		return result;
	}

	public ResultStateType getStateType() {
		return stateType;
	}

	public HandleErrorType getErrorType() {
		return errorType;
	}

	public <T> T getAttach() {
		return (T) attach;
	}

	public void setAttach(Object attach) {
		this.attach = attach;
	}
}
