package com.elex.common.component.data.cache;

import java.util.List;

/**
 * CacheKey模板接口
 * 
 * @author mausmars
 *
 */
public interface ICacheKeyTemplate {
	/**
	 * 是否包含主域
	 * 
	 * @return
	 */
	boolean isContainMainField();

	/**
	 * 获取主域值
	 * 
	 * @param obj
	 * @return
	 */
	String createMainFieldValue(Object obj);

	/**
	 * 获得实体类的class
	 * 
	 * @return
	 */
	Class<?> getCls();

	/**
	 * 获得对象，指定索引类型的最终key
	 * 
	 * @param obj
	 * @return
	 */
	List<CacheKey> getUpdateCacheKeys(Object obj);

	/**
	 * 获得对象，指定索引类型的最终key
	 * 
	 * @param obj
	 * @param cacheKeyType
	 * @return
	 * @throws Exception
	 */
	List<CacheKey> getCacheKeys(Object obj, CacheKeyType cacheKeyType);

	/**
	 * 获得对象，主索引类型的最终key
	 * 
	 * @param obj
	 * @return
	 */
	CacheKey getMainCacheKeys(Object obj);

	/**
	 * 获得对象，所有索引的最终key
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	List<CacheKey> getAllCacheKeys(Object obj);

	/**
	 * 获得对象域，对应的最终索引
	 * 
	 * @param fieldNames
	 * @param fieldKeyParams
	 * @param ownKeyParams
	 * @return
	 * @throws Exception
	 */
	CacheKey getCacheKeys(String[] fieldNames, Object[] fieldKeyParams);

	CacheKey getMainCacheKeys(Object[] fieldKeyParams);
	/**
	 * 获得域对应的索引的信息
	 * 
	 * @param fieldNames
	 * @return
	 */
	// Collection<CKeyTemplate> getCKeyTemplates(String[] fieldNames);

	// Collection<CKeyTemplate> getCKeyTemplates(Collection<String> fieldNames);

	/**
	 * 获得域对应的索引的信息
	 * 
	 * @param fieldName
	 * @return
	 */
	// Collection<CKeyTemplate> getCKeyTemplates(String fieldName);
}
