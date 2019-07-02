package com.elex.common.component.threadpool;

import com.elex.common.component.threadpool.type.ThreadpoolType;
import com.elex.common.service.IService;

/**
 * 线程池服务
 * 
 * @author mausmars
 *
 */
public interface IThreadPoolService extends IService {
	/**
	 * 获取线程池引擎
	 * 
	 * @return
	 */
	IPoolExecutor getPoolExecutor();

	/**
	 * 获取类型
	 * 
	 * @return
	 */
	ThreadpoolType getThreadpoolType();

}
