package com.elex.common.component.lock;

import java.util.concurrent.ConcurrentHashMap;

import com.elex.common.component.lock.config.ScLock;
import com.elex.common.component.lock.type.LockType;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;

/**
 * 本地锁服务
 * 
 * @author mausmars
 *
 */
public class LocalLockService extends AbstractService<ScLock> implements ILockService {
	private ConcurrentHashMap<Object, ILock> lockMap;

	public LocalLockService(IServiceConfig sc, IGlobalContext context) {
		super(sc, context);
		this.lockMap = new ConcurrentHashMap<Object, ILock>();
	}

	@Override
	public void initService() throws Exception {
	}

	@Override
	public void startupService() throws Exception {

	}

	@Override
	public void shutdownService() throws Exception {

	}

	@Override
	public ILock getLock(Object key) {
		ILock lock = lockMap.get(key);
		if (lock != null) {
			return lock;
		}
		// 乐观
		LocalLock localLock = new LocalLock();
		return lockMap.putIfAbsent(key, localLock);
	}

	@Override
	public int getLockOutTime() {
		return 0;
	}

	@Override
	public LockType getLockType() {
		return LockType.LocalLock;
	}
}
