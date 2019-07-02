package com.elex.common.component.task;

import com.elex.common.component.task.quartz.ITaskConfig;
import com.elex.common.component.task.quartz.ITaskTypeConfig;
import com.elex.common.service.IService;

/**
 * 任务服务接口
 * 
 * @author mausmars
 *
 */
public interface ITaskService extends IService {
	/**
	 * 插入任务
	 * 
	 * @param task
	 * @param taskType
	 * @return
	 */
	boolean insertTask(ITimingTask task, ITaskTypeConfig taskType);

	/**
	 * 插入任务
	 * 
	 * @param task
	 * @param taskType
	 * @param config
	 * @return
	 */
	boolean insertTask(ITimingTask task, ITaskTypeConfig taskType, ITaskConfig config);

	/**
	 * 改变任务时间
	 * 
	 * @param taskId
	 * @param taskType
	 * @return
	 */
	boolean changeTaskTime(String taskId, ITaskTypeConfig taskType);

	/**
	 * 移除任务
	 * 
	 * @param taskId
	 */
	void removeTask(String taskId);
}
