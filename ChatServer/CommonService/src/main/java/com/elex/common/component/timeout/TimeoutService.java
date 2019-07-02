package com.elex.common.component.timeout;

import java.util.List;

import com.elex.common.component.task.ITaskService;
import com.elex.common.component.task.quartz.ITaskTypeConfig;
import com.elex.common.component.task.quartz.cron.CronTypeConfig;
import com.elex.common.component.timeout.config.ScTimeout;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceType;

/**
 * 超时服务实现
 * 
 * @author mausmars
 *
 */
public class TimeoutService extends AbstractService<ScTimeout> implements ITimeoutService {
	private ITaskService taskService;

	public TimeoutService(IServiceConfig serviceConfig, IGlobalContext context) {
		super(serviceConfig, context);
	}

	@Override
	public void initService() throws Exception {
		ScTimeout cz = getScTimeout();
		List<String> sids = cz.getDependIdsMap().get(ServiceType.task.name());
		// 加入依赖服务
		taskService = getServiceManager().getService(ServiceType.task, sids.get(0));
	}

	private ScTimeout getScTimeout() {
		return getSConfig();
	}

	@Override
	public void startupService() throws Exception {
		taskService.startup();
	}

	@Override
	public void shutdownService() throws Exception {
		taskService.shutdown();
		taskService = null;
	}

	@Override
	public IServiceConfig getConfig() {
		return serviceConfig;
	}

	public ITimeoutManager createTask(String key) {
		return createTask(key, getScTimeout().getExpression());
	}

	@Override
	public ITimeoutManager createTask(String key, String expression) {
		// 如果服务状态不是启动完成
		checkServiceUseState();

		TimeoutTask task = new TimeoutTask(key);
		task.setTimeout(getScTimeout().getTimeout());
		ITaskTypeConfig taskConfig = new CronTypeConfig(0, 0, expression, 0);
		taskService.insertTask(task, taskConfig);
		return task;
	}

	@Override
	public void removeTask(String key) {
		taskService.removeTask(key);
	}
}
