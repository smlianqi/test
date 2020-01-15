package com.elex.common.component.player;

import com.elex.common.component.player.type.PlayerStateType;

/**
 * player状态工厂
 * 
 * @author mausmars
 *
 */
public interface IPlayerStateFactory {
	/**
	 * 创建状态
	 * 
	 * @param state
	 * @return
	 */
	IPlayerState createPlayerState(PlayerStateType state);
}
