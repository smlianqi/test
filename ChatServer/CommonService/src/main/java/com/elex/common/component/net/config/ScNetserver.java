package com.elex.common.component.net.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elex.common.util.json.JsonUtil;
import com.google.gson.reflect.TypeToken;

/**
 * 服务端类型(类型300)（通信类型2） 扩展实体类
 */
public class ScNetserver extends ScNetserverGeneral {
	// 记录索引变量变化前的值
	private Map<String, Object> indexChangeBefore = new HashMap<String, Object>();
	// 是否初始化完毕
	private boolean isInitFinished;
	
	/**
	 * 依赖服务(类型，id)
	 */
	protected Map<String,List<String>> dependIdsMap;
	
	/**
	 * 扩展配置
	 */
	protected Map<String,String> extraParamsMap;
	

	public ScNetserver() {
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
	public void setId(String id) {
		if(isInitFinished){
			Object obj = indexChangeBefore.get("id");
			if (obj == null) {
				indexChangeBefore.put("id", this.id);
			}
		}
		this.id = id;
	}
	
	public static ScNetserver create(byte[] bytes) throws IOException {
        ScNetserver pojo = new ScNetserver();
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
	public ScNetserver cloneEntity(boolean isSaveBefore){
		ScNetserver entity=new ScNetserver();
		
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
		entity.outsideHost=this.outsideHost;
		entity.port=this.port;
		entity.subReactorThread=this.subReactorThread;
		entity.handlerThread=this.handlerThread;
		entity.allIdleTimeSeconds=this.allIdleTimeSeconds;
		entity.readerIdleTimeSeconds=this.readerIdleTimeSeconds;
		entity.writerIdleTimeSeconds=this.writerIdleTimeSeconds;
		entity.isRegister=this.isRegister;
		entity.connectCount=this.connectCount;
		entity.checkToken=this.checkToken;
		entity.megProtocolType=this.megProtocolType;
		return entity;
	}
}