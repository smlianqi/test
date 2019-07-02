package com.elex.common.service.configloader;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.config.ProvidingFunction;
import com.elex.common.service.type.ServiceType;

/**
 * 服务配置加载器
 * 
 * @author mausmars
 *
 */
public interface IServiceConfigLoader {
	/**
	 * 读取运维配置
	 * 
	 * @param functionType
	 * @return
	 */
	ProvidingFunction loadOperationConfig(FunctionType functionType);

	/**
	 * 获取功能服务器配置
	 * 
	 * @param groupId
	 * @param regionId
	 * @param functionType
	 * @return
	 */
	IFunctionServiceConfig loadConfigFSC(ProvidingFunction providingFunction);

	/**
	 * 加载配置
	 * 
	 * @return
	 */
	IServiceConfig loadConfigSC(ServiceType serviceType, String serviceId, IFunctionServiceConfig fsc);

	/**
	 * 加载配置
	 * 
	 * @return
	 */
	IServiceConfig loadConfigSC(String serviceType, String serviceId, IFunctionServiceConfig fsc);

	ICustomConfig getCustomConfig(ServiceType serviceType);
}
