package com.elex.common.component.script.type;

import javax.script.ScriptContext;

/**
 * 范围枚举
 * 
 * @author mausmars
 * 
 */
public enum ContextScopeType {
	/**
	 * 引擎范围
	 */
	ENGINE_SCOPE(ScriptContext.ENGINE_SCOPE), //
	/**
	 * 引擎管理者范围
	 */
	GLOBAL_SCOPE(ScriptContext.GLOBAL_SCOPE), //
	;
	private int value;

	ContextScopeType(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
