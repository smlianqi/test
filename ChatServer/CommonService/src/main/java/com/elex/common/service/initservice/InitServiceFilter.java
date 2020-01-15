package com.elex.common.service.initservice;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.elex.common.component.function.IFunctionService;
import com.elex.common.component.function.info.FunctionId;
import com.elex.common.component.function.info.FunctionInfo;
import com.elex.common.component.net.server.INetServerService;
import com.elex.common.component.rpc.IRpcService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.IService;
import com.elex.common.service.IServiceManager;
import com.elex.common.service.config.ConfigFunctioninfo;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.config.ProvidingFunction;
import com.elex.common.service.configloader.IServiceConfigLoader;
import com.elex.common.service.factory.IServiceFactory;
import com.elex.common.service.filter.AbstractFilter;
import com.elex.common.service.module.IModuleServiceConfig;
import com.elex.common.service.type.ServiceStateType;
import com.elex.common.service.type.ServiceType;

/**
 * 初始化服务
 * 
 * @author mausmars
 *
 */
public class InitServiceFilter extends AbstractFilter {
	public InitServiceFilter() {
		super("InitServiceFilter");
	}

	@Override
	public boolean doWork(Object context) throws Exception {
		InitContext c = (InitContext) context;

		IGlobalContext globalContext = c.getGlobalContext();

		IServiceFactory serviceFactory = c.getServiceFactory();
		IServiceConfigLoader serviceConfigLoader = serviceFactory.getServiceConfigLoader();

		// 读取运维配置
		ProvidingFunction providingFunction = serviceConfigLoader.loadOperationConfig(c.getFunctionType());
		// 初始化服务
		IFunctionServiceConfig functionServiceConfig = loadServiceConfig(providingFunction, serviceConfigLoader,
				serviceFactory);
		// 初始化回调接口
		IInitCallback initCallback = c.getInitCallback();

		if (initCallback != null) {
			initCallback.serverInitBefore(globalContext);
		}
		// 初始化模块服务
		IModuleServiceConfig moduleServiceConfig = c.getAttachByCls(IModuleServiceConfig.class);
		if (moduleServiceConfig != null) {
			moduleServiceConfig.init(globalContext);
		}
		if (initCallback != null) {
			initCallback.serverInitAfter(globalContext);
			initCallback.serverStartBefore(globalContext);
		}
		// 启动所有服务
		startServer(functionServiceConfig, serviceFactory);

		if (initCallback != null) {
			initCallback.serverStartAfter(globalContext);
		}
		// 注册服务
		registerServer(functionServiceConfig, serviceFactory);

		return false;
	}

	// 注册服务
	private void registerServer(IFunctionServiceConfig functionServiceConfig, IServiceFactory serviceFactory) {
		FunctionId functionId = new FunctionId(functionServiceConfig.getGroupId(), functionServiceConfig.getRegionId(),
				functionServiceConfig.getFunctionType());

		FunctionInfo functionInfo = new FunctionInfo();
		functionInfo.setFunctionId(functionId);

		List<IRpcService> rpcServiceList = serviceFactory.getServiceManager().getTypeService(ServiceType.rpc);
		for (IRpcService rs : rpcServiceList) {
			// 注册
			rs.registerService(functionInfo);
		}
		List<INetServerService> netServiceList = serviceFactory.getServiceManager()
				.getTypeService(ServiceType.netserver);
		for (INetServerService rs : netServiceList) {
			// 注册
			rs.registerService(functionInfo);
		}

		List<IFunctionService> functionServices = serviceFactory.getServiceManager()
				.getTypeService(ServiceType.function);
		if (!functionServices.isEmpty()) {
			IFunctionService functionService = functionServices.get(0);
			functionService.register(functionInfo);
		}
	}

	private IFunctionServiceConfig loadServiceConfig(ProvidingFunction pf, IServiceConfigLoader serviceConfigLoader,
			IServiceFactory serviceFactory) {
		// 获取功能配置信息
		IFunctionServiceConfig fsc = serviceConfigLoader.loadConfigFSC(pf);

		ConfigFunctioninfo configFunctioninfo = fsc.getConfig();
		// 初始化依赖的服务
		Map<String, List<String>> dependIdsMap = configFunctioninfo.getDependIdsMap();
		for (Entry<String, List<String>> entry : dependIdsMap.entrySet()) {
			for (String sid : entry.getValue()) {
				IServiceConfig serviceConfig = serviceConfigLoader.loadConfigSC(entry.getKey(), sid, fsc);
				// 创建服务
				serviceFactory.createService(serviceConfig);
			}
		}
		return fsc;
	}

	private void startServer(IFunctionServiceConfig fsc, IServiceFactory serviceFactory) {
		IServiceManager serviceManager = serviceFactory.getServiceManager();

		ConfigFunctioninfo configFunctioninfo = fsc.getConfig();
		Map<String, List<String>> dependIdsMap = configFunctioninfo.getDependIdsMap();
		for (Entry<String, List<String>> entry : dependIdsMap.entrySet()) {
			for (String sid : entry.getValue()) {
				IService service = serviceManager.getService(entry.getKey(), sid);
				service.startup();
			}
		}
	}

//	private void startupServer(IService service, IServiceManager serviceManager) {
//		IServiceConfig serviceConfig = service.getConfig();
//		Map<String, List<String>> dependIdsMap = serviceConfig.getDependIdsMap();
//		for (Entry<String, List<String>> entry : dependIdsMap.entrySet()) {
//			for (String sid : entry.getValue()) {
//				IService s = serviceManager.getService(entry.getKey(), sid);
//				startupServer(s, serviceManager);
//			}
//		}
//		service.startup();
//	}
}
