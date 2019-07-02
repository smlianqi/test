package com.elex.common.component.threadbox;

import com.elex.common.service.IService;

/**
 * 线程工具箱
 * 
 * @author mausmars
 *
 */
public interface IThreadBoxService extends IService {
	void remove();

	<T> T fetch(Object key);

	void store(Object key, Object value);
}
