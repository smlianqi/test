package com.elex.common.component.task.quartz.cron;

import com.elex.common.component.task.quartz.ITaskTypeConfig;
import com.elex.common.component.task.quartz.TaskConfigType;

/**
 * 时钟任务类型
 * 
 * @author mausmars
 * 
 */
public class CronTypeConfig implements ITaskTypeConfig {
	private long startTime; // 开始时间
	private long overTime; // 结束时间
	private String cornExpression; // 表达式
	private int misfireHander;

	public CronTypeConfig() {
	}

	public CronTypeConfig(long startTime, long overTime, String cornExpression, int misfireHander) {
		this.startTime = startTime;
		this.overTime = overTime;
		this.cornExpression = cornExpression;
		this.misfireHander = misfireHander;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getOverTime() {
		return overTime;
	}

	public void setOverTime(long overTime) {
		this.overTime = overTime;
	}

	public String getCornExpression() {
		return cornExpression;
	}

	public void setCornExpression(String cornExpression) {
		this.cornExpression = cornExpression;
	}

	public int getMisfireHander() {
		return misfireHander;
	}

	public void setMisfireHander(int misfireHander) {
		this.misfireHander = misfireHander;
	}

	@Override
	public TaskConfigType getTaskConfigType() {
		return TaskConfigType.CronConfig;
	}
}
