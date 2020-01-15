package com.elex.common.component.threadpool.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WorkFuture implements IFuture {
	private Future<?> future;
	private IWorkTask workTask;
	private HTaskType taskType;

	public WorkFuture(IWorkTask workTask, HTaskType taskType) {
		this.workTask = workTask;
		this.taskType = taskType;
	}

	@Override
	public IHTask getTask() {
		return workTask.getTask();
	}

	@Override
	public HTaskType getHTaskType() {
		return taskType;
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return future.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return future.isCancelled();
	}

	@Override
	public boolean isDone() {
		return future.isDone();
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException {
		return future.get();
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return future.get(timeout, unit);
	}

	public void setFuture(Future<?> future) {
		this.future = future;
	}

	public <T extends IWorkTask> T getWorkTask() {
		return (T) workTask;
	}

	public void setWorkTask(IWorkTask workTask) {
		this.workTask = workTask;
	}
}
