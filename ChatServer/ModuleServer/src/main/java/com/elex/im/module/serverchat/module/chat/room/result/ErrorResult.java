package com.elex.im.module.serverchat.module.chat.room.result;

import com.elex.im.module.HandleErrorType;
import com.elex.im.module.serverchat.module.chat.type.ResultStateType;

public class ErrorResult extends RoomResult {
	protected HandleErrorType errorType;// 错误码

	protected Object attach;

	public static ErrorResult errorResult(HandleErrorType errorType) {
		ErrorResult result = new ErrorResult();
		result.setStateType(ResultStateType.Fail);
		result.setErrorType(errorType);
		return result;
	}

	public static ErrorResult errorResult(HandleErrorType errorType, Object attach) {
		ErrorResult result = new ErrorResult();
		result.setStateType(ResultStateType.Fail);
		result.setAttach(attach);
		result.setErrorType(errorType);
		return result;
	}

	public HandleErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(HandleErrorType errorType) {
		this.errorType = errorType;
	}

	public <T> T getAttach() {
		return (T) attach;
	}

	public void setAttach(Object attach) {
		this.attach = attach;
	}
}
