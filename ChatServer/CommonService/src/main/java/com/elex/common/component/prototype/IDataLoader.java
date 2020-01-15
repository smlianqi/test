package com.elex.common.component.prototype;

import java.util.Map;

import com.elex.common.component.data.IDao;
import com.elex.common.component.prototype.config.ScPrototype;

/**
 * 数据加载器
 * 
 * @author mausmars
 *
 */
public interface IDataLoader {
	/**
	 * 加载数据
	 * 
	 * @param jsonPath
	 * @param sc
	 * @param daoMap
	 * @param params
	 * @return 返回已经初始化数据的class
	 */
	Class<?>[] load(String jsonPath, ScPrototype sc, Map<Class<?>, IDao<Object>> daoMap, Object params);
}
