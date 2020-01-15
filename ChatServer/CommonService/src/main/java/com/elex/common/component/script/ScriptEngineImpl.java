package com.elex.common.component.script;

import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.elex.common.component.script.luaj.LuajScriptEngineFactory;
import com.elex.common.component.script.type.ScriptType;

/**
 * 引擎操作实现
 * 
 * @author mausmars
 * 
 */
public class ScriptEngineImpl implements IScriptEngine {
	/** 引擎key */
	private String key;
	/** 引擎类型 */
	private ScriptType scriptType;
	/** 引擎管理者实例 */
	private ScriptEngineManager engineManager;
	/** 引擎实例 */
	private volatile ScriptEngine se;

	public ScriptEngineImpl(String key, ScriptType scriptType) {
		this.key = key;
		this.scriptType = scriptType;
		this.engineManager = new ScriptEngineManager();
		// 注册引擎工厂
		this.engineManager.registerEngineName("luaj", new LuajScriptEngineFactory());
	}

	@Override
	public ScriptType getScriptType() {
		return scriptType;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void resetConfig(boolean isRebuild, String content, List<SContext> contexts) {
		try {
			String engineName = null;
			switch (scriptType) {
			case js:
				engineName = "JavaScript";
				break;
			case luaj:
				engineName = "luaj";
				break;
			case jnlua:
				engineName = "jnlua";
				break;
			default:
				break;
			}
			if (se == null) {
				ScriptEngine se = engineManager.getEngineByName(engineName);
				init(se, content, contexts);
				this.se = se;
			} else {
				if (isRebuild) {
					ScriptEngine se = engineManager.getEngineByName(engineName);
					init(se, content, contexts);
					this.se = se;
				} else {
					this.se.eval(content);
				}
			}
		} catch (ScriptException e) {
			throw new ScriptOperaterException(e);
		}
	}

	private void init(ScriptEngine se, String content, List<SContext> contexts) throws ScriptException {
		ScriptContext sc = se.getContext();
		if (contexts != null) {
			for (SContext context : contexts) {
				// 设置上下文
				sc.setAttribute(context.fieldName, context.fieldValue, context.scope.value());
			}
		}
		se.eval(content);
	}

	@Override
	public Object invokeMethod(String mkey, String name, Object... args) {
		checkAvailable();

		try {
			Object obj = null;
			switch (scriptType) {
			case js:
				obj = ((Invocable) se).invokeMethod(se.get(mkey), name, args);
				break;
			case luaj:

				break;
			case jnlua:
				obj = ((Invocable) se).invokeMethod(se.get(mkey), name, args);
				break;
			default:
				break;
			}
			return obj;
		} catch (Exception e) {
			throw new ScriptOperaterException(e);
		}
	}

	@Override
	public Object invokeFunction(String name, Object... args) {
		checkAvailable();

		try {
			Object obj = null;
			switch (scriptType) {
			case js:
				obj = ((Invocable) se).invokeFunction(name, args);
				break;
			case luaj:
				obj = se.get(name);
				break;
			case jnlua:
				obj = ((Invocable) se).invokeFunction(name, args);
				break;
			default:
				break;
			}
			return obj;
		} catch (Exception e) {
			throw new ScriptOperaterException(e);
		}
	}

	@Override
	public <T> T getInterface(Class<T> clasz) {
		checkAvailable();

		Object obj = null;
		switch (scriptType) {
		case js:
			obj = ((Invocable) se).getInterface(clasz);
			break;
		case luaj:
			obj = ((Invocable) se).getInterface(clasz);
			break;
		case jnlua:
			obj = ((Invocable) se).getInterface(clasz);
			break;
		default:
			break;
		}
		return (T) obj;
	}

	@Override
	public <T> T getInterface(String mkey, Class<T> clasz) {
		checkAvailable();

		Object obj = null;
		switch (scriptType) {
		case js:
			obj = ((Invocable) se).getInterface(se.get(mkey), clasz);
			break;
		case luaj:

			break;
		case jnlua:
			obj = ((Invocable) se).getInterface(se.get(mkey), clasz);
			break;
		default:
			break;
		}
		return (T) obj;

	}

	private void checkAvailable() {
		if (se == null) {
			throw new ScriptOperaterException("Engine isn't available! please init!");
		}
	}
}
