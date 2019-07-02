package com.elex.common.component.player;

import com.elex.common.component.player.service.IPlayerMgrService;
import com.elex.common.component.player.type.UserType;

/**
 * player工厂
 * 
 * @author mausmars
 * 
 */
public interface IPlayerFactory {
	/**
	 * 创建player
	 * 
	 * @param id
	 * @param keyId
	 * @return
	 */
	IPlayer createPlayer(long userId, UserType userType);

	/**
	 * 获取player管理服务
	 * 
	 * @return
	 */
	IPlayerMgrService getPlayerMgrService();
}
