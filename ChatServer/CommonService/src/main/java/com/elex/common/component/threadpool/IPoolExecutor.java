package com.elex.common.component.threadpool;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.elex.common.component.threadpool.task.IFuture;
import com.elex.common.component.threadpool.task.IHashCallable;
import com.elex.common.component.threadpool.task.IHashTask;

/**
 * 池引擎
 * 
 * @author mausmars
 *
 */
public interface IPoolExecutor {
	/**
	 * 启动
	 */
	void startup();

	/**
	 * 结束，如果是立即结束返回未执行任务
	 * 
	 * @param isNow
	 * @return
	 */
	List<IHashTask> shutdown(boolean isNow);

	/**
	 * 执行任务
	 * 
	 * @param task
	 */
	void execute(IHashTask task);

	/**
	 * 任务带IFuture
	 * 
	 * @param task
	 * @return
	 */
	IFuture submit(IHashCallable<Object> task);

	/**
	 * 任务带IFuture
	 * 
	 * @param task
	 * @param result
	 * @return 返回null，添加失败，重复任务
	 */
	IFuture submit(IHashTask task, Object result);

	/**
	 * 任务带IFuture
	 * 
	 * @param task
	 * @return 返回null，添加失败，重复任务
	 */
	IFuture submit(IHashTask task);

	/**
	 * 延迟执行
	 * 
	 * @param task
	 * @param delay
	 * @param unit
	 * @return 返回null，添加失败，重复任务
	 */
	IFuture schedule(IHashTask task, long delay, TimeUnit unit);

	/**
	 * 延迟执行
	 * 
	 * @param callable
	 * @param delay
	 * @param unit
	 * @return 返回null，添加失败，重复任务
	 */
	IFuture schedule(IHashCallable<Object> callable, long delay, TimeUnit unit);

	/**
	 * 固定n秒，如果任务n秒内不能完成，会在执行完后立即执行
	 * 
	 * @param task
	 * @param initialDelay
	 * @param period
	 * @param unit
	 * @return 返回null，添加失败，重复任务
	 */
	IFuture scheduleAtFixedRate(IHashTask task, long initialDelay, long period, TimeUnit unit);

	/**
	 * 任务执行完后n秒
	 * 
	 * @param task
	 * @param initialDelay
	 * @param delay
	 * @param unit
	 * @return 返回null，添加失败，重复任务
	 */
	IFuture scheduleWithFixedDelay(IHashTask task, long initialDelay, long delay, TimeUnit unit);

	/**
	 * 是否终结
	 * 
	 * @return
	 */
	boolean isTerminated();

	/**
	 * 是否停止
	 * 
	 * @return
	 */
	boolean isShutdown();

	/**
	 * 移除任务，取消自己做
	 * 
	 * @param groupKey
	 * @param key
	 */
	IFuture removeTask(String groupKey, String key);

	/**
	 * 移除任务，并且取消
	 * 
	 * @param groupKey
	 * @param key
	 */
	IFuture removeAndCancelTask(String groupKey, String key, boolean mayInterruptIfRunning);

	/**
	 * 任务数量
	 * 
	 * @param groupKey
	 * @return
	 */
	int taskCount(String groupKey);

	/**
	 * 线程池线程数量
	 * 
	 * @return
	 */
	int size();

	/**
	 * 等待结束
	 * 
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;
}
