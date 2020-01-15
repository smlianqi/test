package com.elex.common.component.data.cache;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.elex.common.component.io.IWriteReadable;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import redis.clients.jedis.ShardedJedis;

/**
 * 缓存需要登录从数据库加载信息，做初始化
 * 
 * @author mausmars
 *
 */
public class RedisCacheDao implements ICacheDao {
	protected static final ILogger logger = XLogUtil.logger();

	private ShardedJedis cache;

	@Override
	public void remove(CacheKey cacheKey) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(IWriteReadable pojo, ICacheKeyTemplate ckt, Object attach) {
		if (logger.isInfoEnabled()) {
			logger.info("CacheDao insert.");
		}
		try {
			List<CacheKey> cacheKeys = ckt.getAllCacheKeys(pojo);
			// 一个数据实体对应多个key，这里key超时需要有规则
			for (CacheKey cacheKey : cacheKeys) {
				String typeTotalKey = getTypeTotalKey(cacheKey);
				byte[] key = getKey(typeTotalKey, cacheKey.getFieldKey());

				if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Main) {
					// 添加归属成员
					cache.set(key, pojo.getBytes());
					// TODO 设置超时
					insertKey2TypeTotal(typeTotalKey, cacheKey.getFieldKey());
				} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Single) {
					cache.set(key, cacheKey.getMainCacheKey().getFieldKey().getBytes());
					// TODO 设置超时
					insertKey2TypeTotal(typeTotalKey, cacheKey.getFieldKey());
				} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Multi) {
					// 这里还是要添加，要求登录的时候全部加载一次
					cache.sadd(key, cacheKey.getMainCacheKey().getFieldKey().getBytes());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(IWriteReadable pojo, ICacheKeyTemplate ckt, Object attach) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheDao update.");
		}
		try {
			boolean isModifyMain = false;
			List<CacheKey> changeCacheKeys = ckt.getUpdateCacheKeys(pojo);
			for (CacheKey cacheKey : changeCacheKeys) {
				String typeTotalKey = getTypeTotalKey(cacheKey);
				byte[] key = getKey(typeTotalKey, cacheKey.getFieldKey());
				byte[] oldKey = getKey(typeTotalKey, cacheKey.getOldFieldKey());
				if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Main) {
					isModifyMain = true;

					// 建立新索引
					cache.set(key, pojo.getBytes());
					// TODO 设置超时
					insertKey2TypeTotal(typeTotalKey, cacheKey.getFieldKey());

					// 单一索引
					List<CacheKey> singleCKs = ckt.getCacheKeys(pojo, CacheKeyType.CK_Single);
					for (CacheKey singleCK : singleCKs) {
						String singleTypeTotalKey = getTypeTotalKey(singleCK);
						byte[] singleKey = getKey(singleTypeTotalKey, singleCK.getFieldKey());
						cache.set(singleKey, cacheKey.getFieldKey().getBytes());
					}

					List<CacheKey> multiCKs = ckt.getCacheKeys(pojo, CacheKeyType.CK_Multi);
					for (CacheKey multiCK : multiCKs) {
						String multiTypeTotalKey = getTypeTotalKey(multiCK);
						byte[] multiKey = getKey(multiTypeTotalKey, multiCK.getFieldKey());

						cache.srem(multiKey, cacheKey.getMainCacheKey().getOldFieldKey().getBytes());// 从集合中移除
						cache.sadd(multiKey, cacheKey.getMainCacheKey().getFieldKey().getBytes());
					}
					// 删除旧索引
					cache.del(oldKey);
					removeKeyFromTypeTotal(typeTotalKey, cacheKey.getOldFieldKey());
					break;
				}
			}
			if (!isModifyMain) {
				// 更新内容
				CacheKey cacheKey = ckt.getMainCacheKeys(pojo);
				String typeTotalKey = getTypeTotalKey(cacheKey);
				byte[] key = getKey(typeTotalKey, cacheKey.getFieldKey());
				cache.set(key, pojo.getBytes());
			}

