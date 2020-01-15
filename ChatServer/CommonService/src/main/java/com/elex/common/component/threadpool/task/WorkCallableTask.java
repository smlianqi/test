package com.elex.common.component.threadpool.task;

import java.util.concurrent.Callable;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 抽象任务
 * 
 * @author mausmars
 *
 */
public final class WorkCallableTask implements IWorkTask, Callable<Object> {
	protected static final ILogger logger = XLogUtil.logger();

	private volatile TaskStateType taskStateType;
	private IHashCallable<Object> task;
	private TaskContext context;

	WorkCallableTask(IHashCallable<Object> task, TaskContext context) {
		this.task = task;
		this.context = context;
		this.taskStateType = TaskStateType.Wait;
	}

	public String getKey() {
		return task.getHashKey();
	}

	public String getGroupKey() {
		return task.getGroupKey();
	}

	@Override
	public Object call() throws Exception {
		boolean isRun = false;
		synchronized (this) {
			if (this.taskStateType == TaskStateType.Wait) {
				this.taskStateType = TaskStateType.Runing;
				isRun = true;
			}
		}
		Object obj = null;
		if (isRun) {
			if (logger.isDebugEnabled()) {
				logger.debug("Task [run] start! key=" + getKey() + ", groupKey=" + getGroupKey());
			}
			try {
				// 执行
				obj = task.call();
				if (logger.isDebugEnabled()) {
					logger.debug("Task [run] finished! key=" + getKey() + ", groupKey=" + getGroupKey());
				}
			} catch (Exception e) {
				logger.error("Task [run] error! key=" + getKey() + ", groupKey=" + getGroupKey(), e);
			}
			// 任务完成
			setTaskState(TaskStateType.Finished);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Task [run] state error! key=" + getKey() + ", groupKey=" + getGroupKey()
						+ ", taskStateType=" + this.taskStateType);
			}
		}
		// 任务执行完毕，移除任务
		context.getGroupTaskManager().removeTask(task.getHashKey());
		return obj;
	}

	/**
	 * 取消任务
	 * 
	 * @return
	 */
	public boolean cancelTask() {
		boolean isSuccess = false;
		synchronized (this) {
			if (this.taskStateType == TaskStateType.Wait) {
				this.taskStateType = TaskStateType.Cancel;
				isSuccess = true;
			}
		}
		return isSuccess;
	}

	public boolean isFinished() {
		return this.taskStateType == TaskStateType.Finished;
	}

	public boolean isCanceled() {
		return this.taskStateType == TaskStateType.Cancel;
	}

	private synchronized void setTaskState(TaskStateType taskStateType) {
		this.taskStateType = taskStateType;
	}

	public IHashCallable<Object> getTask() {
		return task;
	}
}
