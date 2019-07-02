package com.elex.common.component.net.client;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.elex.common.component.net.config.ChannelOption;
import com.elex.common.component.net.config.NetCustomConfig;
import com.elex.common.component.net.config.ScNetclient;
import com.elex.common.component.net.handlerconfig.DefaultNetHandlerConfig;
import com.elex.common.component.net.handlerconfig.INetHandlerConfig;
import com.elex.common.component.threadpool.IThreadPoolService;
import com.elex.common.net.INetClient;
import com.elex.common.net.message.protocol.ICommandMessageFactory;
import com.elex.common.net.service.netty.ChannelOptionConfig;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.service.AbstractService;
import com.elex.common.service.GeneralConstant;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceType;

public abstract class ANettyClientService extends AbstractService<ScNetclient> implements INetClientService {
	/** 网络链接管理 */
	protected ConcurrentMap<Integer, INetClient> clientMap = new ConcurrentHashMap<Integer, INetClient>();
	/** id创建者 */
	protected AtomicInteger idCreater = new AtomicInteger(0);

	/** 线程池服务 */
	protected IThreadPoolService threadPoolService;

	public ANettyClientService(IServiceConfig serviceConfig, IGlobalContext globalContext, ICustomConfig customConfig) {
		super(serviceConfig, globalContext);
		this.customConfig = customConfig;
	}

	@Override
	public void initService() throws Exception {
		ScNetclient c = getSConfig();

		List<String> threadpoolSids = c.getDependIdsMap().get(ServiceType.threadpool.name());
		if (threadpoolSids != null) {
			threadPoolService = getServiceManager().getService(ServiceType.threadpool, threadpoolSids.get(0));
		}
	}

	@Override
	public void startupService() throws Exception {
		if (threadPoolService != null) {
			threadPoolService.startup();
		}
	}

	@Override
	public void shutdownService() throws Exception {
		// 关闭客户端
		for (INetClient netClient : clientMap.values()) {
			netClient.shutdown();
		}
		clientMap = null;
	}

	@Override
	public void removeNetClient(int clientId) {
		INetClient netClient = clientMap.remove(clientId);
		if (netClient != null) {
			// 连接停止
			netClient.shutdown();
		}
	}

	@Override
	public void removeNetClient(ISession session) {
		Integer clientId = session.getAttach(SessionAttachType.ClientServiceId);
		if (clientId != null) {
			removeNetClient(clientId);
		}
	}

	protected ICommandMessageFactory getCommandMessageFactory() {
		NetCustomConfig cc = (NetCustomConfig) customConfig;
		return cc.getCommandMessageFactory();
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

}
