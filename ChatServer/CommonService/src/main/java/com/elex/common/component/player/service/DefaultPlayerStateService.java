package com.elex.common.component.player.service;

import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.IPlayerState;
import com.elex.common.component.player.IPlayerStateFactory;
import com.elex.common.component.player.type.PlayerMServiceType;
import com.elex.common.component.player.type.PlayerStateType;

/**
 * player状态机
 * 
 * @author mausmars
 * 
 */
public class DefaultPlayerStateService implements IPlayerStateService {
	protected IPlayerStateFactory playerStateFactory;
	// 用户
	protected IPlayer player;
	// 用户状态
	protected volatile IPlayerState currentState;

	public DefaultPlayerStateService(IPlayer player, IPlayerStateFactory playerStateFactory) {
		this.player = player;
		this.playerStateFactory = playerStateFactory;
		this.currentState = playerStateFactory.createPlayerState(PlayerStateType.PS_Create);
	}

	@Override
	public PlayerMServiceType getType() {
		return PlayerMServiceType.State;
	}

	// 改变状态
	public boolean changeState(PlayerStateType state) {
		IPlayerState nextState = playerStateFactory.createPlayerState(state);
		if (nextState == null) {
			return false;
		}
		// 退出状态
		currentState.exit(player);
		currentState = nextState;
		currentState.enter(player);
		return true;
	}

	public IPlayer getPlayer() {
		return player;
	}

	public IPlayerState getCurrentState() {
		return currentState;
	}
}
