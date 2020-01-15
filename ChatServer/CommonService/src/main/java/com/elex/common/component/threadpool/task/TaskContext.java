package com.elex.common.component.threadpool.task;

/**
 * 任务上下文
 * 
 * @author mausmars
 *
 */
public final class TaskContext {
	private GroupTaskManager groupTaskManager;

	public TaskContext() {
	}

	public GroupTaskManager getGroupTaskManager() {
		return groupTaskManager;
	}

	public void setGroupTaskManager(GroupTaskManager groupTaskManager) {
		this.groupTaskManager = groupTaskManager;
	}
}
