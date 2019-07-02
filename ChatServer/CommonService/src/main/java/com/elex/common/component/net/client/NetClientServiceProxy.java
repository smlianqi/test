package com.elex.common.component.net.client;

import com.elex.common.component.net.config.ScNetclient;
import com.elex.common.net.INetClient;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.NetProtocolType;
import com.elex.common.net.type.NetServiceType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceStateType;

public class NetClientServiceProxy implements INetClientService {
	private INetClientService service;

	public NetClientServiceProxy(IServiceConfig serviceConfig, IGlobalContext context, ICustomConfig customConfig) {
		ScNetclient netclient = serviceConfig.getConfig();

		NetServiceType netClientServiceType = NetServiceType.valueOf(netclient.getNetServiceType());
		NetProtocolType netClientProtocolType = NetProtocolType.valueOf(netclient.getNetProtocolType());

		if (netClientServiceType == NetServiceType.netty) {
			if (netClientProtocolType == NetProtocolType.tcp) {
				NettyTcpNetClientService netService = new NettyTcpNetClientService(serviceConfig, context,
						customConfig);
				service = netService;
			} else if (netClientProtocolType == NetProtocolType.udp) {

			} else if (netClientProtocolType == NetProtocolType.http
					|| netClientProtocolType == NetProtocolType.https) {

			} else if (netClientProtocolType == NetProtocolType.ws || netClientProtocolType == NetProtocolType.wss) {
				NettyWebSocketNetClientService netService = new NettyWebSocketNetClientService(serviceConfig, context,
						customConfig);
				service = netService;
			}
		}
	}

	@Override
	public IGlobalContext getGlobalContext() {
		return service.getGlobalContext();
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
	public <T extends INetClient> T createNetClient() {
		return service.createNetClient();
	}

	@Override
	public <T extends INetClient> T createNetClient(String host, int port) {
		return service.createNetClient(host, port);
	}

	@Override
	public void removeNetClient(int clientId) {
		service.removeNetClient(clientId);
	}

	@Override
	public void removeNetClient(ISession session) {
		service.removeNetClient(session);
	}
}
