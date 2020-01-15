package com.elex.common.component.script.luaj;

import javax.script.Invocable;
import javax.script.ScriptException;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.script.LuaScriptEngine;

import net.sf.cglib.proxy.Enhancer;

public class LuajScriptEngine extends LuaScriptEngine implements Invocable {
	private Globals globals;
	private CglibProxy cglibProxy;

	public LuajScriptEngine() {
		globals = JsePlatform.standardGlobals();
		cglibProxy = new CglibProxy(globals);
	}

	public Object eval(String script) throws ScriptException {
		// 加载脚本并编译
		return globals.load(script).call();
	}

	@Override
	public Object invokeMethod(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
		return null;
	}

	@Override
	public Object invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
		return null;
	}

	@Override
	public <T> T getInterface(Class<T> clazz) {
		// cglib代理
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(cglibProxy);
		Object o = enhancer.create();
		return (T) o;
	}

	@Override
	public <T> T getInterface(Object thiz, Class<T> clazz) {
		return null;
	}
}
