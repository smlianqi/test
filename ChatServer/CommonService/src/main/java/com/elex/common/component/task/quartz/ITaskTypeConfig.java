package com.elex.common.component.task.quartz;

/**
 * 任务类型配置
 * 
 * @author mausmars
 *
 */
public interface ITaskTypeConfig {
	/**
	 * 任务配置类型
	 * 
	 * @return
	 */
	TaskConfigType getTaskConfigType();

	/**
	 * 开始时间
	 * 
	 * @return
	 */
	long getStartTime();

	/**
	 * 结束时间
	 * 
	 * @return
	 */
	long getOverTime();
}
