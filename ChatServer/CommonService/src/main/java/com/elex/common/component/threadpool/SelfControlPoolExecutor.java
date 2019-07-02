package com.elex.common.component.threadpool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.elex.common.component.threadpool.task.IFuture;
import com.elex.common.component.threadpool.task.IHTask;
import com.elex.common.component.threadpool.task.IHashCallable;
import com.elex.common.component.threadpool.task.IHashTask;
import com.elex.common.component.threadpool.task.WorkCallableTask;
import com.elex.common.component.threadpool.task.WorkFuture;
import com.elex.common.component.threadpool.task.WorkRunnableTask;
import com.elex.common.component.threadpool.task.WorkTaskManager;
import com.elex.common.component.threadpool.type.ExecutorStateType;
import com.elex.common.util.hash.IHashFunc;
import com.elex.common.util.hash.SimpleHashFunction;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 线程池
 * 
 * @author mausmars
 *
 */
public class SelfControlPoolExecutor implements IPoolExecutor {
	protected static final ILogger logger = XLogUtil.logger();

	private ScheduledExecutorService[] executors;
	private volatile ExecutorStateType executorStateType;

	// 默认hash函数
	private IHashFunc hashFunc;
	private WorkTaskManager workTaskManager = new WorkTaskManager();

	public SelfControlPoolExecutor(int count, String threadPoolName) {
		// 初始化线程池数量
		executors = new ScheduledExecutorService[count];
		for (int i = 0; i < count; i++) {
			// 创建单线程定时任务引擎
			executors[i] = Executors.newSingleThreadScheduledExecutor();
		}
		executorStateType = ExecutorStateType.Init;
	}

	@Override
	public void startup() {
		synchronized (this) {
			if (executorStateType == ExecutorStateType.Init) {
				if (hashFunc == null) {
					hashFunc = new SimpleHashFunction();
				}
				executorStateType = ExecutorStateType.Running;
			}
		}
	}

	@Override
	public List<IHashTask> shutdown(boolean isNow) {
		synchronized (this) {
			if (executorStateType == ExecutorStateType.Running) {
				executorStateType = ExecutorStateType.Stoped;
			}
		}
		if (isNow) {
			// 马上结束
			List<IHashTask> tasks = new LinkedList<>();
			for (ExecutorService executor : executors) {
				List<Runnable> ts = executor.shutdownNow();
				for (Runnable t : ts) {
					WorkRunnableTask workTask = (WorkRunnableTask) t;
					// 返回未完成的任务
					tasks.add(workTask.getTask());
				}
			}
			return tasks;
		} else {
			for (ExecutorService executor : executors) {
				executor.shutdown();
			}
			return null;
		}
	}

	private ScheduledExecutorService hashExecutor(IHTask hash) {
		int index = 0;
		if (hash.isDirectHash()) {
			Integer v = Integer.parseInt(hash.getGroupKey());
			index = (v % size());
		} else {
			long h = hashFunc.hash(hash.getGroupKey());
			h = Math.abs(h);
			index = (int) (h % size());
		}
		return executors[index];
	}

	@Override
	public void execute(IHashTask task) {
		if (executorStateType != ExecutorStateType.Running) {
			throw new RuntimeException("Executor is not running!");
		}
		WorkFuture workFuture = workTaskManager.createRunnableTask(task);
		if (workFuture == null) {
			// 重复任务添加
			if (logger.isDebugEnabled()) {
				logger.debug("Task repeat insert!!! key=" + task.getHashKey() + " ,groupkey=" + task.getGroupKey());
			}
			return;
		}
		ScheduledExecutorService executor = hashExecutor(task);

		WorkRunnableTask workTask = workFuture.getWorkTask();
		// 执行这个方法没有IFuture
		// 执行
		executor.execute(workTask);
	}

	@Override
	public IFuture submit(IHashCallable<Object> task) {
		if (executorStateType != ExecutorStateType.Running) {
			throw new RuntimeException("Executor is not running!");
		}
		WorkFuture workFuture = workTaskManager.createCallableTask(task);
		if (workFuture == null) {
			// 重复任务添加
			if (logger.isDebugEnabled()) {
				logger.debug("Task repeat insert!!! key=" + task.getHashKey() + " ,groupkey=" + task.getGroupKey());
			}
			return null;
		}
		ScheduledExecutorService executor = hashExecutor(task);

		WorkCallableTask workTask = workFuture.getWorkTask();
		// 执行
		Future<?> future = executor.submit(workTask);
		workFuture.setFuture(future);
		return workFuture;
	}

	@Override
	public IFuture submit(IHashTask task, Object result) {
		if (executorStateType != ExecutorStateType.Running) {
			throw new RuntimeException("Executor is not running!");
		}
		WorkFuture workFuture = workTaskManager.createRunnableTask(task);
		if (workFuture == null) {
			// 重复任务添加
			if (logger.isDebugEnabled()) {
				logger.debug("Task repeat insert!!! key=" + task.getHashKey() + " ,groupkey=" + task.getGroupKey());
			}
			return null;
		}
		ScheduledExecutorService executor = hashExecutor(task);

		WorkRunnableTask workTask = workFuture.getWorkTask();
		// 执行
		Future<?> future = executor.submit(workTask, result);
		workFuture.setFuture(future);
		return workFuture;
	}

