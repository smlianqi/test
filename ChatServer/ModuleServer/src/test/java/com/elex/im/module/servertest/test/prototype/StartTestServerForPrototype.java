package com.elex.im.module.servertest.test.prototype;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.prototype.IPrototypeService;
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
public class StartTestServerForPrototype {
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
		testPrototype(serverService);
	}

	// 测试rpc
	private static void testPrototype(TestService serverService) {
		IPrototypeService prototypeService = (IPrototypeService) serverService.getServiceManager()
				.getService(ServiceType.prototype, TestConstant.TestPrototypeServiceIdKey);

	}
}
