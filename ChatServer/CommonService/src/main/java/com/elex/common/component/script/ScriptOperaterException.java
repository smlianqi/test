package com.elex.common.component.script;

/**
 * 脚本操作异常类
 * 
 * @author mausmars
 * 
 */
public class ScriptOperaterException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ScriptOperaterException() {
	}

	public ScriptOperaterException(Throwable t) {
		super(t);
	}

	public ScriptOperaterException(String msg) {
		super(msg);
	}

	public ScriptOperaterException(String msg, Throwable t) {
		super(msg, t);
	}
}
