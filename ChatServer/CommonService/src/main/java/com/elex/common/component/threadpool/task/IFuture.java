package com.elex.common.component.threadpool.task;

import java.util.concurrent.Future;

/**
 * future接口
 * 
 * @author mausmars
 *
 */
public interface IFuture<T> extends IWorkTask, Future<T> {
	HTaskType getHTaskType();

	// 这里不暴露取消接口，通过IPoolExecutor的取消接口走
	// boolean cancel(boolean mayInterruptIfRunning);

}
