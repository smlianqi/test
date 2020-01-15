package com.elex.common.component.cache;

import com.elex.common.component.cache.type.CacheInteractionType;
import com.elex.common.component.cache.type.CacheType;
import com.elex.common.service.IService;

/**
 * redis服务接口
 * 
 * @author mausmars
 *
 */
public interface ICacheService extends IService {
	/**
	 * 创建缓存
	 * 
	 * @param param
	 * @return
	 */
	<T> T createCache(Object param);

	/**
	 * 移除缓存
	 * 
	 * @param cacheName
	 */
	void removeCache(String cacheName);

	CacheInteractionType getCacheInteractionType();

	CacheType getCacheType();

	/**
	 * 清除全部缓存
	 * 
	 * @return
	 */
	void flushAll();
}
