package com.elex.common.service.factory;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.elex.common.component.cache.CacheServiceProxy;
import com.elex.common.component.cache.config.ScCache;
import com.elex.common.component.data.DataService;
import com.elex.common.component.data.config.ScData;
import com.elex.common.component.database.DatabaseService;
import com.elex.common.component.database.config.ScDatabase;
import com.elex.common.component.event.EventService;
import com.elex.common.component.event.config.ScEvent;
import com.elex.common.component.function.FunctionService;
import com.elex.common.component.function.config.ScFunction;
import com.elex.common.component.ignite.IgniteService;
import com.elex.common.component.ignite.config.ScIgnite;
import com.elex.common.component.lock.LockServiceProxy;
import com.elex.common.component.lock.config.ScLock;
import com.elex.common.component.member.MemberService;
import com.elex.common.component.member.config.ScMember;
import com.elex.common.component.net.client.NetClientServiceProxy;
import com.elex.common.component.net.config.ScNetclient;
import com.elex.common.component.net.config.ScNetserver;
import com.elex.common.component.net.server.NetServerServiceProxy;
import com.elex.common.component.prototype.PrototypeService;
import com.elex.common.component.prototype.config.ScPrototype;
import com.elex.common.component.rpc.RpcConstant;
import com.elex.common.component.rpc.RpcServiceProxy;
import com.elex.common.component.rpc.config.ScRpc;
import com.elex.common.component.rpc.ice.IceRpcServicePrx;
import com.elex.common.component.rpc.type.RpcNetServiceType;
import com.elex.common.component.script.ScriptService;
import com.elex.common.component.script.config.ScScript;
import com.elex.common.component.task.QuartzTaskService;
import com.elex.common.component.task.config.ScTask;
import com.elex.common.component.threadbox.ThreadBoxService;
import com.elex.common.component.threadpool.ThreadPoolServiceProxy;
import com.elex.common.component.timeout.TimeoutService;
import com.elex.common.component.timeout.config.ScTimeout;
import com.elex.common.component.zookeeper.ZookeeperService;
import com.elex.common.component.zookeeper.config.ScZookeeper;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.IService;
import com.elex.common.service.IServiceManager;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.configloader.IServiceConfigLoader;
import com.elex.common.service.type.ServiceType;

/**
 * 服务工厂
 * 
 * @author mausmars
 *
 */
public class ServiceFactory implements IServiceFactory {
	// 服务配置加载器
	private IServiceConfigLoader serviceConfigLoader;
	// 服务管理器
	private IGlobalContext context;

	public ServiceFactory(IGlobalContext context, IServiceConfigLoader serviceConfigLoader) {
		this.context = context;
		this.serviceConfigLoader = serviceConfigLoader;
	}

