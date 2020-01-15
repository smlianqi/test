package com.elex.common.component.net.server;

import com.elex.common.component.function.info.FunctionInfo;
import com.elex.common.component.net.config.ScNetserver;
import com.elex.common.net.session.ISessionManager;
import com.elex.common.net.type.NetProtocolType;
import com.elex.common.net.type.NetServiceType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceStateType;

public class NetServerServiceProxy implements INettyNetServerService {
	private INetServerService service;

	public NetServerServiceProxy(IServiceConfig serviceConfig, IGlobalContext context, ICustomConfig customConfig) {
		ScNetserver netserver = serviceConfig.getConfig();

		NetServiceType netServerServiceType = NetServiceType.valueOf(netserver.getNetServiceType());
		NetProtocolType netServerProtocolType = NetProtocolType.valueOf(netserver.getNetProtocolType());

		if (netServerServiceType == NetServiceType.netty) {
			if (netServerProtocolType == NetProtocolType.tcp) {
				NettyTcpNetServerService netService = new NettyTcpNetServerService(serviceConfig, context,
						customConfig);
				service = netService;
			} else if (netServerProtocolType == NetProtocolType.udp) {
				// TODO
			} else if (netServerProtocolType == NetProtocolType.udt) {
				// TODO
			} else if (netServerProtocolType == NetProtocolType.http
					|| netServerProtocolType == NetProtocolType.https) {
				NettyHttpNetServerService netService = new NettyHttpNetServerService(serviceConfig, context,
						customConfig);
				service = netService;
			} else if (netServerProtocolType == NetProtocolType.ws || netServerProtocolType == NetProtocolType.wss) {
				NettyWebSocketNetServerService netService = new NettyWebSocketNetServerService(serviceConfig, context,
						customConfig);
				service = netService;
			}
		} else if (netServerServiceType == NetServiceType.jetty) {
			// TODO
		}
	}

	@Override
	public IGlobalContext getGlobalContext() {
		return service.getGlobalContext();
	}

	@Override
	public void registerService(FunctionInfo functionInfo) {
		service.registerService(functionInfo);
	}

	@Override
	public <T extends IServiceConfig> T getConfig() {
		return service.getConfig();
	}

	@Override
	public IFunctionServiceConfig getFunctionServiceConfig() {
		return service.getFunctionServiceConfig();
	}

	@Override
	public void init() {
		service.init();
	}

	@Override
	public void startup() {
		service.startup();
	}

	@Override
	public void shutdown() {
		service.shutdown();
	}

	@Override
	public ServiceStateType getServiceStateType() {
		return service.getServiceStateType();
	}

	@Override
	public ISessionManager getSessionManager() {
		if (service instanceof INettyNetServerService) {
			return ((INettyNetServerService) service).getSessionManager();
		}
		return null;
	}
}
