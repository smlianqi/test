package com.elex.common.component.cache.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.elex.common.component.cache.ICacheService;
import com.elex.common.component.cache.config.ScCache;
import com.elex.common.component.cache.type.CacheInteractionType;
import com.elex.common.component.cache.type.CacheType;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * redis服务
 * 
 * @author mausmars
 *
 */
public class RedisService extends AbstractService<ScCache> implements ICacheService {
	private JedisPoolConfig jedisPoolConfig;
	private List<JedisShardInfo> shards;
	private ShardedJedisPool shardedJedisPool;

	public RedisService(IServiceConfig sc, IGlobalContext context) {
		super(sc, context);
	}

	@Override
	public void initService() throws Exception {
		ScCache cr = getSConfig();

		Map<String, Object> extraParamsMap = cr.getExtraParamsMap();
		int maxIdle = (int) ((double) extraParamsMap.get("max_idle"));
		int maxTotal = (int) ((double) extraParamsMap.get("max_total"));
		int maxWaitMillis = (int) ((double) extraParamsMap.get("max_wait_millis"));
		boolean testOnReturn = (boolean) extraParamsMap.get("test_on_return");
		boolean testOnBorrow = (boolean) extraParamsMap.get("test_on_borrow");
		List<List<Object>> cacheInfos = (List<List<Object>>) extraParamsMap.get("cache_infos");

		jedisPoolConfig = new JedisPoolConfig();
		shards = new ArrayList<JedisShardInfo>();
		for (List<Object> cacheInfo : cacheInfos) {
			JedisShardInfo jedisShardInfo = new JedisShardInfo((String) cacheInfo.get(0),
					(int) ((double) cacheInfo.get(1)));
			jedisShardInfo.setSoTimeout((int) ((double) cacheInfo.get(2)));
			jedisShardInfo.setConnectionTimeout((int) ((double) cacheInfo.get(3)));
			shards.add(jedisShardInfo);

		}
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

		jedisPoolConfig.setTestOnReturn(testOnReturn);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
	}

	@Override
	public void startupService() throws Exception {
		shardedJedisPool = new ShardedJedisPool(jedisPoolConfig, shards);
	}

	@Override
	public void shutdownService() throws Exception {
		// 销毁连接池
		shardedJedisPool.destroy();
	}

	@Override
	public <T> T createCache(Object param) {
		// 检查可用性
		checkServiceUseState();
		return (T) shardedJedisPool.getResource();
	}

	@Override
	public void removeCache(String cacheName) {
	}

	@Override
	public void flushAll() {
		// 检查可用性
		checkServiceUseState();

		ShardedJedis jedis = null;
		try {
			jedis = shardedJedisPool.getResource();
			Collection<Jedis> jedises = jedis.getAllShards();
			if (logger.isInfoEnabled()) {
				logger.info("RedisClient flushAll start!");
			}
			for (Jedis j : jedises) {
				j.flushAll();
			}
			if (logger.isInfoEnabled()) {
				logger.info("RedisClient flushAll over!");
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		jedis.close();
	}

	// ------------------------------------------------------
	public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
		this.jedisPoolConfig = jedisPoolConfig;
	}

	public void setShards(List<JedisShardInfo> shards) {
		this.shards = shards;
	}

	@Override
	public CacheInteractionType getCacheInteractionType() {
		return CacheInteractionType.Remote;
	}

	@Override
	public CacheType getCacheType() {
		return CacheType.redis;
	}
}