	@Override
	public IFuture submit(IHashTask task) {
		if (executorStateType != ExecutorStateType.Running) {
			throw new RuntimeException("Executor is not running!");
		}
		WorkFuture workFuture = workTaskManager.createRunnableTask(task);
		if (workFuture == null) {
			// 重复任务添加
			if (logger.isDebugEnabled()) {
				logger.debug("Task repeat insert!!! key=" + task.getHashKey() + " ,groupkey=" + task.getGroupKey());
			}
			return null;
		}
		ScheduledExecutorService executor = hashExecutor(task);

		WorkRunnableTask workTask = workFuture.getWorkTask();
		// 执行
		Future<?> future = executor.submit(workTask);
		workFuture.setFuture(future);
		return workFuture;
	}

	@Override
	public IFuture schedule(IHashTask task, long delay, TimeUnit unit) {
		if (executorStateType != ExecutorStateType.Running) {
			throw new RuntimeException("Executor is not running!");
		}
		WorkFuture workFuture = workTaskManager.createRunnableTask(task);
		if (workFuture == null) {
			// 重复任务添加
			if (logger.isDebugEnabled()) {
				logger.debug("Task repeat insert!!! key=" + task.getHashKey() + " ,groupkey=" + task.getGroupKey());
			}
			return null;
		}
		ScheduledExecutorService executor = hashExecutor(task);

		WorkRunnableTask workTask = workFuture.getWorkTask();
		// 执行
		Future<?> future = executor.schedule(workTask, delay, unit);
		workFuture.setFuture(future);
		return workFuture;
	}

	@Override
	public IFuture schedule(IHashCallable<Object> task, long delay, TimeUnit unit) {
		if (executorStateType != ExecutorStateType.Running) {
			throw new RuntimeException("Executor is not running!");
		}
		WorkFuture workFuture = workTaskManager.createCallableTask(task);
		if (workFuture == null) {
			// 重复任务添加
			if (logger.isDebugEnabled()) {
				logger.debug("Task repeat insert!!! key=" + task.getHashKey() + " ,groupkey=" + task.getGroupKey());
			}
			return null;
		}
		ScheduledExecutorService executor = hashExecutor(task);

		WorkCallableTask workTask = workFuture.getWorkTask();
		// 执行
		Future<?> future = executor.schedule(workTask, delay, unit);
		workFuture.setFuture(future);
		return workFuture;
	}

	@Override
	public IFuture scheduleAtFixedRate(IHashTask task, long initialDelay, long period, TimeUnit unit) {
		if (executorStateType != ExecutorStateType.Running) {
			throw new RuntimeException("Executor is not running!");
		}
		WorkFuture workFuture = workTaskManager.createRunnableTask(task);
		if (workFuture == null) {
			// 重复任务添加
			if (logger.isDebugEnabled()) {
				logger.debug("Task repeat insert!!! key=" + task.getHashKey() + " ,groupkey=" + task.getGroupKey());
			}
			return null;
		}
		ScheduledExecutorService executor = hashExecutor(task);

		WorkRunnableTask workTask = workFuture.getWorkTask();
		// 执行
		Future<?> future = executor.scheduleAtFixedRate(workTask, initialDelay, period, unit);
		workFuture.setFuture(future);
		return workFuture;
	}

	@Override
	public IFuture scheduleWithFixedDelay(IHashTask task, long initialDelay, long delay, TimeUnit unit) {
		if (executorStateType != ExecutorStateType.Running) {
			throw new RuntimeException("Executor is not running!");
		}
		WorkFuture workFuture = workTaskManager.createRunnableTask(task);
		if (workFuture == null) {
			// 重复任务添加
			if (logger.isDebugEnabled()) {
				logger.debug("Task repeat insert!!! key=" + task.getHashKey() + " ,groupkey=" + task.getGroupKey());
			}
			return null;
		}
		ScheduledExecutorService executor = hashExecutor(task);

		WorkRunnableTask workTask = workFuture.getWorkTask();
		// 执行
		Future<?> future = executor.scheduleWithFixedDelay(workTask, initialDelay, delay, unit);
		workFuture.setFuture(future);
		return workFuture;
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		boolean isOver = true;
		for (ExecutorService executor : executors) {
			if (!executor.awaitTermination(timeout, unit)) {
				isOver = false;
				break;
			}
		}
		return isOver;
	}

	@Override
	public boolean isShutdown() {
		return executorStateType == ExecutorStateType.Stoped;
	}

	@Override
	public boolean isTerminated() {
		for (ExecutorService executor : executors) {
			if (!executor.isTerminated()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int taskCount(String groupKey) {
		return workTaskManager.taskCount(groupKey);
	}

	@Override
	public IFuture removeTask(String groupKey, String key) {
		return workTaskManager.removeTask(groupKey, key);
	}

	@Override
	public IFuture removeAndCancelTask(String groupKey, String key, boolean mayInterruptIfRunning) {
		WorkFuture future = (WorkFuture) workTaskManager.removeTask(groupKey, key);
		if (future == null) {
			return null;
		}
		future.cancel(mayInterruptIfRunning);
		return future;
	}

	@Override
	public int size() {
		return executors.length;
	}

	// --------------------------
	public void setHashFunc(IHashFunc hashFunc) {
		this.hashFunc = hashFunc;
	}
}
