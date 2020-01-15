package com.elex.common.service.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.elex.common.component.function.type.FunctionType;

import java.util.Properties;

/**
 * 运维配置
 * 
 * @author mausmars
 *
 */
public class ProvidingFunction {
	private String groupId;// 组id
	private String regionId;// 区id
	private FunctionType functionType;// 功能类型

	// {文件名:{属性:值}} 运维配置缓存
	private Map<String, Map<String, String>> propertiesMap = new HashMap<String, Map<String, String>>();

	public ProvidingFunction(FunctionType functionType, Properties properties) {
		this.functionType = functionType;
		init(properties);
	}

	private void init(Properties properties) {
		// groupId = properties.getProperty("group_id");
		regionId = properties.getProperty("region_id");

		for (Entry<Object, Object> entry : properties.entrySet()) {
			String keyStr = (String) entry.getKey();
			String[] keyStrs = keyStr.split("\\.");
			if (keyStrs.length == 2) {
				Map<String, String> pMap = propertiesMap.get(keyStrs[0]);
				if (pMap == null) {
					pMap = new HashMap<String, String>();
					propertiesMap.put(keyStrs[0], pMap);
				}
				pMap.put(keyStrs[1], (String) entry.getValue());
			}
		}
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public FunctionType getFunctionType() {
		return functionType;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public Map<String, Map<String, String>> getPropertiesMap() {
		return propertiesMap;
	}

	public void setPropertiesMap(Map<String, Map<String, String>> propertiesMap) {
		this.propertiesMap = propertiesMap;
	}
}
