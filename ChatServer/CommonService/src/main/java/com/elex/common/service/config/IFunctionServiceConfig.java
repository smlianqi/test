package com.elex.common.service.config;

import java.util.Map;

import com.elex.common.component.function.type.FunctionType;

/**
 * 功能服务配置
 * 
 * @author mausmars
 *
 */
public interface IFunctionServiceConfig {
	/**
	 * 服务id
	 * 
	 * @return
	 */
	String getGroupId();

	/**
	 * 区id
	 * 
	 * @return
	 */
	String getRegionId();

	/**
	 * 配置类型
	 * 
	 * @return
	 */
	FunctionType getFunctionType();

	/**
	 * 获取配置
	 * 
	 * @return
	 */
	<T> T getConfig();

	/**
	 * {文件名:{属性:值}} 运维配置缓存
	 * 
	 * @return
	 */
	Map<String, Map<String, String>> getPropertiesMap();
}