			for (CacheKey cacheKey : changeCacheKeys) {
				String typeTotalKey = getTypeTotalKey(cacheKey);
				byte[] key = getKey(typeTotalKey, cacheKey.getFieldKey());
				byte[] oldKey = getKey(typeTotalKey, cacheKey.getOldFieldKey());
				if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Main) {

				} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Single) {
					// if (cacheKey.getMainCacheKey() != null) {
					// cache.set(key, cacheKey.getMainCacheKey().getFieldKey());
					// // TODO 设置超时
					// } else {
					// byte[] v = cache.get(oldKey);
					// cache.set(key, v);
					// }
					// 在前面的主索引变化已经处理了,这里拿的旧值已经是新值
					byte[] v = cache.get(oldKey);
					cache.set(key, v);

					insertKey2TypeTotal(typeTotalKey, cacheKey.getFieldKey());

					// 删除旧索引
					cache.del(oldKey);
					removeKeyFromTypeTotal(typeTotalKey, cacheKey.getOldFieldKey());
				} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Multi) {
					// 在前面的主索引变化已经处理了,这里拿的旧值已经是新值
					exchange(key, oldKey);
					// if (cacheKey.getMainCacheKey() != null) {
					// cache.srem(key,
					// cacheKey.getMainCacheKey().getOldFieldKey());// 从集合中移除
					// cache.sadd(key,
					// cacheKey.getMainCacheKey().getFieldKey());
					// }
					insertKey2TypeTotal(typeTotalKey, cacheKey.getFieldKey());

					// 删除旧索引
					cache.del(oldKey);
					removeKeyFromTypeTotal(typeTotalKey, cacheKey.getOldFieldKey());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void exchange(byte[] key, byte[] oldKey) {
		Set<byte[]> values = cache.smembers(oldKey);
		for (byte[] value : values) {
			cache.sadd(key, value);
		}
	}

	@Override
	public void remove(IWriteReadable pojo, ICacheKeyTemplate ckt, Object attach) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheDao remove.");
		}
		try {
			List<CacheKey> cacheKeys = ckt.getAllCacheKeys(pojo);
			for (CacheKey cacheKey : cacheKeys) {
				String typeTotalKey = getTypeTotalKey(cacheKey);
				byte[] key = getKey(typeTotalKey, cacheKey.getFieldKey());
				if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Main) {
					cache.del(key);

					removeKeyFromTypeTotal(typeTotalKey, cacheKey.getFieldKey());
				} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Single) {
					cache.del(key);

					removeKeyFromTypeTotal(typeTotalKey, cacheKey.getFieldKey());
				} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Multi) {
					// 移除成员
					cache.srem(key, cacheKey.getMainCacheKey().getFieldKey().getBytes());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void batchInsert(List<? extends IWriteReadable> pojos, ICacheKeyTemplate ckt, Object attach) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheDao batchInsert.");
		}
		for (IWriteReadable pojo : pojos) {
			insert(pojo, ckt, attach);
		}
	}

	@Override
	public void batchUpdate(List<? extends IWriteReadable> pojos, ICacheKeyTemplate ckt, Object attach) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheDao batchUpdate.");
		}
		for (IWriteReadable pojo : pojos) {
			update(pojo, ckt, attach);
		}
	}

	@Override
	public void batchRemove(List<? extends IWriteReadable> pojos, ICacheKeyTemplate ckt, Object attach) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheDao batchRemove.");
		}
		for (IWriteReadable pojo : pojos) {
			remove(pojo, ckt, attach);
		}
	}

	@Override
	public void cacheAll(List<? extends IWriteReadable> pojos, ICacheKeyTemplate ckt) {
		if (logger.isInfoEnabled()) {
			logger.info("CacheDao cacheAll.");
		}
		try {
			for (IWriteReadable pojo : pojos) {
				List<CacheKey> cacheKeys = ckt.getAllCacheKeys(pojo);
				for (CacheKey cacheKey : cacheKeys) {
					String typeTotalKey = getTypeTotalKey(cacheKey);
					byte[] key = getKey(typeTotalKey, cacheKey.getFieldKey());
					if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Main) {
						// 添加归属成员
						cache.set(key, pojo.getBytes());
						// TODO 设置超时
						insertKey2TypeTotal(typeTotalKey, cacheKey.getFieldKey());
					} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Single) {
						// 对应1个主Id
						cache.set(key, cacheKey.getMainCacheKey().getFieldKey().getBytes());
						// TODO 设置超时
						insertKey2TypeTotal(typeTotalKey, cacheKey.getFieldKey());
					} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Multi) {
						// 对应多个主Id
						cache.sadd(key, cacheKey.getMainCacheKey().getFieldKey().getBytes());
						// TODO 设置超时
						insertKey2TypeTotal(typeTotalKey, cacheKey.getFieldKey());
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T extends IWriteReadable> List<T> selectCache(CacheKey cacheKey) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheDao selectCache.");
		}
		Class<?> cls = cacheKey.getCacheKeyTemplate().getCls();
		List<T> datas = new LinkedList<T>();
		if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Main) {
			String typeTotalKey = getTypeTotalKey(cacheKey);
			byte[] key = getKey(typeTotalKey, cacheKey.getFieldKey());
			byte[] data = cache.get(key);
			if (data == null) {
				return null;
			}
			datas.add(createEntity(data, cls));
		} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Single) {
			String typeTotalKey = getTypeTotalKey(cacheKey);
			byte[] key = getKey(typeTotalKey, cacheKey.getFieldKey());
			// 对应1个主Id
			byte[] mainFieldKey = cache.get(key);
			if (mainFieldKey == null) {
				return null;
			}
			String mainTotalKey = getTypeTotalKey(cacheKey.getTableKey(), CacheKeyType.CK_Main);
			byte[] mainKey = getKey(mainTotalKey, new String(mainFieldKey));
			byte[] data = cache.get(mainKey);
			if (data != null) {
				datas.add(createEntity(data, cls));
			}
		} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Multi) {
			String typeTotalKey = getTypeTotalKey(cacheKey);
			byte[] key = getKey(typeTotalKey, cacheKey.getFieldKey());
			// 对应多个主Id
			Set<byte[]> mainFieldKeys = cache.smembers(key);
			if (mainFieldKeys == null) {
				return null;
			}
			for (byte[] mainFieldKey : mainFieldKeys) {
				String mainTotalKey = getTypeTotalKey(cacheKey.getTableKey(), CacheKeyType.CK_Main);
				byte[] mainKey = getKey(mainTotalKey, new String(mainFieldKey));
				byte[] data = cache.get(mainKey);
				datas.add(createEntity(data, cls));
			}
		}
		return datas;
	}

	// 清除全部缓存
	@Override
	public void clearCache(ICacheKeyTemplate ckt) {
		// multi
		clearTypeCache(ckt, CacheKeyType.CK_Multi);
		// singl
		clearTypeCache(ckt, CacheKeyType.CK_Single);
		// main
		clearTypeCache(ckt, CacheKeyType.CK_Main);
	}

	private void clearTypeCache(ICacheKeyTemplate ckt, CacheKeyType cacheKeyType) {
		String typeTotalKey = getTypeTotalKey(ckt.getCls().getSimpleName(), cacheKeyType);
		Set<String> feildKeys = cache.smembers(typeTotalKey);
		for (String feildKey : feildKeys) {
			byte[] key = getKey(typeTotalKey, feildKey);
			cache.del(key);
		}
		cache.del(typeTotalKey);
	}

	@Override
	public <T extends IWriteReadable> List<T> selectAll(ICacheKeyTemplate ckt) {
		String typeTotalKey = getTypeTotalKey(ckt.getCls().getSimpleName(), CacheKeyType.CK_Main);
		Set<String> feildKeys = cache.smembers(typeTotalKey);

		List<T> datas = new LinkedList<T>();
		for (String feildKey : feildKeys) {
			byte[] key = getKey(typeTotalKey, feildKey);
			byte[] data = cache.get(key);
			datas.add(createEntity(data, ckt.getCls()));
		}
		return datas;
	}

	@Override
	public long selectAllCount(ICacheKeyTemplate ckt) {
		// 如果把数据全加载到redis中才是正确的值！
		String typeTotalKey = getTypeTotalKey(ckt.getClass().getSimpleName(), CacheKeyType.CK_Main);
		return cache.scard(typeTotalKey);
	}

	private <T extends IWriteReadable> T createEntity(byte[] data, Class<?> cls) {
		try {
			T t = (T) cls.newInstance();
			t.initField(data);
			return t;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void insertKey2TypeTotal(String totalKey, String key) {
		// 总集合添加
		// tableKey_type:feilds
		cache.sadd(totalKey.getBytes(), key.getBytes());
	}

	private void removeKeyFromTypeTotal(String totalKey, String key) {
		// 总集合移除
		cache.srem(totalKey.getBytes(), key.getBytes());
	}

	private byte[] getKey(String totalKey, String fieldKey) {
		return getKeyStr(totalKey, fieldKey).getBytes();
	}

	private String getTypeTotalKey(String tableKey, CacheKeyType cacheKeyType) {
		return getTypeTotalStr(tableKey, cacheKeyType);
	}

	private String getTypeTotalKey(CacheKey cacheKey) {
		return getTypeTotalStr(cacheKey.getTableKey(), cacheKey.getCacheKeyType());
	}

	private String getTypeTotalStr(String tableKey, CacheKeyType cacheKeyType) {
		// 表_main或其他类型
		StringBuilder sb = new StringBuilder();
		sb.append(tableKey);
		sb.append("_");
		sb.append(cacheKeyType.suffix);
		return sb.toString();
	}

	private String getKeyStr(String totalKey, String fieldKey) {
		// 表_main_fieldKey
		StringBuilder sb = new StringBuilder();
		sb.append(totalKey);
		sb.append("_");
		sb.append(fieldKey);
		return sb.toString();
	}

	@Override
	public int selectCacheCount(CacheKey cacheKey) {
		return 0;
	}

	// ------------------------------
	public void setCache(Object cache) {
		this.cache = (ShardedJedis) cache;
	}
}
