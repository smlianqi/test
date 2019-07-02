package com.elex.common.component.player.service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.type.PlayerMServiceType;

/**
 * 用户管理类
 * 
 * @author mausmars
 * 
 */
public class DefaultPlayerMgrService implements IPlayerMgrService {
	// {用户id : player}
	protected ConcurrentMap<Long, IPlayer> playerMap = new ConcurrentHashMap<Long, IPlayer>();

	@Override
	public PlayerMServiceType getType() {
		return PlayerMServiceType.PlayerMgr;
	}

	@Override
	public <T extends IPlayer> T insert(IPlayer player) {
		IPlayer p = playerMap.putIfAbsent(player.getUserId(), player);
		if (p == null) {
			p = player;
		}
		return (T) p;
	}

	@Override
	public <T extends IPlayer> T select(long userId) {
		return (T) playerMap.get(userId);
	}

	@Override
	public <T extends IPlayer> T remove(long userId) {
		IPlayer p = playerMap.remove(userId);
		if (p == null) {
			return null;
		} else {
			return (T) p;
		}
	}

	@Override
	public void removerAllPlayer() {
		playerMap.clear();
	}

	@Override
	public List<IPlayer> getAllPlayer() {
		List<IPlayer> players = new LinkedList<>();
		players.addAll(playerMap.values());
		return players;
	}
}
