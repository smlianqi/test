package com.elex.common.component.task;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.elex.common.component.task.config.ScTask;
import com.elex.common.component.task.quartz.DefaultTaskConfig;
import com.elex.common.component.task.quartz.ITaskConfig;
import com.elex.common.component.task.quartz.ITaskTypeConfig;
import com.elex.common.component.task.quartz.QuartzJob;
import com.elex.common.component.task.quartz.TaskConfigType;
import com.elex.common.component.task.quartz.TaskContext;
import com.elex.common.component.task.quartz.cron.CronTypeConfig;
import com.elex.common.component.task.quartz.cron.TaskMisFireType;
import com.elex.common.component.task.quartz.simple.SimpleTypeConfig;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceStateType;
import com.elex.common.util.time.TimeUtil;

/**
 * Quartz任务服务
 * 
 * @author mausmars
 *
 */
public class QuartzTaskService extends AbstractService<ScTask> implements ITaskService {
	private String JobGroupKey = "JobGroup";
	private String TriggerGroupKey = "TriggerGroup";
	private String ConfigfilePathKey = "ConfigfilePath";

	private String jobGroup;
	private String triggerGroup;

	private Scheduler sched;

	public QuartzTaskService(IServiceConfig sc, IGlobalContext context) {
		super(sc, context);
	}

	@Override
	public void initService() throws Exception {
		jobGroup = getSConfig().getExtraParamsMap().get(JobGroupKey);
		triggerGroup = getSConfig().getExtraParamsMap().get(TriggerGroupKey);
		String configfilePath = getSConfig().getExtraParamsMap().get(ConfigfilePathKey);

		SchedulerFactory sf = null;
		if (configfilePath != null) {
			sf = new StdSchedulerFactory(configfilePath);
		} else {
			sf = new StdSchedulerFactory();
		}
		sched = sf.getScheduler();
	}

	@Override
	public void startupService() throws Exception {
		sched.start();
	}

	@Override
	public void shutdownService() throws Exception {
		if (sched != null) {
			sched.shutdown(true);
		}
	}

	@Override
	public boolean insertTask(ITimingTask task, ITaskTypeConfig taskType) {
		if (state != ServiceStateType.Started) {
			return false;
		}
		return insertTask(task, taskType, DefaultTaskConfig.DefaultTaskConfig);
	}

	@Override
	public boolean insertTask(ITimingTask task, ITaskTypeConfig taskType, ITaskConfig config) {
		if (state != ServiceStateType.Started) {
			return false;
		}
		try {
			if (config == null) {
				config = DefaultTaskConfig.DefaultTaskConfig;
			}

			Trigger trigger = createTrigger(task.getTimingKey(), taskType);
			if (trigger == null) {
				return false;
			}
			TaskContext taskContext = new TaskContext(taskType, config);

			JobDetail job = JobBuilder.newJob(QuartzJob.class).withIdentity(task.getTimingKey(), jobGroup).build();
			// 设置参数
			job.getJobDataMap().put(QuartzJob.TaskContext, taskContext);
			job.getJobDataMap().put(QuartzJob.TaskKey, task);

			Date startDate = sched.scheduleJob(job, trigger);
			// 手动启动
			// sched.triggerJob(JobKey.jobKey("job8", "group1"));

			// 打印日志
			if (trigger instanceof SimpleTrigger) {
				SimpleTrigger t = (SimpleTrigger) trigger;
				if (logger.isInfoEnabled()) {
					StringBuilder sb = new StringBuilder();
					sb.append("[QuartzTask] ");
					sb.append(job.getKey());
					sb.append(" start_time at: ");
					sb.append(startDate);
					sb.append(" and	 repeat_count: ");
					sb.append(t.getRepeatCount());
					sb.append(" times, repeat_interval: ");
					sb.append(t.getRepeatInterval() / TimeUtil.MillisecondsPerSeconds);
					sb.append(" seconds");
					logger.info(sb.toString());
				}
			} else if (trigger instanceof CronTrigger) {
				CronTrigger t = (CronTrigger) trigger;
				if (logger.isInfoEnabled()) {
					StringBuilder sb = new StringBuilder();
					sb.append("[QuartzTask] ");
					sb.append(job.getKey());
					sb.append(" start_time at: ");
					sb.append(startDate);
					sb.append(" and cron_expression: ");
					sb.append(t.getCronExpression());
					logger.info(sb.toString());
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
	}

	@Override
	public boolean changeTaskTime(String taskId, ITaskTypeConfig taskType) {
		if (state != ServiceStateType.Started) {
			return false;
		}
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(taskId, triggerGroup);
			Trigger trigger = createTrigger(taskId, taskType);
			// 可以重新设置时间
			Date startDate = sched.rescheduleJob(triggerKey, trigger);
			if (logger.isInfoEnabled()) {
				logger.info("changeTaskTime to run at: " + startDate);
			}
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		return false;
	}

	@Override
	public void removeTask(String taskId) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(taskId, triggerGroup);
			sched.pauseTrigger(triggerKey);
			// 停止触发器
			sched.unscheduleJob(triggerKey);// 移除触发器
			sched.deleteJob(JobKey.jobKey(taskId, jobGroup));// 删除任务
		} catch (SchedulerException e) {
			logger.error("", e);
		}
	}

	private Trigger createTrigger(String taskId, ITaskTypeConfig taskType) {
		TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(taskId, triggerGroup);
		if (taskType.getStartTime() > 0) {
			// 开始时间，小于这个时间的都会开始
			triggerBuilder.startAt(new Date(taskType.getStartTime()));
		}
		if (taskType.getOverTime() > 0) {
			triggerBuilder.endAt(new Date(taskType.getOverTime()));
		}

		TaskConfigType taskConfigType = taskType.getTaskConfigType();
		if (taskConfigType == TaskConfigType.SimpleConfig) {
			SimpleTypeConfig simpleTypeTask = (SimpleTypeConfig) taskType;
			SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
			if (simpleTypeTask.getIntervalTime() > 0) {
				// 时间间隔
				simpleScheduleBuilder.withIntervalInMilliseconds(simpleTypeTask.getIntervalTime());
			}
			if (simpleTypeTask.getRepeatCount() > 0) {
				// 重复次数
				simpleScheduleBuilder.withRepeatCount(simpleTypeTask.getRepeatCount());
			} else if (simpleTypeTask.getRepeatCount() < 0) {
				// 永远循环
				simpleScheduleBuilder.repeatForever();
			}
			triggerBuilder.withSchedule(simpleScheduleBuilder);
		} else if (taskConfigType == TaskConfigType.CronConfig) {
			CronTypeConfig cronTypeTask = (CronTypeConfig) taskType;
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
					.cronSchedule(cronTypeTask.getCornExpression());
			if (cronTypeTask.getMisfireHander() > 0) {
				if (cronTypeTask.getMisfireHander() == TaskMisFireType.Cron_InstructionDoNothing.getValue()) {
					cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
				}
			}
			triggerBuilder.withSchedule(cronScheduleBuilder);
		} else {
			return null;
		}
		return triggerBuilder.build();
	}
}
