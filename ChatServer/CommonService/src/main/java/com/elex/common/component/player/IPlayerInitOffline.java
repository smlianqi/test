package com.elex.common.component.player;

/**
 * player登录登出接口
 * 
 * @author mausmars
 *
 */
public interface IPlayerInitOffline {
	/**
	 * 注册初始化
	 * 
	 * @param player
	 */
	void registerInit(long userId);

	/**
	 * 加载数据
	 */
	void uploadData(IPlayer player);

	/**
	 * 登录初始化
	 * 
	 * @param player
	 */
	void loginInit(IPlayer player);

	/**
	 * 离线
	 * 
	 * @param player
	 */
	void offline(IPlayer player, long time);
}
