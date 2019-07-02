package com.elex.common.service.config;

import java.util.List;
import java.util.Map;

import com.elex.common.service.type.ServiceType;

/**
 * 服务配置接口
 * 
 * @author mausmars
 *
 */
public interface IServiceConfig {
	/**
	 * 服务id
	 * 
	 * @return
	 */
	String getServiceId();

	/**
	 * 配置类型
	 * 
	 * @return
	 */
	ServiceType getServiceType();

	IFunctionServiceConfig getFunctionServiceConfig();

	/**
	 * 获取配置
	 * 
	 * @return
	 */
	<T> T getConfig();

	Map<String, List<String>> getDependIdsMap();

	/**
	 * 
	 * @return
	 */
	Object getAttach(Object key);
}
