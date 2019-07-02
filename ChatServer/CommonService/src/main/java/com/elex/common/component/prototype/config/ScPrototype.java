package com.elex.common.component.prototype.config;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.elex.common.util.json.JsonUtil;
import com.google.gson.reflect.TypeToken;

/**
 * 静态数据配置 扩展实体类
 */
public class ScPrototype extends ScPrototypeGeneral {
	// 记录索引变量变化前的值
	private Map<String, Object> indexChangeBefore = new HashMap<String, Object>();
	// 是否初始化完毕
	private boolean isInitFinished;
	
	/**
	 * 依赖服务
	 */
	protected Map<String,List<String>> dependIdsMap;
	
	/**
	 * 扩展配置
	 */
	protected Map<String,List<String>> extraParamsMap;
	
	/**
	 * 表配置信息
	 */
	protected Map<String,List<String>> tableConfigsMap;
	

	public ScPrototype() {
		dependIdsMap = new HashMap<String,List<String>>();
		extraParamsMap = new HashMap<String,List<String>>();
		tableConfigsMap = new HashMap<String,List<String>>();
	}
	
	@Override
	public void obtainAfter() {
		isInitFinished = true;
		
		dependIdsMap = JsonUtil.gsonString2Obj(this.dependIds, new TypeToken<Map<String,List<String>>>(){}.getType());
		extraParamsMap = JsonUtil.gsonString2Obj(this.extraParams, new TypeToken<Map<String,List<String>>>(){}.getType());
		tableConfigsMap = JsonUtil.gsonString2Obj(this.tableConfigs, new TypeToken<Map<String,List<String>>>(){}.getType());
	}

	@Override
	public void saveBefore() {
		this.dependIds = JsonUtil.gsonObj2String(dependIdsMap);
		this.extraParams = JsonUtil.gsonObj2String(extraParamsMap);
		this.tableConfigs = JsonUtil.gsonObj2String(tableConfigsMap);
	}

	@Override
	public void saveAfter() {
	
	}
	
	@Override
	public Map<String, Object> getIndexChangeBefore() {
		return indexChangeBefore;
	}
	
	@Override
	public void setId(String id) {
		if(isInitFinished){
			Object obj = indexChangeBefore.get("id");
			if (obj == null) {
				indexChangeBefore.put("id", this.id);
			}
		}
		this.id = id;
	}
	
	public static ScPrototype create(byte[] bytes) throws IOException {
        ScPrototype pojo = new ScPrototype();
        pojo.initField(bytes);
        pojo.obtainAfter();// 取数据后
        return pojo;
    }

	public Map<String,List<String>> getDependIdsMap() {
		return dependIdsMap;
	}

	public void setDependIdsMap(Map<String,List<String>> dependIdsMap) {
		this.dependIdsMap = dependIdsMap;
	}
	public Map<String,List<String>> getExtraParamsMap() {
		return extraParamsMap;
	}

	public void setExtraParamsMap(Map<String,List<String>> extraParamsMap) {
		this.extraParamsMap = extraParamsMap;
	}
	public Map<String,List<String>> getTableConfigsMap() {
		return tableConfigsMap;
	}

	public void setTableConfigsMap(Map<String,List<String>> tableConfigsMap) {
		this.tableConfigsMap = tableConfigsMap;
	}
	
	@Override
	public ScPrototype cloneEntity(boolean isSaveBefore){
		ScPrototype entity=new ScPrototype();
		
		if(isSaveBefore){
			saveBefore();
		}

		entity.id=this.id;
		entity.dependIds=this.dependIds;
		entity.readme=this.readme;
		entity.extraParams=this.extraParams;
		entity.tableConfigs=this.tableConfigs;
		entity.path=this.path;
		entity.loaderType=this.loaderType;
		return entity;
	}
}