package com.elex.common.component.rpc.config;

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
 * rpc配置（通信类型1） 扩展实体类
 */
public class ScRpc extends ScRpcGeneral {
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
	 * 服务对象id
	 */
	protected List<List<String>> interfaceInfosList;
	

	public ScRpc() {
		dependIdsMap = new HashMap<String,List<String>>();
		extraParamsMap = new HashMap<String,String>();
		interfaceInfosList = new ArrayList<List<String>>();
	}
	
	@Override
	public void obtainAfter() {
		isInitFinished = true;
		
		dependIdsMap = JsonUtil.gsonString2Obj(this.dependIds, new TypeToken<Map<String,List<String>>>(){}.getType());
		extraParamsMap = JsonUtil.gsonString2Obj(this.extraParams, new TypeToken<Map<String,String>>(){}.getType());
		interfaceInfosList = JsonUtil.gsonString2Obj(this.interfaceInfos, new TypeToken<List<List<String>>>(){}.getType());
	}

	@Override
	public void saveBefore() {
		this.dependIds = JsonUtil.gsonObj2String(dependIdsMap);
		this.extraParams = JsonUtil.gsonObj2String(extraParamsMap);
		this.interfaceInfos = JsonUtil.gsonObj2String(interfaceInfosList);
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
	
	public static ScRpc create(byte[] bytes) throws IOException {
        ScRpc pojo = new ScRpc();
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
	public List<List<String>> getInterfaceInfosList() {
		return interfaceInfosList;
	}

	public void setInterfaceInfosList(List<List<String>> interfaceInfosList) {
		this.interfaceInfosList = interfaceInfosList;
	}
	
	@Override
	public ScRpc cloneEntity(boolean isSaveBefore){
		ScRpc entity=new ScRpc();
		
		if(isSaveBefore){
			saveBefore();
		}

		entity.id=this.id;
		entity.dependIds=this.dependIds;
		entity.readme=this.readme;
		entity.extraParams=this.extraParams;
		entity.netServiceType=this.netServiceType;
		entity.netProtocolType=this.netProtocolType;
		entity.host=this.host;
		entity.port=this.port;
		entity.interfaceInfos=this.interfaceInfos;
		return entity;
	}
}