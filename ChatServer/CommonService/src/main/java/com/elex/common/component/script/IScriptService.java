package com.elex.common.component.script;

import com.elex.common.component.script.type.ScriptType;
import com.elex.common.service.IService;

/**
 * 脚本服务接口
 * 
 * @author mausmars
 *
 */
public interface IScriptService extends IService {
	IScriptEngine createAndGetEngine(String key, ScriptType scriptType);
}
