package com.elex.common.component.threadpool.task;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 工作任务管理器
 * 
 * @author mausmars
 *
 */
public class WorkTaskManager {
	// {groupKey:GroupTaskManager}
	private ConcurrentHashMap<String, GroupTaskManager> groupTaskManagerMap = new ConcurrentHashMap<String, GroupTaskManager>();

	public WorkFuture createRunnableTask(IHashTask task) {
		GroupTaskManager groupTaskManager = groupTaskManagerMap.get(task.getGroupKey());
		if (groupTaskManager == null) {
			groupTaskManager = new GroupTaskManager();
			GroupTaskManager gtm = groupTaskManagerMap.putIfAbsent(task.getGroupKey(), groupTaskManager);
			if (gtm != null) {
				groupTaskManager = gtm;
			}
		}
		if (groupTaskManager.isContain(task.getHashKey())) {
			return null;
		}
		TaskContext taskContext = new TaskContext();
		taskContext.setGroupTaskManager(groupTaskManager);

		WorkRunnableTask workTask = new WorkRunnableTask(task, taskContext);
		WorkFuture workFuture = new WorkFuture(workTask, HTaskType.Runnable);

		// 插入任务
		groupTaskManager.insertTask(workFuture);
		return workFuture;
	}

	public WorkFuture createCallableTask(IHashCallable<Object> task) {
		GroupTaskManager groupTaskManager = groupTaskManagerMap.get(task.getGroupKey());
		if (groupTaskManager == null) {
			groupTaskManager = new GroupTaskManager();
			GroupTaskManager gtm = groupTaskManagerMap.putIfAbsent(task.getGroupKey(), groupTaskManager);
			if (gtm != null) {
				groupTaskManager = gtm;
			}
		}
		if (groupTaskManager.isContain(task.getHashKey())) {
			return null;
		}
		TaskContext taskContext = new TaskContext();
		taskContext.setGroupTaskManager(groupTaskManager);

		WorkCallableTask workTask = new WorkCallableTask(task, taskContext);
		WorkFuture workFuture = new WorkFuture(workTask, HTaskType.Callable);

		// 插入任务
		groupTaskManager.insertTask(workFuture);
		return workFuture;
	}

	public IFuture removeTask(String groupKey, String key) {
		GroupTaskManager groupTaskManager = groupTaskManagerMap.get(groupKey);
		if (groupTaskManager != null) {
			return groupTaskManager.removeTask(key);
		}
		return null;
	}

	public int taskCount(String groupKey) {
		GroupTaskManager groupTaskManager = groupTaskManagerMap.get(groupKey);
		if (groupTaskManager == null) {
			return 0;
		}
		return groupTaskManager.taskCount();
	}
}
