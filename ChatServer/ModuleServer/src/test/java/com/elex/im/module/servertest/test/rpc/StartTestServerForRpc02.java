package com.elex.im.module.servertest.test.rpc;

import com.elex.common.component.function.IFunctionCluster;
import com.elex.common.component.function.IFunctionService;
import com.elex.common.component.function.info.IFunctionInfo;
import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.rpc.config.RpcCustomConfig;
import com.elex.common.service.configloader.LocalServiceConfigLoader;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.initservice.InitServiceFilter;
import com.elex.common.service.type.ServiceType;
import com.elex.im.module.servertest.TestService;
import com.elex.im.module.servertest.constant.TestConstant;
import com.elex.im.module.servertest.rpcservice.ITestService;
import com.elex.im.module.servertest.rpcservice.TestLocalService;
import com.elex.im.module.servertest.rpcservice.bean.TestRpcBean;

/**
 * 启动战场服务，临时测试服务
 * 
 * @author mausmars
 *
 */
public class StartTestServerForRpc02 {
	public static void main(String[] args) {
		// 配置加载器
		RpcCustomConfig rpcCustomConfig = new RpcCustomConfig();
		rpcCustomConfig.putLocalService("Default_Service_OID", new TestLocalService());

		LocalServiceConfigLoader serviceConfigLoader = new LocalServiceConfigLoader();
		serviceConfigLoader.putCustomConfig(rpcCustomConfig);

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
		testRpc(serverService);
	}

	// 测试rpc
	private static void testRpc(TestService serverService) {
		IFunctionService functionService = serverService.getServiceManager().getService(ServiceType.function,
				TestConstant.TestFunctionServiceIdKey);
		IFunctionCluster functionCluster = functionService.getFunctionCluster("slgx1", "r1", FunctionType.test);

		for (int i = 0; i < 1000; i++) {
			IFunctionInfo functionInfo = functionCluster.getRandomFunctionInfo();
			ITestService testService = functionInfo.getService(FServiceType.IceRpc, ITestService.class.getSimpleName());

			TestRpcBean testRpcBean = new TestRpcBean();
			testRpcBean.setId(111);
			testRpcBean.setName("mali_2222222222");
			testRpcBean.setSex(1);

			try {
				testService.insert(testRpcBean);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
