package com.elex.common.component.cache;

import com.elex.common.component.cache.config.ScCache;
import com.elex.common.component.cache.ehcache.EH2CacheService;
import com.elex.common.component.cache.ehcache.EH3CacheService;
import com.elex.common.component.cache.redis.RedisService;
import com.elex.common.component.cache.type.CacheInteractionType;
import com.elex.common.component.cache.type.CacheType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceStateType;

public class CacheServiceProxy implements ICacheService {
	private ICacheService service;

	public CacheServiceProxy(IServiceConfig serviceConfig, IGlobalContext context) {
		ScCache cache = serviceConfig.getConfig();

		CacheType cacheType = CacheType.valueOf(cache.getCacheType());

		switch (cacheType) {
		case redis:
			service = new RedisService(serviceConfig, context);
			break;
		case ehcache3:
			service = new EH3CacheService(serviceConfig, context);
			break;
		case ehcache2:
			service = new EH2CacheService(serviceConfig, context);
			break;
		default:
			// 类型错误
			throw new RuntimeException();
		}
	}

	@Override
	public IGlobalContext getGlobalContext() {
		return service.getGlobalContext();
	}

	@Override
	public CacheInteractionType getCacheInteractionType() {
		return service.getCacheInteractionType();
	}

	@Override
	public CacheType getCacheType() {
		return service.getCacheType();
	}

	@Override
	public <T> T createCache(Object param) {
		return service.createCache(param);
	}

	@Override
	public void removeCache(String cacheName) {
		service.removeCache(cacheName);
	}

	@Override
	public void flushAll() {
		service.flushAll();
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
