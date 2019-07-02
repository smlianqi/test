package com.elex.common.component.rpc;

import com.elex.common.component.function.info.FunctionInfo;
import com.elex.common.component.rpc.config.ScRpc;
import com.elex.common.component.rpc.ice.IceRpcService;
import com.elex.common.component.rpc.type.RpcNetServiceType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceStateType;

public class RpcServiceProxy implements IRpcService {
	private IRpcService service;

	public RpcServiceProxy(IServiceConfig serviceConfig, IGlobalContext context, ICustomConfig customConfig) {

		ScRpc rpc = serviceConfig.getConfig();
		RpcNetServiceType rpcNetServiceType = RpcNetServiceType.valueOf(rpc.getNetServiceType());

		if (rpcNetServiceType == RpcNetServiceType.ice) {
			IceRpcService iceRpcService = new IceRpcService(serviceConfig, context, customConfig);
			// 设置ice 对应类
			service = iceRpcService;
		} else if (rpcNetServiceType == RpcNetServiceType.netty) {

		} else if (rpcNetServiceType == RpcNetServiceType.hessian) {

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
	public void registerService(FunctionInfo functionInfo) {
		service.registerService(functionInfo);
	}
}
