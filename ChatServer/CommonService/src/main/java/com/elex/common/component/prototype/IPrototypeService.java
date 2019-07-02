package com.elex.common.component.prototype;

import com.elex.common.component.data.IDao;
import com.elex.common.service.IService;

public interface IPrototypeService extends IService {
	/**
	 * 获得dao
	 * 
	 * @param cls
	 * @return
	 */
	<T extends IDao<?>> T getPrototype(Class<?> cls);
}
