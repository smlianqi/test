package com.elex.common.component.event.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elex.common.util.json.JsonUtil;
import com.google.gson.reflect.TypeToken;

/**
 * 事件配置 扩展实体类
 */
public class ScEvent extends ScEventGeneral {
	// 记录索引变量变化前的值
	private Map<String, Object> indexChangeBefore = new HashMap<String, Object>();
	// 是否初始化完毕
	private boolean isInitFinished;

	/**
	 * 依赖服务
	 */
	protected Map<String, List<String>> dependIdsMap;

	/**
	 * 扩展配置
	 */
	protected Map<String, String> extraParamsMap;

	public ScEvent() {
		dependIdsMap = new HashMap<String, List<String>>();
		extraParamsMap = new HashMap<String, String>();
	}

	@Override
	public void obtainAfter() {
		isInitFinished = true;

		dependIdsMap = JsonUtil.gsonString2Obj(this.dependIds, new TypeToken<Map<String, List<String>>>() {
		}.getType());
		extraParamsMap = JsonUtil.gsonString2Obj(this.extraParams, new TypeToken<Map<String, String>>() {
		}.getType());
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
		if (isInitFinished) {
			Object obj = indexChangeBefore.get("id");
			if (obj == null) {
				indexChangeBefore.put("id", this.id);
			}
		}
		this.id = id;
	}

	public static ScEvent create(byte[] bytes) throws IOException {
		ScEvent pojo = new ScEvent();
		pojo.initField(bytes);
		pojo.obtainAfter();// 取数据后
		return pojo;
	}

	public Map<String, List<String>> getDependIdsMap() {
		return dependIdsMap;
	}

	public void setDependIdsMap(Map<String, List<String>> dependIdsMap) {
		this.dependIdsMap = dependIdsMap;
	}

	public Map<String, String> getExtraParamsMap() {
		return extraParamsMap;
	}

	public void setExtraParamsMap(Map<String, String> extraParamsMap) {
		this.extraParamsMap = extraParamsMap;
	}

	@Override
	public ScEvent cloneEntity(boolean isSaveBefore) {
		ScEvent entity = new ScEvent();

		if (isSaveBefore) {
			saveBefore();
		}

		entity.id = this.id;
		entity.dependIds = this.dependIds;
		entity.readme = this.readme;
		entity.extraParams = this.extraParams;
		entity.threadCount = this.threadCount;
		return entity;
	}
}