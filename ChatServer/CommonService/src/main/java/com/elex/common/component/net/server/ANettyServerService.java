package com.elex.common.component.net.server;

import java.util.List;

import com.elex.common.component.function.info.FunctionInfo;
import com.elex.common.component.net.INetCreateSessionHandler;
import com.elex.common.component.net.config.ChannelOption;
import com.elex.common.component.net.config.NetCustomConfig;
import com.elex.common.component.net.config.ScNetserver;
import com.elex.common.component.net.handlerconfig.DefaultNetHandlerConfig;
import com.elex.common.component.net.handlerconfig.INetHandlerConfig;
import com.elex.common.component.threadpool.IThreadPoolService;
import com.elex.common.component.timeout.ITimeoutManager;
import com.elex.common.component.timeout.ITimeoutService;
import com.elex.common.net.INetServer;
import com.elex.common.net.handler.ICreateSessionHandler;
import com.elex.common.net.message.protocol.ICommandMessageFactory;
import com.elex.common.net.service.netty.ChannelOptionConfig;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.net.session.ISessionManager;
import com.elex.common.service.AbstractService;
import com.elex.common.service.GeneralConstant;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceType;
import com.elex.common.util.netaddress.NetUtil;

public abstract class ANettyServerService extends AbstractService<ScNetserver> implements INettyNetServerService {
	protected INetServer server;
	// 线程池服务
	protected IThreadPoolService threadPoolService;
	// 超时服务
	protected ITimeoutService timeoutService;
	// session管理器
	protected ISessionManager sessionManager;

	public ANettyServerService(IServiceConfig serviceConfig, IGlobalContext globalContext, ICustomConfig customConfig) {
		super(serviceConfig, globalContext);
		this.customConfig = customConfig;
	}

	@Override
	public void initService() throws Exception {
		ScNetserver c = getSConfig();

		List<String> threadpoolSids = c.getDependIdsMap().get(ServiceType.threadpool.name());
		if (threadpoolSids != null) {
			threadPoolService = getServiceManager().getService(ServiceType.threadpool, threadpoolSids.get(0));
		}

		List<String> timeoutSids = c.getDependIdsMap().get(ServiceType.timeout.name());
		if (timeoutSids != null) {
			timeoutService = getServiceManager().getService(ServiceType.timeout, timeoutSids.get(0));
		}
		server = craeteNetServer();
	}

	@Override
	public void startupService() throws Exception {

		String netIp = null;
		//优先外网地址
		netIp = NetUtil.getOutsideNetIp(getSConfig().getHost());
		//外网地址不存在取内网地址
		if (netIp == null){
			netIp = NetUtil.getInsideNetIp(getSConfig().getHost());
		}
		//取回环地址
		if (netIp == null){
			netIp = getSConfig().getHost();
		}

		// 启动的时候检查端口占用，如果占用就加1
		int port = NetUtil.getAvailablePort(netIp, getSConfig().getPort());
		getSConfig().setPort(port);
		getSConfig().setHost(netIp);
		if (getSConfig().getOutsideHost() == null) {
			getSConfig().setOutsideHost(netIp);
		}

		NettyNetConfig nettyNetConfig = creatNetServerConfig(getSConfig().getPort());
		server.setConfig(nettyNetConfig);

		if (threadPoolService != null) {
			threadPoolService.startup();
		}

		if (timeoutService != null) {
			timeoutService.startup();
			List<ICreateSessionHandler> createSessionHandlers = getNetHandlerConfig().getCreateSessionHandlers();
			ITimeoutManager timeoutManager = timeoutService.createTask(createTimeoutManagerName());

			if (createSessionHandlers != null && !createSessionHandlers.isEmpty()) {
				for (ICreateSessionHandler createSessionHandler : createSessionHandlers) {
					if (createSessionHandler instanceof INetCreateSessionHandler) {
						INetCreateSessionHandler csh = (INetCreateSessionHandler) createSessionHandler;
						csh.init(timeoutManager);
					}
				}
			}
		}
		// 启动
		server.startup();
	}

	@Override
	public void shutdownService() throws Exception {
		// 移除任务
		if (timeoutService != null) {
			timeoutService.removeTask(createTimeoutManagerName());
		}
		server.shutdown();
	}

	@Override
	public void registerService(FunctionInfo functionInfo) {
		// 暂时不支持webosocket,http
	}

	protected abstract INetServer craeteNetServer();

	protected abstract NettyNetConfig creatNetServerConfig(int port);

	@Override
	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	protected ChannelOptionConfig createChannelOptionConfig() {
		// 优化配置
		ChannelOption channelOption = (ChannelOption) getAttach(GeneralConstant.ChannelOptionKey);
		if (channelOption == null) {
			return null;
		}
		ChannelOptionConfig coc = new ChannelOptionConfig();
		coc.setBacklog(channelOption.getBacklog());
		coc.setKeepalive(channelOption.getKeepalive());
		coc.setNodelay(channelOption.getNodelay());
		coc.setReuseaddr(channelOption.getReuseaddr());

		coc.setTimeout(channelOption.getTimeout());
		coc.setLinger(channelOption.getLinger());
		coc.setRcvbuf(channelOption.getRcvbuf());
		coc.setSndbuf(channelOption.getSndbuf());
		coc.setConnectTimeoutMillis(channelOption.getConnectTimeout());
		return coc;
	}

	protected INetHandlerConfig getNetHandlerConfig() {
		NetCustomConfig cc = (NetCustomConfig) customConfig;

		INetHandlerConfig c = cc.getNetHandlerConfig(serviceConfig.getServiceId());
		if (c == null) {
			c = new DefaultNetHandlerConfig();
			cc.putNetHandlerConfig(serviceConfig.getServiceId(), c);
		}
		return c;
	}

	protected String createTimeoutManagerName() {
		ScNetserver c = getSConfig();
		return ServiceType.netserver + "_" + c.getId();
	}

	protected ICommandMessageFactory getCommandMessageFactory() {
		NetCustomConfig cc = (NetCustomConfig) customConfig;
		return cc.getCommandMessageFactory();
	}
}
