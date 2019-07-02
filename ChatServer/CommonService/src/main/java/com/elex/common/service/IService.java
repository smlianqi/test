package com.elex.common.service;

import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceStateType;

/**
 * 服务接口
 * 
 * @author mausmars
 *
 */
public interface IService {
	/**
	 * 服务配置（服务归属;组，区）
	 * 
	 * @return
	 */
	<T extends IServiceConfig> T getConfig();

	IFunctionServiceConfig getFunctionServiceConfig();

	/**
	 * 初始化
	 */
	void init();

	/**
	 * 开始服务
	 */
	void startup();

	/**
	 * 停止服务
	 */
	void shutdown();

	/**
	 * 得到服务器状态
	 * 
	 * @return
	 */
	ServiceStateType getServiceStateType();

	IGlobalContext getGlobalContext();
}
