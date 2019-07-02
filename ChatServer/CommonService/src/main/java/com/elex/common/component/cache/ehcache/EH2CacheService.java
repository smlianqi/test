package com.elex.common.component.cache.ehcache;

import java.util.Map;

import com.elex.common.component.cache.ICacheService;
import com.elex.common.component.cache.config.ScCache;
import com.elex.common.component.cache.type.CacheInteractionType;
import com.elex.common.component.cache.type.CacheType;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.MemoryUnit;

/**
 * ehcache2.0
 * 
 * @author mausmars
 *
 */
public class EH2CacheService extends AbstractService<ScCache> implements ICacheService {
	private CacheManager cacheManager;

	public EH2CacheService(IServiceConfig sc, IGlobalContext context) {
		super(sc, context);
	}

	@Override
	public void initService() throws Exception {
		ScCache cr = getSConfig();

		Map<String, Object> extraParamsMap = cr.getExtraParamsMap();
		int heap = (int) ((double) extraParamsMap.get("heap"));

		Configuration config = new Configuration();
		CacheConfiguration defaultCacheConfiguration = new CacheConfiguration();
		defaultCacheConfiguration.setMaxEntriesLocalHeap(0);
		defaultCacheConfiguration.setEternal(true);
		// defaultCacheConfiguration.setTimeToIdleSeconds(30);
		// defaultCacheConfiguration.setTimeToLiveSeconds(30);
		// TODO
		defaultCacheConfiguration.maxBytesLocalHeap(heap, MemoryUnit.MEGABYTES);
		// defaultCacheConfiguration.maxEntriesLocalDisk(10000000);
		config.addDefaultCache(defaultCacheConfiguration);// 设置默认cache
		cacheManager = CacheManager.create(config);
	}

	@Override
	public void startupService() throws Exception {

	}

	@Override
	public void shutdownService() throws Exception {
		cacheManager.shutdown();
	}

	@Override
	public <T> T createCache(Object param) {
		CacheConfiguration cacheConfig = (CacheConfiguration) param;

		Ehcache cache = new Cache(cacheConfig);
		Ehcache oldCache = cacheManager.addCacheIfAbsent(cache);
		if (oldCache == null) {
			oldCache = cache;
		}
		return (T) oldCache;
	}

	@Override
	public void removeCache(String cacheName) {
		cacheManager.removeCache(cacheName);
	}

	@Override
	public void flushAll() {
		cacheManager.clearAll();
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
