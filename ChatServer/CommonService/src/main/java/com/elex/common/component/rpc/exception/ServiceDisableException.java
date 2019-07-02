package com.elex.common.component.rpc.exception;

/**
 * 服务不可用异常
 * 
 * @author mausmars
 *
 */
public class ServiceDisableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ServiceDisableException() {
	}

	public ServiceDisableException(Throwable cause) {
		super(cause);
	}
}
