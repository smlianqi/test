package com.elex.common.component.rpc.exception;

/**
 * 逻辑检查错误异常
 * 
 * @author mausmars
 *
 */
public class CheckException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int errorCode; // 错误码
	private String errorMsg; // 错误内容

	public CheckException() {
	}

	public CheckException(int errorCode) {
		this.errorCode = errorCode;
		this.errorMsg = "";
	}

	public CheckException(int errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public CheckException(int errorCode, String errorMsg, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	// -------------------------------
	public static CheckException createException(int errorCode) {
		return new CheckException(errorCode);
	}

	public static CheckException createException(int errorCode, String errorMsg) {
		return new CheckException(errorCode, errorMsg);
	}

	public static CheckException createException(int errorCode, String errorMsg, Throwable cause) {
		return new CheckException(errorCode, errorMsg, cause);
	}
}
