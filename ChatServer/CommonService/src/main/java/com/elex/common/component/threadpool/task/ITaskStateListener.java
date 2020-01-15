package com.elex.common.component.threadpool.task;

/**
 * 任务状态监听
 * 
 * @author mausmars
 *
 */
public interface ITaskStateListener {
	/**
	 * 通知
	 * 
	 * @param taskStateType
	 */
	void update(TaskStateType taskStateType);
}
