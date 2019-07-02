package com.elex.common.service.initservice;

import com.elex.common.service.IGlobalContext;

/**
 * 初始回调
 * 
 * @author mausmars
 *
 */
public interface IInitCallback {
	/**
	 * 服务初始化前
	 */
	void serverInitBefore(IGlobalContext context);

	/**
	 * 服务初始化后
	 */
	void serverInitAfter(IGlobalContext context);

	/**
	 * 服务启动前
	 */
	void serverStartBefore(IGlobalContext context);

	/**
	 * 服务启动后
	 */
	void serverStartAfter(IGlobalContext context);
}
