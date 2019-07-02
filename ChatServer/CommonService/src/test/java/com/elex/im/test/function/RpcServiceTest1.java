package com.elex.im.test.function;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.service.ServerService;
import com.elex.common.service.configloader.LocalServiceConfigLoader;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.initservice.InitServiceFilter;

public class RpcServiceTest1 {
	public static void main(String[] args) {
		// 配置加载器
		LocalServiceConfigLoader serviceConfigLoader = new LocalServiceConfigLoader();

		// 处理链
		DefaultFilterChain initFilterChain = new DefaultFilterChain("init_chain");
		initFilterChain.insertNodeToLast(new InitServiceFilter());

		// 创建服务启动
		ServerService serverService = new ServerService(FunctionType.test);
		serverService.setInitFilterChain(initFilterChain);
		serverService.setServiceConfigLoader(serviceConfigLoader);
		// 启动
		serverService.startup(true);
	}
}
