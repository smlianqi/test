package com.elex.common.component.threadpool.task;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 组任务管理
 * 
 * @author mausmars
 *
 */
public class GroupTaskManager {
	// {任务key:WorkTask}
	private ConcurrentHashMap<String, IFuture> taskMap = new ConcurrentHashMap<String, IFuture>();

	public boolean isContain(String key) {
		return taskMap.containsKey(key);
	}

	/**
	 * 插入任务
	 * 
	 * @param task
	 */
	public IFuture insertTask(IFuture task) {
		IFuture t = taskMap.putIfAbsent(task.getTask().getHashKey(), task);
		if (t == null) {
			t = task;
		}
		return t;
	}

	/**
	 * 移除任务
	 * 
	 * @param key
	 */
	public IFuture removeTask(String key) {
		return this.taskMap.remove(key);
	}

	/**
	 * 任务数量
	 * 
	 * @return
	 */
	public int taskCount() {
		return taskMap.size();
	}
}
