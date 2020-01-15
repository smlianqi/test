package com.elex.im.module.serverclient.serveraccess;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.service.IPlayerMgrService;
import com.elex.common.service.IGlobalContext;

/**
 * client服上下文接口
 * 
 * @author mausmars
 *
 */
public interface IChatClientContext extends IGlobalContext {
	/**
	 * 玩家管理
	 * 
	 * @return
	 */
	IPlayerMgrService getPlayerManager(FunctionType functionType);
}
