package com.elex.common.component.net.config;

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
public class ChannelOption extends ChannelOptionGeneral {
	// 记录索引变量变化前的值
	private Map<String, Object> indexChangeBefore = new HashMap<String, Object>();
	// 是否初始化完毕
	private boolean isInitFinished;
	
	/**
	 * 扩展配置
	 */
	protected Map<String,String> extraParamsMap;
	

	public ChannelOption() {
		extraParamsMap = new HashMap<String,String>();
	}
	
	@Override
	public void obtainAfter() {
		isInitFinished = true;
		
		extraParamsMap = JsonUtil.gsonString2Obj(this.extraParams, new TypeToken<Map<String,String>>(){}.getType());
	}

	@Override
	public void saveBefore() {
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
	
	public static ChannelOption create(byte[] bytes) throws IOException {
        ChannelOption pojo = new ChannelOption();
        pojo.initField(bytes);
        pojo.obtainAfter();// 取数据后
        return pojo;
    }

	public Map<String,String> getExtraParamsMap() {
		return extraParamsMap;
	}

	public void setExtraParamsMap(Map<String,String> extraParamsMap) {
		this.extraParamsMap = extraParamsMap;
	}
	
	@Override
	public ChannelOption cloneEntity(boolean isSaveBefore){
		ChannelOption entity=new ChannelOption();
		
		if(isSaveBefore){
			saveBefore();
		}

		entity.id=this.id;
		entity.readme=this.readme;
		entity.extraParams=this.extraParams;
		entity.backlog=this.backlog;
		entity.keepalive=this.keepalive;
		entity.nodelay=this.nodelay;
		entity.reuseaddr=this.reuseaddr;
		entity.linger=this.linger;
		entity.timeout=this.timeout;
		entity.rcvbuf=this.rcvbuf;
		entity.sndbuf=this.sndbuf;
		entity.connectTimeout=this.connectTimeout;
		return entity;
	}
}