	@Override
	public IService createService(IServiceConfig config) {
		ServiceType serviceType = config.getServiceType();

		IService service = context.getServiceManager().getService(serviceType, config.getServiceId());
		if (service != null) {
			return service;
		}
		switch (serviceType) {
		case cache: {
			service = new CacheServiceProxy(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScCache c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case zookeeper: {
			service = new ZookeeperService(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScZookeeper c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case database: {
			// c3p0, mybatis
			service = new DatabaseService(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScDatabase c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case threadpool: {
			service = new ThreadPoolServiceProxy(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);
			break;
		}
		case threadbox: {
			service = new ThreadBoxService(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);
			break;
		}
		case objectpool: {

			break;
		}
		case timeout: {
			service = new TimeoutService(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScTimeout c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case netclient: {
			ICustomConfig customConfig = serviceConfigLoader.getCustomConfig(serviceType);
			service = new NetClientServiceProxy(config, context, customConfig);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScNetclient c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case netserver: {
			ICustomConfig customConfig = serviceConfigLoader.getCustomConfig(serviceType);
			service = new NetServerServiceProxy(config, context, customConfig);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScNetserver c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case rpc: {
			ICustomConfig customConfig = serviceConfigLoader.getCustomConfig(serviceType);
			service = new RpcServiceProxy(config, context, customConfig);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScRpc c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case function: {
			service = new FunctionService(config, context);
			// fs.setRpcServiceFactoryMap(rpcServiceFactoryMap);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScFunction c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);

			// 如果有function类型，创建代理prx服务，暂时只有ice
			createRpcPrxService(RpcConstant.RpcServicePrxId, RpcNetServiceType.ice, config);
			break;
		}
		case data: {
			service = new DataService(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScData c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case member: {
			service = new MemberService(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScMember c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case lock: {
			service = new LockServiceProxy(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScLock c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case task: {
			service = new QuartzTaskService(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScTask c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case prototype: {
			service = new PrototypeService(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScPrototype c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case script: {
			service = new ScriptService(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScScript c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case ignite: {
			ICustomConfig customConfig = serviceConfigLoader.getCustomConfig(serviceType);
			service = new IgniteService(config, context, customConfig);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScIgnite c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		case event: {
			service = new EventService(config, context);
			// 这里先插入
			context.getServiceManager().insertService(service);

			ScEvent c = config.getConfig();
			// 处理依赖服务
			dependService(c.getDependIdsMap(), config);
			break;
		}
		default:
			break;
		}
		// 服务初始化
		service.init();
		return service;
	}

	private void createRpcPrxService(String serviceId, RpcNetServiceType rpcNetServiceType, IServiceConfig c) {
		IService service = context.getServiceManager().getService(ServiceType.rpcprx, serviceId);
		if (service != null) {
			return;
		}

		IServiceConfig config = new IServiceConfig() {
			@Override
			public String getServiceId() {
				return serviceId;
			}

			@Override
			public ServiceType getServiceType() {
				return ServiceType.rpcprx;
			}

			@Override
			public IFunctionServiceConfig getFunctionServiceConfig() {
				return c.getFunctionServiceConfig();
			}

			@Override
			public <T> T getConfig() {
				return null;
			}

			@Override
			public Object getAttach(Object key) {
				return null;
			}

			@Override
			public Map<String, List<String>> getDependIdsMap() {
				return null;
			}
		};

		if (rpcNetServiceType == RpcNetServiceType.ice) {
			IceRpcServicePrx iceRpcServicePrx = new IceRpcServicePrx(config, context);
			service = iceRpcServicePrx;
		}

		// 服务初始化
		service.init();

		// 加入到管理其中
		context.getServiceManager().insertService(service);
	}

	// 处理依赖服务
	private void dependService(Map<String, List<String>> dependIds, IServiceConfig config) {
		for (Entry<String, List<String>> entry : dependIds.entrySet()) {
			String serviceType = entry.getKey();
			List<String> serviceIds = entry.getValue();

			for (String serviceId : serviceIds) {
				IServiceConfig serviceConfig = serviceConfigLoader.loadConfigSC(serviceType, serviceId,
						config.getFunctionServiceConfig());
				if (!context.getServiceManager().isContain(serviceType, serviceId)) {
					// 如果服务不存在就创建
					createService(serviceConfig);
				}
			}
		}
	}

	@Override
	public IService createService(ServiceType serviceType, String serviceId, IFunctionServiceConfig fsc) {
		IService service = context.getServiceManager().getService(serviceType, serviceId);
		if (service != null) {
			return service;
		}
		// 通过配置加载
		IServiceConfig serviceConfig = serviceConfigLoader.loadConfigSC(serviceType, serviceId, fsc);
		if (serviceConfig == null) {
			return null;
		}
		return createService(serviceConfig);
	}

	@Override
	public IServiceConfigLoader getServiceConfigLoader() {
		return serviceConfigLoader;
	}

	@Override
	public IServiceManager getServiceManager() {
		return context.getServiceManager();
	}
}
