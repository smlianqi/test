package com.elex.im.module.servergate;

import com.elex.common.component.net.config.NetCustomConfig;
import com.elex.common.component.player.service.DefaultPlayerFactory;
import com.elex.common.component.player.service.DefaultPlayerMgrService;
import com.elex.common.message.FlatMessageCreater;
import com.elex.common.message.PrototMessageCreater;
import com.elex.common.service.GeneralConstant;
import com.elex.common.service.ServiceManager;
import com.elex.common.service.configloader.LocalServiceConfigLoader;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.initservice.InitServiceFilter;
import com.elex.common.service.module.IModuleServiceConfig;
import com.elex.im.message.FlatModuleMessageCreater;
import com.elex.im.message.PrototModuleMessageCreater;
import com.elex.im.module.MessageRounter;
import com.elex.im.module.common.ClosedSessionHandler;
import com.elex.im.module.common.CreateSessionHandler;
import com.elex.im.module.listener.ClientConnectListener;
import com.elex.im.module.listener.IClientConnectListener;
import com.elex.im.module.servergate.module.GateHandlerConfig;

/**
 * 逻辑服务
 * 
 * @author mausmars
 *
 */
public class GateServer {
	private GateService serverService;
	private IClientConnectListener clientConnectListener;

	public void init(ServiceManager sm) {
		GateService serverService = new GateService(sm);
		clientConnectListener = new ClientConnectListener(serverService);
		initServer(serverService);
		this.serverService = serverService;
	}

	public void init() {
		GateService serverService = new GateService();
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

	private void initServer(GateService serverService) {
		// 设置处理器
		NetCustomConfig netCustomConfig = new NetCustomConfig();
		netCustomConfig.insertMessageCreater(new FlatMessageCreater());
		netCustomConfig.insertMessageCreater(new PrototMessageCreater());
		netCustomConfig.insertModuleMessageCreater(new FlatModuleMessageCreater());
		netCustomConfig.insertModuleMessageCreater(new PrototModuleMessageCreater());

		IModuleServiceConfig serverHandler = setServerHandler(serverService, netCustomConfig);
		IModuleServiceConfig clientHandler = setClientHandler(serverService, netCustomConfig);

		// 配置加载器
		LocalServiceConfigLoader serviceConfigLoader = new LocalServiceConfigLoader();
		serviceConfigLoader.putCustomConfig(netCustomConfig);// 网络通信配置

		// 处理链
		DefaultFilterChain initFilterChain = new DefaultFilterChain("init_chain");
		initFilterChain.insertNodeToLast(new InitServiceFilter());

		serverService.setInitFilterChain(initFilterChain);
		serverService.setServiceConfigLoader(serviceConfigLoader);

		// 模块服务
		serverService.setModuleServiceConfig(serverHandler);
		serverService.setModuleServiceConfig(clientHandler);
	}

	private IModuleServiceConfig setServerHandler(GateService serverService, NetCustomConfig netCustomConfig) {
		// ProtoGateHandlerConfig handlerConfig = new ProtoGateHandlerConfig();
		GateHandlerConfig handlerConfig = new GateHandlerConfig();

		handlerConfig.createMessageConfigs(serverService);

		// 设置消息路由
		handlerConfig.setMessageRounter(new MessageRounter());

		DefaultPlayerFactory playerFactory = new DefaultPlayerFactory();
		playerFactory.setPlayerManager(new DefaultPlayerMgrService());
		playerFactory.setContext(serverService);
		// 设置player工厂
		handlerConfig.setPlayerFactory(playerFactory);

		// 添加session创建处理器
		handlerConfig.addCreateSessionHandler(new CreateSessionHandler());
		// 添加session关闭处理器
		handlerConfig.addCloseSessionHandlers(new ClosedSessionHandler(serverService));
		netCustomConfig.putNetHandlerConfig(GeneralConstant.NetClientServiceId_C2G, handlerConfig);
		return handlerConfig;
	}

	private IModuleServiceConfig setClientHandler(GateService serverService, NetCustomConfig netCustomConfig) {
		// ProtoGateHandlerConfig handlerConfig = new ProtoGateHandlerConfig();
		GateHandlerConfig handlerConfig = new GateHandlerConfig();

		handlerConfig.createMessageConfigs(serverService);

		// 设置消息路由
		handlerConfig.setMessageRounter(new MessageRounter());

		DefaultPlayerFactory playerFactory = new DefaultPlayerFactory();
		playerFactory.setPlayerManager(new DefaultPlayerMgrService());
		playerFactory.setContext(serverService);
		// 设置player工厂
		handlerConfig.setPlayerFactory(playerFactory);

		// 添加session创建处理器
		handlerConfig.addCreateSessionHandler(new CreateSessionHandler());
		// 添加session关闭处理器
		handlerConfig.addCloseSessionHandlers(new ClosedSessionHandler(serverService));
		// 设置客户端连接监听器
		handlerConfig.setListener(clientConnectListener);
		// 这个client为了连接其他server暴露的net服务
		netCustomConfig.putNetHandlerConfig(GeneralConstant.NetServerServiceId_S2S, handlerConfig);
		return handlerConfig;
	}

	public void stop() {
		serverService.shutdown();
	}
}
