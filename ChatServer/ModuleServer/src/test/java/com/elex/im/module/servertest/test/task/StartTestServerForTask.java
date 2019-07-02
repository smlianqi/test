package com.elex.im.module.servertest.test.task;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.task.ITaskService;
import com.elex.common.component.task.quartz.simple.SimpleTypeConfig;
import com.elex.common.service.configloader.LocalServiceConfigLoader;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.initservice.InitServiceFilter;
import com.elex.common.service.type.ServiceType;
import com.elex.im.module.servertest.TestService;
import com.elex.im.module.servertest.constant.TestConstant;

/**
 * 测试rpc服务01，和02做配合
 * 
 * @author mausmars
 *
 */
public class StartTestServerForTask {
	public static void main(String[] args) {
		// 配置加载器
		LocalServiceConfigLoader serviceConfigLoader = new LocalServiceConfigLoader();

		// 处理链
		DefaultFilterChain initFilterChain = new DefaultFilterChain("init_chain");
		initFilterChain.insertNodeToLast(new InitServiceFilter());

		// 创建战场服务
		TestService serverService = new TestService(FunctionType.test);
		serverService.setInitFilterChain(initFilterChain);
		serverService.setServiceConfigLoader(serviceConfigLoader);

		// 启动
		serverService.startup(false);

		testService(serverService);
	}

	private static void testService(TestService serverService) {
		testTask(serverService);
	}

	// 测试rpc
	private static void testTask(TestService serverService) {
		TestTask task = new TestTask("test");

		long startTime = System.currentTimeMillis() + 60 * 1000;
		// 取消任务
		ITaskService taskService = serverService.getServiceManager().getService(ServiceType.task,
				TestConstant.TestTaskServiceIdKey);

		SimpleTypeConfig taskType1 = new SimpleTypeConfig(startTime, 0, 0, 0);
		taskService.insertTask(task, taskType1);

		// SimpleTypeConfig taskType2 = new SimpleTypeConfig(System.currentTimeMillis(),
		// 0, 0, 0);
		// taskService.changeTaskTime("test", taskType2);
	}
}
