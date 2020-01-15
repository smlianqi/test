package com.elex.common.component.script.luaj;

import javax.script.ScriptEngine;

import org.luaj.vm2.script.LuaScriptEngineFactory;

public class LuajScriptEngineFactory extends LuaScriptEngineFactory {
	public ScriptEngine getScriptEngine() {
		return new LuajScriptEngine();
	}
}
