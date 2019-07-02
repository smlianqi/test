package com.elex.im.module.serverclient.imitateclient;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.service.IPlayerMgrService;
import com.elex.common.service.IGlobalContext;
import com.elex.im.module.serverclient.imitateclient.module.usertestconfig.IUserTestConfigManager;

/**
 * client服上下文接口
 * 
 * @author mausmars
 *
 */
public interface IClientContext extends IGlobalContext {
	/**
	 * 玩家管理
	 * 
	 * @return
	 */
	IPlayerMgrService getPlayerManager(FunctionType functionType);

	/**
	 * 
	 * @return
	 */
	IUserTestConfigManager getUserTestConfigManager();
}
