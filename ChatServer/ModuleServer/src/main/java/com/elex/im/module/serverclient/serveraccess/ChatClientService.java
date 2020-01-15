package com.elex.im.module.serverclient.serveraccess;

import java.util.HashMap;
import java.util.Map;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.service.DefaultPlayerFactory;
import com.elex.common.component.player.service.DefaultPlayerMgrService;
import com.elex.common.component.player.service.IPlayerMgrService;
import com.elex.common.service.ServerService;

public class ChatClientService extends ServerService implements IChatClientContext {
	private Map<FunctionType, DefaultPlayerMgrService> playerManagerMap;

	public ChatClientService(FunctionType functionType) {
		super(functionType);
		playerManagerMap = new HashMap<>();
		init();
	}

	private void init() {
		initPlayerFactoryMananger(FunctionType.logic);

	}

	private void initPlayerFactoryMananger(FunctionType functionType) {
		DefaultPlayerMgrService playerManager = new DefaultPlayerMgrService();
		DefaultPlayerFactory playerFactory = new DefaultPlayerFactory();
		playerFactory.setPlayerManager(playerManager);
		playerFactory.setContext(this);

		playerManagerMap.put(functionType, playerManager);
	}

	/**
	 * 开始服务
	 */
	@Override
	public void startup(boolean isBlock) {
		if (logger.isInfoEnabled()) {
			logger.info("ProtoChatClientAccessService startup!");
		}
		// 调用父类启动流程
		super.startup(isBlock);
	}

	@Override
	public IPlayerMgrService getPlayerManager(FunctionType functionType) {
		return playerManagerMap.get(functionType);
	}
}
