package com.elex.common.component.cache.ehcache;

import java.util.Map;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import com.elex.common.component.cache.ICacheService;
import com.elex.common.component.cache.config.ScCache;
import com.elex.common.component.cache.type.CacheInteractionType;
import com.elex.common.component.cache.type.CacheType;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;

/**
 * ehcache3.0
 * 
 * @author mausmars
 *
 */
public class EH3CacheService extends AbstractService<ScCache> implements ICacheService {
	private CacheManager cacheManager;

	private static final String CacheName = "LocalCache";

	public EH3CacheService(IServiceConfig sc, IGlobalContext context) {
		super(sc, context);
	}

	@Override
	public void initService() throws Exception {
		ScCache cr = getSConfig();

		Map<String, Object> extraParamsMap = cr.getExtraParamsMap();
		int heap = (int) ((double) extraParamsMap.get("heap"));

		this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache(CacheName, CacheConfigurationBuilder
				.newCacheConfigurationBuilder(String.class, Object.class, ResourcePoolsBuilder.heap(heap)).build())
				.build(true);
	}

	@Override
	public void startupService() throws Exception {

	}

	@Override
	public void shutdownService() throws Exception {
		cacheManager.close();
	}

	@Override
	public <T> T createCache(Object param) {
		Cache<String, Object> cache = cacheManager.getCache(CacheName, String.class, Object.class);
		return (T) cache;
	}

	@Override
	public void removeCache(String cacheName) {
		cacheManager.removeCache(cacheName);
	}

	@Override
	public void flushAll() {
		cacheManager.removeCache(CacheName);
	}

	@Override
	public CacheInteractionType getCacheInteractionType() {
		return CacheInteractionType.Local;
	}

	@Override
	public CacheType getCacheType() {
		return CacheType.ehcache3;
	}
}
