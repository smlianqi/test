package com.elex.common.component.script;

import java.util.concurrent.ConcurrentHashMap;

import com.elex.common.component.script.config.ScScript;
import com.elex.common.component.script.type.ScriptType;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;

public class ScriptService extends AbstractService<ScScript> implements IScriptService {
	private ConcurrentHashMap<String, IScriptEngine> scriptEngineMap = new ConcurrentHashMap<>();

	public ScriptService(IServiceConfig serviceConfig, IGlobalContext context) {
		super(serviceConfig, context);
	}

	@Override
	public void initService() throws Exception {
		ScScript c = getSConfig();

	}

	@Override
	public void startupService() throws Exception {

	}

	@Override
	public void shutdownService() throws Exception {
		scriptEngineMap = null;
	}

	@Override
	public IScriptEngine createAndGetEngine(String key, ScriptType scriptType) {
		IScriptEngine scriptEngine = scriptEngineMap.get(key);
		if (scriptEngine == null) {
			scriptEngine = new ScriptEngineImpl(key, scriptType);
			IScriptEngine se = scriptEngineMap.putIfAbsent(key, scriptEngine);
			if (se != null) {
				scriptEngine = se;
			}
			return scriptEngine;
		} else {
			return scriptEngineMap.get(key);
		}
	}
}
