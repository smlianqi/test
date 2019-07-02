package com.elex.common.component.lock;

import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.core.RLock;

import com.elex.common.component.lock.config.ScLock;
import com.elex.common.component.lock.type.LockType;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;

/**
 * 网络锁服务
 * 
 * @author mausmarsOs
 *
 */
public class NetLockService extends AbstractService<ScLock> implements ILockService {
	private final static String RedisAddressKey = "redis_address";

	private Redisson redisson;

	public NetLockService(IServiceConfig sc, IGlobalContext context) {
		super(sc, context);
	}

	@Override
	public void initService() throws Exception {
		String redisAddress = getSConfig().getExtraParamsMap().get(RedisAddressKey);

		Config c = new Config();
		c.useSingleServer().setAddress(redisAddress);
		redisson = Redisson.create(c);
	}

	@Override
	public void startupService() throws Exception {
	}

	@Override
	public void shutdownService() throws Exception {
		redisson.shutdown();
	}

	@Override
	public LockType getLockType() {
		return LockType.NetLock;
	}

	@Override
	public ILock getLock(Object key) {
		RLock lock = redisson.getLock((String) key);
		return new NetLock(lock);
	}

	@Override
	public int getLockOutTime() {
		// TODO
		return 0;
	}
}
