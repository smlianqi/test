package com.elex.common.component.player.service;

import java.util.List;

import com.elex.common.component.player.IPlayer;

/**
 * player对象管理（ 管理全部的对象）
 * 
 * @author mausmars
 * 
 */
public interface IPlayerMgrService extends IPlayerMService {
	/**
	 * 添加
	 * 
	 * @param player
	 */
	<T extends IPlayer> T insert(IPlayer player);

	/**
	 * 查询
	 * 
	 * @param id
	 * @return
	 */
	<T extends IPlayer> T select(long id);

	/**
	 * 移除
	 * 
	 * @param id
	 * @return
	 */
	<T extends IPlayer> T remove(long id);

	/**
	 * 获取全部用户
	 * 
	 * @return
	 */
	List<IPlayer> getAllPlayer();

	/**
	 * 移除全部player
	 * 
	 * @param id
	 * @return
	 */
	void removerAllPlayer();
}
