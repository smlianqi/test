package com.elex.common.service.module;

/**
 * 逻辑模块服务
 * 
 * @author mausmars
 *
 */
public interface IModuleService {
	/**
	 * 模板类型
	 * 
	 * @return
	 */
	ModuleServiceType getModuleServiceType();

	/**
	 * 初始化
	 */
	void init();

	<T extends IModuleService> T getModuleService(ModuleServiceType moduleServiceType);
}
