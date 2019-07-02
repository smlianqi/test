package com.elex.im.module.servertest.test.threadpool;

import java.util.concurrent.TimeUnit;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.threadpool.IPoolExecutor;
import com.elex.common.component.threadpool.IThreadPoolService;
import com.elex.common.component.threadpool.task.IFuture;
import com.elex.common.service.configloader.LocalServiceConfigLoader;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.initservice.InitServiceFilter;
import com.elex.common.service.type.ServiceType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.module.servertest.TestService;
import com.elex.im.module.servertest.constant.TestConstant;

/**
 * 测试
 * 
 * @author mausmars
 *
 */
public class StartTestServerForThreadPool {
	protected static final ILogger logger = XLogUtil.logger();

	public static void main(String[] args) {
		// 配置加载器
		LocalServiceConfigLoader serviceConfigLoader = new LocalServiceConfigLoader();

		// 处理链
		DefaultFilterChain initFilterChain = new DefaultFilterChain("init_chain");
		initFilterChain.insertNodeToLast(new InitServiceFilter());

		// 创建服务
		TestService serverService = new TestService(FunctionType.test);
		serverService.setInitFilterChain(initFilterChain);
		serverService.setServiceConfigLoader(serviceConfigLoader);

		// 启动
		serverService.startup(false);

		testService(serverService);
	}

	private static void testService(TestService serverService) {
		// testTask1(serverService);
		testTask2(serverService);
	}

	private static void testTask2(TestService serverService) {
		IThreadPoolService threadPoolService = serverService.getServiceManager().getService(ServiceType.threadpool,
				TestConstant.TestThreadPoolServiceIdKey);

		IPoolExecutor poolExecutor = threadPoolService.getPoolExecutor();

		int id = 1;
		String key1 = "test_" + id + "_1";
		String key2 = "test_" + id + "_2";
		String groupKey = "groupTest_" + id;

		IFuture future1 = poolExecutor.schedule(new TestTask1(key1, groupKey), 1000, TimeUnit.MILLISECONDS);
		IFuture future2 = poolExecutor.schedule(new TestTask1(key1, groupKey), 6000, TimeUnit.MILLISECONDS);

		try {
			Object obje = future1.get();

			logger.debug("get() object =" + obje);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.debug("over!!!");
	}

	// 测试rpc
	private static void testTask1(TestService serverService) {
		int testCount = 2;
		// 取消任务
		IThreadPoolService threadPoolService = serverService.getServiceManager().getService(ServiceType.threadpool,
				TestConstant.TestThreadPoolServiceIdKey);

		IPoolExecutor poolExecutor = threadPoolService.getPoolExecutor();

		for (int i = 1; i < testCount; i++) {
			testTask(i, poolExecutor);
		}

		// 停止引擎
		poolExecutor.shutdown(false);

		for (;;) {
			if (poolExecutor.isTerminated()) {
				break;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.debug("Over TestQuartzTime!");
	}

	private static void testTask(int id, IPoolExecutor poolExecutor) {
		String key1 = "test_" + id + "_1";
		String key2 = "test_" + id + "_2";
		String groupKey = "groupTest_" + id;

		IFuture future1 = poolExecutor.submit(new TestTask1(key1, groupKey));
		IFuture future2 = poolExecutor.submit(new TestTask2(key2, groupKey));

		// 查看任务数量
		int size = poolExecutor.taskCount(groupKey);
		logger.debug("TaskCount! size=" + size);

		// 取消任务
		// IFuture future = poolExecutor.removeAndCancelTask(groupKey, key2, true);
		// logger.debug("CancelTask! isCancelled=" + future.isCancelled());

		// 查看任务数量
		size = poolExecutor.taskCount(groupKey);
		logger.debug("TaskCount! size=" + size);
	}
}
