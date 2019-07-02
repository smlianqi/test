package com.elex.common.component.data.cache;

import java.util.List;

import com.elex.common.component.io.IWriteReadable;

public interface ICacheDao {
	/**
	 * 查询
	 * 
	 * @param cacheKey
	 * @param cls
	 * @return
	 */
	<T extends IWriteReadable> List<T> selectCache(CacheKey cacheKey);

	int selectCacheCount(CacheKey cacheKey);

	<T extends IWriteReadable> List<T> selectAll(ICacheKeyTemplate ckt);

	long selectAllCount(ICacheKeyTemplate ckt);

	/**
	 * 缓存
	 * 
	 * @param cacheKey
	 * @param pojos
	 * @param ckt
	 */
	void cacheAll(List<? extends IWriteReadable> pojos, ICacheKeyTemplate ckt);

	void remove(CacheKey cacheKey);

	/**
	 * 插入
	 * 
	 * @param pojo
	 * @param ckt
	 * @param attach
	 */
	void insert(IWriteReadable pojo, ICacheKeyTemplate ckt, Object attach);

	/**
	 * 更新
	 * 
	 * @param pojo
	 * @param ckt
	 * @param attach
	 */
	void update(IWriteReadable pojo, ICacheKeyTemplate ckt, Object attach);

	/**
	 * 移除
	 * 
	 * @param pojo
	 * @param ckt
	 * @param attach
	 */
	void remove(IWriteReadable pojo, ICacheKeyTemplate ckt, Object attach);

	/**
	 * 批量插入
	 * 
	 * @param pojos
	 * @param ckt
	 * @param attach
	 */
	void batchInsert(List<? extends IWriteReadable> pojos, ICacheKeyTemplate ckt, Object attach);

	/**
	 * 批量更新
	 * 
	 * @param pojos
	 * @param ckt
	 * @param attach
	 */
	void batchUpdate(List<? extends IWriteReadable> pojos, ICacheKeyTemplate ckt, Object attach);

	/**
	 * 批量移除
	 * 
	 * @param pojos
	 * @param ckt
	 * @param attach
	 */
	void batchRemove(List<? extends IWriteReadable> pojos, ICacheKeyTemplate ckt, Object attach);

	/**
	 * 清除
	 * 
	 * @param tableKey
	 */
	void clearCache(ICacheKeyTemplate ckt);
}
