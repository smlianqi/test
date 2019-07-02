package com.elex.im.module.serverclient.imitateclient;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.net.config.NetCustomConfig;
import com.elex.common.component.player.service.DefaultPlayerFactory;
import com.elex.common.component.player.service.DefaultPlayerMgrService;
import com.elex.common.message.FlatMessageCreater;
import com.elex.common.message.PrototMessageCreater;
import com.elex.common.service.configloader.LocalServiceConfigLoader;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.initservice.InitServiceFilter;
import com.elex.im.message.FlatModuleMessageCreater;
import com.elex.im.message.PrototModuleMessageCreater;
import com.elex.im.module.listener.ClientConnectListener;
import com.elex.im.module.listener.IClientConnectListener;
import com.elex.im.module.serverclient.imitateclient.module.Client2GateHandlerConfig;

public class ClientServer {
	private ClientService serverService;
	private IClientConnectListener clientConnectListener;

	public void init() {
		ClientService serverService = new ClientService(FunctionType.client);
		clientConnectListener = new ClientConnectListener(serverService);
		initServer(serverService);
		this.serverService = serverService;
	}

	public void start() {
		// 启动
		new Thread() {
			public void run() {
				serverService.startup(true);
			}
		}.start();
	}

	public void stop() {
		serverService.shutdown();
	}

	private void initServer(ClientService serverService) {
		NetCustomConfig netCustomConfig = new NetCustomConfig();
		netCustomConfig.insertMessageCreater(new FlatMessageCreater());
		netCustomConfig.insertMessageCreater(new PrototMessageCreater());
		netCustomConfig.insertModuleMessageCreater(new FlatModuleMessageCreater());
		netCustomConfig.insertModuleMessageCreater(new PrototModuleMessageCreater());

		// 设置处理器
		// ProtoClient2GateHandlerConfig client2GateHandlerConfig = new
		// ProtoClient2GateHandlerConfig();
		Client2GateHandlerConfig client2GateHandlerConfig = new Client2GateHandlerConfig();

		DefaultPlayerFactory playerFactory = new DefaultPlayerFactory();
		playerFactory.setPlayerManager(new DefaultPlayerMgrService());
		playerFactory.setContext(serverService);
		// 设置player工厂
		client2GateHandlerConfig.setPlayerFactory(playerFactory);
		client2GateHandlerConfig.createMessageConfigs(serverService);
		client2GateHandlerConfig.setListener(clientConnectListener);

		netCustomConfig.putNetHandlerConfig(ClientConstant.GateClientNetServerIdKey, client2GateHandlerConfig);

		// 配置加载器
		LocalServiceConfigLoader serviceConfigLoader = new LocalServiceConfigLoader();
		serviceConfigLoader.putCustomConfig(netCustomConfig);// 网络通信配置

		// 处理链
		DefaultFilterChain initFilterChain = new DefaultFilterChain("init_chain");
		initFilterChain.insertNodeToLast(new InitServiceFilter());

		serverService.setInitFilterChain(initFilterChain);
		serverService.setServiceConfigLoader(serviceConfigLoader);

		// 启动
		serverService.startup(false);
	}
}
