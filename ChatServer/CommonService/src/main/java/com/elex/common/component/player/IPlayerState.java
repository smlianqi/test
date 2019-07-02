package com.elex.common.component.player;

import com.elex.common.component.player.type.PlayerStateType;

/**
 * player状态
 * 
 * @author mausmars
 * 
 */
public interface IPlayerState {
	/**
	 * 类型
	 */
	PlayerStateType getType();

	/**
	 * 进入状态
	 */
	void enter(IPlayer player);

	/**
	 * 退出状态
	 */
	void exit(IPlayer player);

	/**
	 * 持续状态
	 */
	void update(IPlayer player);
}
