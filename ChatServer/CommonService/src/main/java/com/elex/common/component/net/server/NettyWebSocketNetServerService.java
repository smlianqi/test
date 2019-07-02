package com.elex.common.component.net.server;

import java.util.List;

import com.elex.common.component.net.config.ChannelOption;
import com.elex.common.component.net.config.NetCustomConfig;
import com.elex.common.component.net.config.ScNetserver;
import com.elex.common.component.net.handlerconfig.INetHandlerConfig;
import com.elex.common.component.threadpool.IPoolExecutor;
import com.elex.common.net.INetServer;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.message.MessageConfigMgr;
import com.elex.common.net.service.netty.ChannelOptionConfig;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.net.service.netty.NettyWebSocketServer;
import com.elex.common.net.service.netty.filter.websocket.MessageWebSocketHandler;
import com.elex.common.net.service.netty.handler.NettyTcpHandler;
import com.elex.common.net.service.netty.session.NettySessionFactory;
import com.elex.common.net.session.SessionManager;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.net.type.NetProtocolType;
import com.elex.common.service.GeneralConstant;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IServiceConfig;

public class NettyWebSocketNetServerService extends ANettyServerService {
	public NettyWebSocketNetServerService(IServiceConfig serviceConfig, IGlobalContext globalContext,
			ICustomConfig customConfig) {
		super(serviceConfig, globalContext, customConfig);
	}

	@Override
	protected INetServer craeteNetServer() {
		return new NettyWebSocketServer();
	}

	protected NettyNetConfig creatNetServerConfig(int port) {
		ScNetserver c = getSConfig();

		INetHandlerConfig netHandlerConfig = getNetHandlerConfig();

		// 处理器配置
		MegProtocolType megProtocolType = MegProtocolType.valueOf(c.getMegProtocolType());
		List<MessageConfig> messageConfigs = netHandlerConfig.getMessageConfigs(megProtocolType);

		// 处理器配置
		MessageConfigMgr messageConfigMgr = new MessageConfigMgr(messageConfigs);

		IPoolExecutor poolExecutor = null;
		if (threadPoolService != null) {
			poolExecutor = threadPoolService.getPoolExecutor();
		}
		// 网络配置
		ScNetserver ns = getSConfig();
		NettyNetConfig config = new NettyNetConfig();
		// 网络协议
		config.setNetProtocolType(NetProtocolType.valueOf(ns.getNetProtocolType()));
		config.setPort(port);
		config.setSubReactorThreadNum(ns.getSubReactorThread());
		config.setHandlerThreadNum(ns.getHandlerThread());
		config.setAllIdleTimeSeconds(ns.getAllIdleTimeSeconds());
		config.setReaderIdleTimeSeconds(ns.getReaderIdleTimeSeconds());
		config.setWriterIdleTimeSeconds(ns.getWriterIdleTimeSeconds());
		config.setMessageConfigMgr(messageConfigMgr);
		config.setCommandMessageFactory(getCommandMessageFactory());
		config.setMegProtocolType(megProtocolType);

		MessageWebSocketHandler messageHandler = new MessageWebSocketHandler(config);
		messageHandler.setPoolExecutor(poolExecutor);
		messageHandler.setMessageConfigMgr(messageConfigMgr);

		SessionManager sessionManager = new SessionManager();
		sessionManager.setMessageConfigMgr(messageConfigMgr);
		sessionManager.setPlayerFactory(netHandlerConfig.getPlayerFactory());
		this.sessionManager = sessionManager;

		NettySessionFactory sessionFactory = new NettySessionFactory();
		sessionFactory.setService(this);
		sessionFactory.setMessageHandler(messageHandler);
		sessionFactory.setSessionManager(sessionManager);
		sessionFactory.setNetCustomConfig((NetCustomConfig) customConfig);

		NettyTcpHandler gameHandler = new NettyTcpHandler();
		gameHandler.setMessageHandler(messageHandler);
		gameHandler.setCreateSessionHandlers(netHandlerConfig.getCreateSessionHandlers());
		gameHandler.setCloseSessionHandlers(netHandlerConfig.getCloseSessionHandlers());
		gameHandler.setSessionFactory(sessionFactory);

		config.setHandler(gameHandler);
		config.setSessionFactory(sessionFactory);

		// 优化配置
		ChannelOptionConfig coc = createChannelOptionConfig();
		config.setChannelOptionConfig(coc);
		return config;
	}
}
