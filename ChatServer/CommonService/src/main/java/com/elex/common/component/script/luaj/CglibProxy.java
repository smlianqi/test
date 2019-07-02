package com.elex.common.component.script.luaj;

import java.lang.reflect.Method;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * cglib 代理
 * 
 * @author mausmars
 *
 */
public class CglibProxy implements MethodInterceptor {
	private Globals globals;

	public CglibProxy(Globals globals) {
		this.globals = globals;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		LuaValue func = globals.get(method.getName());

		LuaValue[] luaValues = new LuaValue[args.length + 1];

		// 脚本第一个参数作为上下文
		LuaTable context = new LuaTable();
		luaValues[0] = context;

		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			// 转类型
			luaValues[i + 1] = CoerceJavaToLua.coerce(arg);
		}
		Varargs result = func.invoke(luaValues);

		// TODO 这里规定暂时只能返回一个值
		LuaValue rlv = result.arg(1);

		// TODO 这里可以扩展luaj支持返回函数等操作给java，但是这里是映射java接口不处理了，可以单独接口做支持
		Object rv = null;
		if (rlv.isint()) {
			rv = rlv.toint();
		} else if (rlv.isboolean()) {
			rv = rlv.toboolean();
		} else if (rlv.islong()) {
			rv = rlv.tolong();
		} else if (rlv.isstring()) {
			rv = rlv.tojstring();
		} else if (rlv.isuserdata()) {
			rv = rlv.touserdata();
		} else if (rlv.isnil()) {
			rv = null;
		}
		return rv;
	}
}
