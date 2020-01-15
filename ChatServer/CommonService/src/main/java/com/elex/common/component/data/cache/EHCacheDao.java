package com.elex.common.component.data.cache;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.ehcache.Cache;

import com.elex.common.component.io.IWriteReadable;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public class EHCacheDao implements ICacheDao {
	protected static final ILogger logger = XLogUtil.logger();

	private Cache<String, Object> cache;

	@Override
	public <T extends IWriteReadable> List<T> selectCache(CacheKey cacheKey) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheDao selectCache");
		}
		List<T> datas = new LinkedList<T>();

		String key = cacheKey.getFieldKey();
		if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Main) {
			Object data = cache.get(key);
			if (data == null) {
				return null;
			}
			datas.add((T) data);
		} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Single) {
			// 对应1个主Id
			String mainKey = (String) cache.get(key);
			if (mainKey == null) {
				return null;
			}
			Object data = cache.get(mainKey);
			if (data != null) {
				datas.add((T) data);
			}
		} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Multi) {
			// 对应多个主Id
			Set<String> mainKeys = smembers(key);
			if (mainKeys == null) {
				return null;
			}
			for (String mainKey : mainKeys) {
				Object data = cache.get(mainKey);
				datas.add((T) data);
			}
		}
		return datas;
	}

	@Override
	public <T extends IWriteReadable> List<T> selectAll(ICacheKeyTemplate ckt) {
		List<T> datas = new LinkedList<T>();
		String typeTotalKey = CacheKey.getTableKey(ckt.getCls().getSimpleName(), CacheKeyType.CK_Main);
		Set<String> feildKeys = smembers(typeTotalKey);
		if (feildKeys != null) {
			for (String feildKey : feildKeys) {
				Object data = cache.get(feildKey);
				datas.add((T) data);
			}
		}
		return datas;
	}

	@Override
	public long selectAllCount(ICacheKeyTemplate ckt) {
		// 如果把数据全加载到cache中才是正确的值
		String typeTotalKey = CacheKey.getTableKey(ckt.getCls().getSimpleName(), CacheKeyType.CK_Main);
		return scard(typeTotalKey);
	}

	@Override
	public int selectCacheCount(CacheKey cacheKey) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheDao selectCacheCount");
		}
		String key = cacheKey.getFieldKey();
		int count = 0;
		if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Main) {
			Object data = cache.get(key);
			if (data != null) {
				count = 1;
			}
		} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Single) {
			// 对应1个主Id
			String mainKey = (String) cache.get(key);
			if (mainKey != null) {
				count = 1;
			}
		} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Multi) {
			// 对应多个主Id
			count = scard(key);
		}
		return count;
	}

	@Override
	public void remove(CacheKey cacheKey) {
		if (logger.isDebugEnabled()) {
			logger.debug("CacheDao remove");
		}
		try {
			List<IWriteReadable> pojos = selectCache(cacheKey);
			// 批量删除
			batchRemove(pojos, cacheKey.getCacheKeyTemplate(), null);
		} catch (Exception e) {
			throw new RuntimeException(e);
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
					String key = cacheKey.getFieldKey();

					if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Main) {
						// 添加归属成员
						cache.put(key, pojo);
						// TODO 设置超时
						insertKey2TypeTotal(cacheKey);
					} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Single) {
						// 对应1个主Id
						cache.put(key, cacheKey.getMainCacheKey().getFieldKey());
						// TODO 设置超时
						insertKey2TypeTotal(cacheKey);
					} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Multi) {
						// 对应多个主Id
						sadd(key, cacheKey.getMainCacheKey().getFieldKey());
						// TODO 设置超时
						insertKey2TypeTotal(cacheKey);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
				String key = cacheKey.getFieldKey();

				if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Main) {
					// 添加归属成员
					cache.put(key, pojo);
					insertKey2TypeTotal(cacheKey);
				} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Single) {
					cache.put(key, cacheKey.getMainCacheKey().getFieldKey());
					insertKey2TypeTotal(cacheKey);
				} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Multi) {
					// 这里还是要添加，要求登录的时候全部加载一次，否则是不完整的集合
					sadd(key, cacheKey.getMainCacheKey().getFieldKey());
					insertKey2TypeTotal(cacheKey);
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
				String key = cacheKey.getFieldKey();
				String oldKey = cacheKey.getOldFieldKey();

				if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Main) {
					isModifyMain = true;

					// 建立新索引
					cache.put(key, pojo);
					// TODO 设置超时
					insertKey2TypeTotal(cacheKey);

					// 单一索引
					List<CacheKey> singleCKs = ckt.getCacheKeys(pojo, CacheKeyType.CK_Single);
					for (CacheKey singleCK : singleCKs) {
						String singleKey = singleCK.getFieldKey();
						cache.put(singleKey, cacheKey.getFieldKey());
					}

					List<CacheKey> multiCKs = ckt.getCacheKeys(pojo, CacheKeyType.CK_Multi);
					for (CacheKey multiCK : multiCKs) {
						String multiKey = multiCK.getFieldKey();

						srem(multiKey, cacheKey.getMainCacheKey().getOldFieldKey());// 从集合中移除
						sadd(multiKey, cacheKey.getMainCacheKey().getFieldKey());
					}
					// 删除旧索引
					cache.remove(oldKey);
					removeKeyFromTypeTotal(cacheKey, true);
					break;
				}
			}
			if (!isModifyMain) {
				// 更新内容
				CacheKey cacheKey = ckt.getMainCacheKeys(pojo);
				String key = cacheKey.getFieldKey();
				cache.put(key, pojo);
			}

			for (CacheKey cacheKey : changeCacheKeys) {
				String key = cacheKey.getFieldKey();
				String oldKey = cacheKey.getOldFieldKey();
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
					String v = (String) cache.get(oldKey);
					cache.put(key, v);

					insertKey2TypeTotal(cacheKey);

					// 删除旧索引
					cache.remove(oldKey);
					removeKeyFromTypeTotal(cacheKey, true);
				} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Multi) {
					// 在前面的主索引变化已经处理了,这里拿的旧值已经是新值
					exchange(key, oldKey);
					// if (cacheKey.getMainCacheKey() != null) {
					// cache.srem(key,
					// cacheKey.getMainCacheKey().getOldFieldKey());// 从集合中移除
					// cache.sadd(key,
					// cacheKey.getMainCacheKey().getFieldKey());
					// }
					insertKey2TypeTotal(cacheKey);

					// 删除旧索引
					cache.remove(oldKey);
					removeKeyFromTypeTotal(cacheKey, true);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void exchange(String key, String oldKey) {
		Set<String> values = smembers(oldKey);
		if (values != null) {
			for (String value : values) {
				sadd(key, value);
			}
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
				String key = cacheKey.getFieldKey();
				if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Main) {
					cache.remove(key);
					removeKeyFromTypeTotal(cacheKey, false);
				} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Single) {
					cache.remove(key);
					removeKeyFromTypeTotal(cacheKey, false);
				} else if (cacheKey.getCacheKeyType() == CacheKeyType.CK_Multi) {
					// 移除成员
					srem(key, cacheKey.getMainCacheKey().getFieldKey());
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

	private void sadd(String key, String mainKey) {
		Set<String> keySet = (Set<String>) cache.get(key);
		if (keySet == null) {
			keySet = new HashSet<String>();
			cache.put(key, keySet);
		}
		keySet.add(mainKey);
	}

	private void srem(String key, String mainKey) {
		Set<String> keySet = (Set<String>) cache.get(key);
		if (keySet == null) {
			return;
		}
		keySet.remove(mainKey);
	}

	private Set<String> smembers(String key) {
		Set<String> keySet = (Set<String>) cache.get(key);
		return keySet;
	}

	private int scard(String key) {
		Set<String> keySet = (Set<String>) cache.get(key);
		if (keySet == null || keySet.isEmpty()) {
			return 0;
		}
		return keySet.size();
	}

	private void insertKey2TypeTotal(CacheKey cacheKey) {
		// 总集合添加
		String key = cacheKey.getFieldKey();
		String totalKey = cacheKey.getTableKey();
		sadd(totalKey, key);
	}

	private void removeKeyFromTypeTotal(CacheKey cacheKey, boolean isOld) {
		// 总集合移除
		String key = null;
		if (isOld) {
			key = cacheKey.getOldFieldKey();
		} else {
			key = cacheKey.getFieldKey();
		}
		String totalKey = cacheKey.getTableKey();
		srem(totalKey, key);
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
		String totalKey = CacheKey.getTableKey(ckt.getCls().getSimpleName(), cacheKeyType);

		Set<String> feildKeys = smembers(totalKey);
		if (feildKeys != null) {
			for (String feildKey : feildKeys) {
				// 移除
				cache.remove(feildKey);
			}
		}
		// 移除
		cache.remove(totalKey);
	}

	// ------------------------------
	public void setCache(Object cache) {
		this.cache = (Cache<String, Object>) cache;
	}
}
