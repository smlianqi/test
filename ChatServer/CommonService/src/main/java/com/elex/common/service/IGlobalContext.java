package com.elex.common.service;

import com.elex.common.service.module.IModuleServiceMgr;

/**
 * 全局上下文
 * 
 * @author mausmars
 *
 */
public interface IGlobalContext {
	/**
	 * 服务器启动时间
	 * 
	 * @return
	 */
	long getStartTime();

	/**
	 * 服务器id
	 * 
	 * @return
	 */
	long getServerId();

	/**
	 * 创建唯一id
	 * 
	 * @return
	 */
	long createUniqueId();

	/**
	 * 服务管理
	 * 
	 * @return
	 */
	IServiceManager getServiceManager();

	/**
	 * 获取模块服务管理器
	 * 
	 * @return
	 */
	IModuleServiceMgr getModuleServiceMgr();
}