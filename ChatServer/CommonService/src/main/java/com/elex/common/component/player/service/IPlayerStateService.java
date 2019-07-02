package com.elex.common.component.player.service;

import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.IPlayerState;
import com.elex.common.component.player.type.PlayerStateType;

/**
 * player状态管理
 * 
 * @author mausmars
 * 
 */
public interface IPlayerStateService extends IPlayerMService {
	/**
	 * 改变状态
	 * 
	 * @param state
	 * @return
	 */
	boolean changeState(PlayerStateType state);

	/**
	 * 对应的Player
	 * 
	 * @return
	 */
	IPlayer getPlayer();

	/**
	 * 当前状态
	 * 
	 * @return
	 */
	IPlayerState getCurrentState();
}
