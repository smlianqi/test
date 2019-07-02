package com.elex.common.service.factory;

import com.elex.common.service.IService;
import com.elex.common.service.IServiceManager;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.configloader.IServiceConfigLoader;
import com.elex.common.service.type.ServiceType;

/**
 * service工厂
 * 
 * @author mausmars
 *
 */
public interface IServiceFactory {
	/**
	 * 创建服务
	 * 
	 * @param config
	 * @param cm
	 * @param sm
	 */
	IService createService(IServiceConfig config);

	/**
	 * 创建服务
	 * 
	 * @param serviceType
	 * @param serviceId
	 * @return
	 */
	IService createService(ServiceType serviceType, String serviceId, IFunctionServiceConfig fsc);

	/**
	 * 获得配置加载器
	 * 
	 * @return
	 */
	IServiceConfigLoader getServiceConfigLoader();

	/**
	 * 获取服务管理器
	 * 
	 * @return
	 */
	IServiceManager getServiceManager();
}
