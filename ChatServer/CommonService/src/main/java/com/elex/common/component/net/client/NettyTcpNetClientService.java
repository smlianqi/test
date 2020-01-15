package com.elex.common.component.net.client;

import java.util.List;

import com.elex.common.component.function.IFunctionService;
import com.elex.common.component.member.IMemberService;
import com.elex.common.component.net.config.NetCustomConfig;
import com.elex.common.component.net.config.ScNetclient;
import com.elex.common.component.net.handlerconfig.INetHandlerConfig;
import com.elex.common.component.net.server.INettyNetServerService;
import com.elex.common.component.threadpool.IPoolExecutor;
import com.elex.common.net.INetClient;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.message.MessageConfigMgr;
import com.elex.common.net.rounter.ForwardService;
import com.elex.common.net.service.netty.ChannelOptionConfig;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.net.service.netty.NettyTcpClient;
import com.elex.common.net.service.netty.filter.tcp.MessageTcpHandler;
import com.elex.common.net.service.netty.handler.NettyTcpHandler;
import com.elex.common.net.service.netty.session.NettySessionFactory;
import com.elex.common.net.session.SessionManager;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.net.type.NetProtocolType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceType;

public class NettyTcpNetClientService extends ANettyClientService {
	private IFunctionService functionService;

	private INettyNetServerService client2server;

	private IMemberService memberService;

	public NettyTcpNetClientService(IServiceConfig serviceConfig, IGlobalContext globalContext,
			ICustomConfig customConfig) {
		super(serviceConfig, globalContext, customConfig);
	}

	@Override
	public void initService() throws Exception {
		super.initService();

		ScNetclient c = getSConfig();
		{
			List<String> sids = c.getDependIdsMap().get(ServiceType.function.name());
			if (sids != null) {
				functionService = getServiceManager().getService(ServiceType.function, sids.get(0));
			}
		}
		{
			List<String> sids = c.getDependIdsMap().get(ServiceType.netserver.name());
			if (sids != null) {
				client2server = getServiceManager().getService(ServiceType.netserver, sids.get(0));
			}
		}
		{
			List<String> sids = c.getDependIdsMap().get(ServiceType.member.name());
			if (sids != null) {
				memberService = getServiceManager().getService(ServiceType.member, sids.get(0));
			}
		}
	}

	@Override
	public <T extends INetClient> T createNetClient() {
		NettyTcpClient client = new NettyTcpClient(idCreater.incrementAndGet());
		client.setConfig(creatNetClientConfig());
		client.startup();

		clientMap.put(client.getId(), client);
		return (T) client;
	}

	@Override
	public <T extends INetClient> T createNetClient(String host, int port) {
		NettyTcpClient client = new NettyTcpClient(idCreater.incrementAndGet());
		// clone配置，修改host和端口，
		NettyNetConfig netConfig = creatNetClientConfig();
		netConfig = netConfig.clone();
		netConfig.setHost(host);
		netConfig.setPort(port);

		client.setConfig(netConfig);
		client.startup();

		clientMap.put(client.getId(), client);
		return (T) client;
	}

	private NettyNetConfig creatNetClientConfig() {
		ScNetclient c = getSConfig();

		INetHandlerConfig netHandlerConfig = getNetHandlerConfig();

		// 处理器配置
		MegProtocolType megProtocolType = MegProtocolType.valueOf(c.getMegProtocolType());
		List<MessageConfig> messageConfigs = netHandlerConfig.getMessageConfigs(megProtocolType);
		MessageConfigMgr messageConfigMgr = new MessageConfigMgr(messageConfigs);

		IPoolExecutor poolExecutor = null;
		if (threadPoolService != null) {
			poolExecutor = threadPoolService.getPoolExecutor();
		}
		// 网络配置
		ScNetclient ns = getSConfig();

		NettyNetConfig config = new NettyNetConfig();
		// 网络协议
		config.setHost(ns.getHost());
		config.setPort(ns.getPort());
		config.setNetProtocolType(NetProtocolType.valueOf(ns.getNetProtocolType()));
		config.setSubReactorThreadNum(ns.getSubReactorThread());
		config.setHandlerThreadNum(ns.getHandlerThread());
		config.setAllIdleTimeSeconds(ns.getAllIdleTimeSeconds());
		config.setReaderIdleTimeSeconds(ns.getReaderIdleTimeSeconds());
		config.setWriterIdleTimeSeconds(ns.getWriterIdleTimeSeconds());
		config.setMessageConfigMgr(messageConfigMgr);
		config.setCommandMessageFactory(getCommandMessageFactory());
		config.setMegProtocolType(megProtocolType);
		config.setListener(netHandlerConfig.getListener());

		ForwardService forwardService = new ForwardService();
		forwardService.setClient2server(client2server);
		forwardService.setFunctionService(functionService);
		forwardService.setMemberService(memberService);
		forwardService.setMessageRounter(netHandlerConfig.getMessageRounter());

		MessageTcpHandler messageHandler = new MessageTcpHandler();
		messageHandler.setPoolExecutor(poolExecutor);
		messageHandler.setMessageConfigMgr(messageConfigMgr);
		messageHandler.setForwardService(forwardService);

		SessionManager sessionManager = new SessionManager();
		sessionManager.setMessageConfigMgr(messageConfigMgr);
		sessionManager.setPlayerFactory(netHandlerConfig.getPlayerFactory());

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
