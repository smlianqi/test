package com.elex.common.component.task;

import com.elex.common.component.task.quartz.TaskContext;

/**
 * 任务接口，用作定时任务接口
 * 
 * @author mausmars
 *
 */
public interface ITimingTask {
	/**
	 * 任务key
	 * 
	 * @return
	 */
	String getTimingKey();

	/**
	 * 任务执行
	 * 
	 * @param context
	 */
	void execute(TaskContext context);
}
