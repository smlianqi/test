package com.elex.im.module.serverlogic;

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
import com.elex.im.module.serverlogic.module.LogicHandlerConfig;

/**
 * 逻辑服务
 * 
 * @author mausmars
 *
 */
public class LogicServer {
	private LogicService serverService;

	public void init(ServiceManager sm) {
		LogicService serverService = new LogicService(sm);
		initServer(serverService);
		this.serverService = serverService;
	}

	public void init() {
		LogicService serverService = new LogicService();
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

	private void initServer(LogicService serverService) {
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

	private IModuleServiceConfig setServerHandler(LogicService serverService, NetCustomConfig netCustomConfig) {
		// ProtoLogicHandlerConfig handlerConfig = new ProtoLogicHandlerConfig();
		LogicHandlerConfig handlerConfig = new LogicHandlerConfig();

		// 设置消息路由
		handlerConfig.setMessageRounter(new MessageRounter());

		DefaultPlayerFactory playerFactory = new DefaultPlayerFactory();
		playerFactory.setPlayerManager(new DefaultPlayerMgrService());
		playerFactory.setContext(serverService);
		// 设置player工厂
		handlerConfig.setPlayerFactory(playerFactory);

		handlerConfig.createMessageConfigs(serverService);
		// 添加session创建处理器
		handlerConfig.addCreateSessionHandler(new CreateSessionHandler());
		// 添加session关闭处理器
		handlerConfig.addCloseSessionHandlers(new ClosedSessionHandler(serverService));
		netCustomConfig.putNetHandlerConfig(GeneralConstant.NetServerServiceId_S2S, handlerConfig);
		return handlerConfig;
	}

	private IModuleServiceConfig setClientHandler(LogicService serverService, NetCustomConfig netCustomConfig) {
		// ProtoLogicHandlerConfig handlerConfig = new ProtoLogicHandlerConfig();
		LogicHandlerConfig handlerConfig = new LogicHandlerConfig();

		// 设置消息路由
		handlerConfig.setMessageRounter(new MessageRounter());

		DefaultPlayerFactory playerFactory = new DefaultPlayerFactory();
		playerFactory.setPlayerManager(new DefaultPlayerMgrService());
		playerFactory.setContext(serverService);
		// 设置player工厂
		handlerConfig.setPlayerFactory(playerFactory);

		handlerConfig.createMessageConfigs(serverService);
		// 添加session创建处理器
		handlerConfig.addCreateSessionHandler(new CreateSessionHandler());
		// 添加session关闭处理器
		handlerConfig.addCloseSessionHandlers(new ClosedSessionHandler(serverService));
		netCustomConfig.putNetHandlerConfig(GeneralConstant.NetClientServiceId_S2S, handlerConfig);
		return handlerConfig;
	}

	public void stop() {
		serverService.shutdown();
	}
}
