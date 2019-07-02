package com.elex.common.component.threadpool.task;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 抽象任务
 * 
 * @author mausmars
 *
 */
public final class WorkRunnableTask implements IWorkTask, Runnable {
	protected static final ILogger logger = XLogUtil.logger();

	private volatile TaskStateType taskStateType;
	private IHashTask task;
	private TaskContext context;

	WorkRunnableTask(IHashTask task, TaskContext context) {
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
	public void run() {
		boolean isRun = false;
		synchronized (this) {
			if (this.taskStateType == TaskStateType.Wait) {
				this.taskStateType = TaskStateType.Runing;
				isRun = true;
			}
		}
		if (isRun) {
			if (logger.isDebugEnabled()) {
				logger.debug("Task [run] start! key=" + getKey() + ", groupKey=" + getGroupKey());
			}
			try {
				// 执行
				task.run();
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

	public IHashTask getTask() {
		return task;
	}
}
