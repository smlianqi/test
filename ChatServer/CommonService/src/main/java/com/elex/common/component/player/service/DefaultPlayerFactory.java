package com.elex.common.component.player.service;

import com.elex.common.component.player.DefaultPlayer;
import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.IPlayerFactory;
import com.elex.common.component.player.type.UserType;
import com.elex.common.service.IGlobalContext;

public class DefaultPlayerFactory implements IPlayerFactory {
	protected IGlobalContext context;
	protected IPlayerMgrService playerMgrService;

	@Override
	public IPlayer createPlayer(long userId, UserType userType) {
		IPlayer p = playerMgrService.select(userId);
		if (p != null) {
			return p;
		}
		DefaultPlayer player = new DefaultPlayer(userId, userType);
		player.setContext(context);
		// player.setSession((ISession) params);// 这里先设置session
		// -----------------------
		// 事件中心管理
		// DefaultEventCentreService eventCentreService = new
		// DefaultEventCentreService();

		// -----------------------
		// 这里加入用户管理服务，方便移除
		player.insert(playerMgrService);
		// player.insert(eventCentreService);

		// 插入player
		p = playerMgrService.insert(player);
		return p;
	}

	@Override
	public IPlayerMgrService getPlayerMgrService() {
		return playerMgrService;
	}

	// -------------------------------------
	public void setPlayerManager(IPlayerMgrService playerMgrService) {
		this.playerMgrService = playerMgrService;
	}

	public void setContext(IGlobalContext context) {
		this.context = context;
	}

}
