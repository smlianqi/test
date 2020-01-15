package com.elex.common.component.task.quartz;

/**
 * 任务上下文
 * 
 * @author mausmars
 * 
 */
public class TaskContext {
	private ITaskConfig config;
	private ITaskTypeConfig taskType;

	public TaskContext(ITaskTypeConfig taskType, ITaskConfig config) {
		this.config = config;
		this.taskType = taskType;
	}

	public ITaskConfig getConfig() {
		return config;
	}

	public void setConfig(ITaskConfig config) {
		this.config = config;
	}

	public ITaskTypeConfig getTaskType() {
		return taskType;
	}

	public void setTaskType(ITaskTypeConfig taskType) {
		this.taskType = taskType;
	}
}
