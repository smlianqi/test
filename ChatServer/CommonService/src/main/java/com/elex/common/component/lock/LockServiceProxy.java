package com.elex.common.component.lock;

import com.elex.common.component.lock.config.ScLock;
import com.elex.common.component.lock.type.LockType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceStateType;

/**
 * 代理
 * 
 * @author mausmars
 *
 */
public class LockServiceProxy implements ILockService {
	private ILockService service;

	public LockServiceProxy(IServiceConfig serviceConfig, IGlobalContext context) {
		ScLock lock = serviceConfig.getConfig();

		LockType lockType = LockType.valueOf(lock.getLockType());
		if (lockType == LockType.NetLock) {
			// 创建网络锁服务
			service = new NetLockService(serviceConfig, context);
		} else if (lockType == LockType.LocalLock) {
			service = new LocalLockService(serviceConfig, context);
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
	public ILock getLock(Object key) {
		return service.getLock(key);
	}

	@Override
	public int getLockOutTime() {
		return service.getLockOutTime();
	}

	@Override
	public LockType getLockType() {
		return service.getLockType();
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
