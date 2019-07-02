package com.elex.im.module.serverclient.serveraccess;

import com.elex.im.message.proto.ErrorMessage.ErrorMessageDown;

public class ResMessage<T> {
	private T successMessage;
	private ErrorMessageDown errorMessage;

	public T getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(T successMessage) {
		this.successMessage = successMessage;
	}

	public ErrorMessageDown getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(ErrorMessageDown errorMessage) {
		this.errorMessage = errorMessage;
	}

}
