package com.elex.common.component.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.elex.common.component.task.config.ScTask;
import com.elex.common.component.task.quartz.ITaskConfig;
import com.elex.common.component.task.quartz.ITaskTypeConfig;
import com.elex.common.component.task.quartz.TaskConfigType;
import com.elex.common.component.task.quartz.cron.CronTypeConfig;
import com.elex.common.component.task.quartz.simple.SimpleTypeConfig;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;

/**
 * jdk实现定时任务
 * 
 * @author mausmars
 *
 */
public class ScheduledTaskService extends AbstractService<ScTask> implements ITaskService {
	private ScheduledExecutorService executor;

	public ScheduledTaskService(IServiceConfig serviceConfig, IGlobalContext context) {
		super(serviceConfig, context);
	}

	@Override
	public void initService() throws Exception {
		executor = Executors.newScheduledThreadPool(10);
	}

	@Override
	public void startupService() throws Exception {
	}

	@Override
	public void shutdownService() throws Exception {
		executor.shutdownNow();
	}

	@Override
	public boolean insertTask(ITimingTask task, ITaskTypeConfig taskType) {
		TaskConfigType taskConfigType = taskType.getTaskConfigType();
		switch (taskConfigType) {
		case SimpleConfig: {
			SimpleTypeConfig simpleTypeTask = (SimpleTypeConfig) taskType;
			if (simpleTypeTask.getIntervalTime() > 0) {
				// 时间间隔
			}
			if (simpleTypeTask.getRepeatCount() > 0) {
				// 重复次数
			} else if (simpleTypeTask.getRepeatCount() < 0) {
				// 永远循环
			}
			break;
		}
		case CronConfig: {
			CronTypeConfig cronTypeTask = (CronTypeConfig) taskType;
			break;
		}
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean insertTask(ITimingTask task, ITaskTypeConfig taskType, ITaskConfig config) {
		return false;
	}

	@Override
	public boolean changeTaskTime(String taskId, ITaskTypeConfig taskType) {
		return false;
	}

	@Override
	public void removeTask(String taskId) {
	}
}
