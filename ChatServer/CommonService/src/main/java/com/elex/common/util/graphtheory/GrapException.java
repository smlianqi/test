package com.elex.common.util.graphtheory;

/**
 * 图数据结构错误
 * 
 * @author mausmars
 * 
 */
public class GrapException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GrapException() {
	}

	public GrapException(String msg) {
		super(msg);
	}

	public GrapException(String msg, Exception e) {
		super(msg, e);
	}
}
