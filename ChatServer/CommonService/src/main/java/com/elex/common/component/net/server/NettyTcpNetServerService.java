package com.elex.common.component.net.server;

import com.elex.common.component.function.IFunctionService;
import com.elex.common.component.function.info.FService;
import com.elex.common.component.function.info.FunctionInfo;
import com.elex.common.component.function.info.NettyForwardServiceInfo;
import com.elex.common.component.function.info.ServiceAddress;
import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.member.IMemberService;
import com.elex.common.component.net.config.NetCustomConfig;
import com.elex.common.component.net.config.ScNetserver;
import com.elex.common.component.net.handlerconfig.INetHandlerConfig;
import com.elex.common.component.threadpool.IPoolExecutor;
import com.elex.common.net.INetServer;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.message.MessageConfigMgr;
import com.elex.common.net.rounter.ForwardService;
import com.elex.common.net.service.netty.ChannelOptionConfig;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.net.service.netty.NettyTcpServer;
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

import java.util.List;

public class NettyTcpNetServerService extends ANettyServerService {
	private IFunctionService functionService;
	private IMemberService memberService;

	public NettyTcpNetServerService(IServiceConfig serviceConfig, IGlobalContext globalContext,
			ICustomConfig customConfig) {
		super(serviceConfig, globalContext, customConfig);
	}

	@Override
	public void registerService(FunctionInfo functionInfo) {
		ScNetserver c = getSConfig();
		if (!c.getIsRegister()) {
			// 不注册
			return;
		}
		if (c.getCheckToken() == null) {
//			c.setCheckToken(UUIDUtil.getUUID());
			c.setCheckToken("XXX");
		}

		ServiceAddress serviceAddress = new ServiceAddress(c.getHost(), c.getPort(),
				NetProtocolType.valueOf(c.getNetProtocolType()));

		MegProtocolType megProtocolType = MegProtocolType.valueOf(c.getMegProtocolType());
		NettyForwardServiceInfo serviceInfo = new NettyForwardServiceInfo(c.getConnectCount(), c.getCheckToken());

		FService service = new FService(FServiceType.NettyForward, serviceAddress);
		// TODO 这个key暂时是NettyForward
		service.putServiceInfo(FServiceType.NettyForward.name(), serviceInfo);
		// 加入功能信息中
		functionInfo.putFService(service);
	}

	@Override
	public void initService() throws Exception {
		ScNetserver c = getSConfig();
		{
			List<String> sids = c.getDependIdsMap().get(ServiceType.function.name());
			if (sids != null) {
				functionService = getServiceManager().getService(ServiceType.function, sids.get(0));
			}
		}
		{
			List<String> sids = c.getDependIdsMap().get(ServiceType.member.name());
			if (sids != null) {
				memberService = getServiceManager().getService(ServiceType.member, sids.get(0));
			}
		}
		super.initService();
	}

	@Override
	protected INetServer craeteNetServer() {
		return new NettyTcpServer();
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

		ForwardService forwardService = new ForwardService();
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

		// 网络配置
		ScNetserver ns = getSConfig();
		NettyNetConfig config = new NettyNetConfig();
		config.setPort(port);
		config.setSubReactorThreadNum(ns.getSubReactorThread());
		config.setHandlerThreadNum(ns.getHandlerThread());
		config.setAllIdleTimeSeconds(ns.getAllIdleTimeSeconds());
		config.setReaderIdleTimeSeconds(ns.getReaderIdleTimeSeconds());
		config.setWriterIdleTimeSeconds(ns.getWriterIdleTimeSeconds());
		config.setHandler(gameHandler);
		config.setMessageConfigMgr(messageConfigMgr);
		config.setCommandMessageFactory(getCommandMessageFactory());
		config.setMegProtocolType(megProtocolType);
		config.setSessionFactory(sessionFactory);

		// 优化配置
		ChannelOptionConfig coc = createChannelOptionConfig();
		config.setChannelOptionConfig(coc);
		return config;
	}
}
