package com.elex.common.component.function.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elex.common.util.json.JsonUtil;
import com.google.gson.reflect.TypeToken;

/**
 * 功能配置(类型201) 扩展实体类
 */
public class ScFunction extends ScFunctionGeneral {
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
	protected Map<String,String> extraParamsMap;
	
	/**
	 * 组过滤
	 */
	protected List<String> groupFiltersList;
	
	/**
	 * 区过滤
	 */
	protected List<String> regionFiltersList;
	
	/**
	 * 功能管理配置
	 */
	protected List<String> functionFiltersList;
	

	public ScFunction() {
		dependIdsMap = new HashMap<String,List<String>>();
		extraParamsMap = new HashMap<String,String>();
		groupFiltersList = new ArrayList<String>();
		regionFiltersList = new ArrayList<String>();
		functionFiltersList = new ArrayList<String>();
	}
	
	@Override
	public void obtainAfter() {
		isInitFinished = true;
		
		dependIdsMap = JsonUtil.gsonString2Obj(this.dependIds, new TypeToken<Map<String,List<String>>>(){}.getType());
		extraParamsMap = JsonUtil.gsonString2Obj(this.extraParams, new TypeToken<Map<String,String>>(){}.getType());
		groupFiltersList = JsonUtil.gsonString2Obj(this.groupFilters, new TypeToken<List<String>>(){}.getType());
		regionFiltersList = JsonUtil.gsonString2Obj(this.regionFilters, new TypeToken<List<String>>(){}.getType());
		functionFiltersList = JsonUtil.gsonString2Obj(this.functionFilters, new TypeToken<List<String>>(){}.getType());
	}

	@Override
	public void saveBefore() {
		this.dependIds = JsonUtil.gsonObj2String(dependIdsMap);
		this.extraParams = JsonUtil.gsonObj2String(extraParamsMap);
		this.groupFilters = JsonUtil.gsonObj2String(groupFiltersList);
		this.regionFilters = JsonUtil.gsonObj2String(regionFiltersList);
		this.functionFilters = JsonUtil.gsonObj2String(functionFiltersList);
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
	
	public static ScFunction create(byte[] bytes) throws IOException {
        ScFunction pojo = new ScFunction();
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
	public Map<String,String> getExtraParamsMap() {
		return extraParamsMap;
	}

	public void setExtraParamsMap(Map<String,String> extraParamsMap) {
		this.extraParamsMap = extraParamsMap;
	}
	public List<String> getGroupFiltersList() {
		return groupFiltersList;
	}

	public void setGroupFiltersList(List<String> groupFiltersList) {
		this.groupFiltersList = groupFiltersList;
	}
	public List<String> getRegionFiltersList() {
		return regionFiltersList;
	}

	public void setRegionFiltersList(List<String> regionFiltersList) {
		this.regionFiltersList = regionFiltersList;
	}
	public List<String> getFunctionFiltersList() {
		return functionFiltersList;
	}

	public void setFunctionFiltersList(List<String> functionFiltersList) {
		this.functionFiltersList = functionFiltersList;
	}
	
	@Override
	public ScFunction cloneEntity(boolean isSaveBefore){
		ScFunction entity=new ScFunction();
		
		if(isSaveBefore){
			saveBefore();
		}

		entity.id=this.id;
		entity.dependIds=this.dependIds;
		entity.readme=this.readme;
		entity.extraParams=this.extraParams;
		entity.rootPath=this.rootPath;
		entity.groupFilters=this.groupFilters;
		entity.regionFilters=this.regionFilters;
		entity.functionFilters=this.functionFilters;
		return entity;
	}
}