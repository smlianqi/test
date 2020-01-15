package com.elex.common.component.threadpool;

import com.elex.common.component.threadpool.config.ScThreadpool;
import com.elex.common.component.threadpool.type.ThreadpoolType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceStateType;

public class ThreadPoolServiceProxy implements IThreadPoolService {
	private IThreadPoolService service;

	public ThreadPoolServiceProxy(IServiceConfig serviceConfig, IGlobalContext context) {
		ScThreadpool threadpool = serviceConfig.getConfig();

		ThreadpoolType threadpoolType = ThreadpoolType.valueOf(threadpool.getThreadpoolType());
		if (threadpoolType == ThreadpoolType.SelfControl) {
			// 创建网络锁服务
			service = new SelfControlThreadPoolService(serviceConfig, context);
		} else {
			// 类型错误
			throw new RuntimeException();
		}
	}

	@Override
	public IGlobalContext getGlobalContext() {
		return service.getGlobalContext();
	}

	@Override
	public IPoolExecutor getPoolExecutor() {
		return service.getPoolExecutor();
	}

	@Override
	public ThreadpoolType getThreadpoolType() {
		return service.getThreadpoolType();
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
}
