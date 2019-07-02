package com.elex.common.component.task.quartz.simple;

import com.elex.common.component.task.quartz.ITaskTypeConfig;
import com.elex.common.component.task.quartz.TaskConfigType;

/**
 * 简单任务类型
 * 
 * @author mausmars
 * 
 */
public class SimpleTypeConfig implements ITaskTypeConfig {
	private long startTime; // 开始时间
	private long overTime; // 结束时间
	private int repeatCount; // 重复次数
	private int intervalTime; // 间隔时间

	public SimpleTypeConfig() {
	}

	public SimpleTypeConfig(long startTime, long overTime, int repeatCount, int intervalTime) {
		this.startTime = startTime;
		this.overTime = overTime;
		this.repeatCount = repeatCount;
		this.intervalTime = intervalTime;
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

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	@Override
	public TaskConfigType getTaskConfigType() {
		return TaskConfigType.SimpleConfig;
	}

}
