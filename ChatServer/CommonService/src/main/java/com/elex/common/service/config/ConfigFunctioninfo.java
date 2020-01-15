package com.elex.common.service.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elex.common.util.json.JsonUtil;
import com.google.gson.reflect.TypeToken;

/**
 * 区配置 扩展实体类
 */
public class ConfigFunctioninfo extends ConfigFunctioninfoGeneral {
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
	

	public ConfigFunctioninfo() {
		dependIdsMap = new HashMap<String,List<String>>();
		extraParamsMap = new HashMap<String,String>();
	}
	
	@Override
	public void obtainAfter() {
		isInitFinished = true;
		
		dependIdsMap = JsonUtil.gsonString2Obj(this.dependIds, new TypeToken<Map<String,List<String>>>(){}.getType());
		extraParamsMap = JsonUtil.gsonString2Obj(this.extraParams, new TypeToken<Map<String,String>>(){}.getType());
	}

	@Override
	public void saveBefore() {
		this.dependIds = JsonUtil.gsonObj2String(dependIdsMap);
		this.extraParams = JsonUtil.gsonObj2String(extraParamsMap);
	}

	@Override
	public void saveAfter() {
	
	}
	
	@Override
	public Map<String, Object> getIndexChangeBefore() {
		return indexChangeBefore;
	}
	
	@Override
	public void setRegionId(String regionId) {
		if(isInitFinished){
			Object obj = indexChangeBefore.get("regionId");
			if (obj == null) {
				indexChangeBefore.put("regionId", this.regionId);
			}
		}
		this.regionId = regionId;
	}
	@Override
	public void setFunctionType(String functionType) {
		if(isInitFinished){
			Object obj = indexChangeBefore.get("functionType");
			if (obj == null) {
				indexChangeBefore.put("functionType", this.functionType);
			}
		}
		this.functionType = functionType;
	}
	@Override
	public void setGroupId(String groupId) {
		if(isInitFinished){
			Object obj = indexChangeBefore.get("groupId");
			if (obj == null) {
				indexChangeBefore.put("groupId", this.groupId);
			}
		}
		this.groupId = groupId;
	}
	
	public static ConfigFunctioninfo create(byte[] bytes) throws IOException {
        ConfigFunctioninfo pojo = new ConfigFunctioninfo();
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
	
	@Override
	public ConfigFunctioninfo cloneEntity(boolean isSaveBefore){
		ConfigFunctioninfo entity=new ConfigFunctioninfo();
		
		if(isSaveBefore){
			saveBefore();
		}

		entity.groupId=this.groupId;
		entity.functionType=this.functionType;
		entity.regionId=this.regionId;
		entity.dependIds=this.dependIds;
		entity.readme=this.readme;
		entity.extraParams=this.extraParams;
		return entity;
	}
}