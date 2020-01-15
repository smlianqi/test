package com.elex.common.service.module;

import com.elex.common.component.player.IPlayerInitOffline;

public interface IModuleServiceMgr extends IPlayerInitOffline {
	/**
	 * 初始化服务
	 */
	void initMService();

	/**
	 * 获取指定服务
	 * 
	 * @param serviceType
	 * @param serviceId
	 * @return
	 */
	<T extends IModuleService> T getService(ModuleServiceType serviceType);

	/**
	 * 插入服务，保证一个服务只有一个service
	 * 
	 * @param service
	 */
	<T extends IModuleService> T insertService(IModuleService service);
}
