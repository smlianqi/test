package com.elex.im.module.servertest.test.member;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.rpc.config.RpcCustomConfig;
import com.elex.common.service.configloader.LocalServiceConfigLoader;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.initservice.InitServiceFilter;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.module.servertest.TestService;
import com.elex.im.module.servertest.rpcservice.TestLocalService;

/**
 * 测试成员定位服务
 * 
 * @author mausmars
 *
 */
public class StartTestServerForMember {
	protected static final ILogger logger = XLogUtil.logger();

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
		// testMember(serverService);
	}

	// 测试member
	// private static void testMember(TestService serverService) {
	// IFunctionService functionService =
	// serverService.getServiceManager().getService(ServiceType.function, "1");
	//
	// IFunctionCluster functionCluster =
	// functionService.getFunctionCluster("slgx1", "r1", FunctionType.test);
	//
	// IFunctionInfo functionInfo = functionCluster.getRandomFunctionInfo();
	// FunctionId functionId = functionInfo.getFunctionId();
	//
	// IMemberService memberService =
	// serverService.getServiceManager().getService(ServiceType.member, "1");
	// Member member = new Member("mali", MemberType.test);
	// int version = 2;
	// MemberNetsite memberNetsite = new MemberNetsite(version,
	// System.currentTimeMillis(), functionId);
	//
	// // 删除
	// memberService.removeMemberNetsite(member);
	//
	// // 插入位置
	// MemberNetsite mn = memberService.insertMemberNetsite(member, memberNetsite);
	// if (logger.isDebugEnabled()) {
	// logger.debug("fid=" + mn.getFunctionId().getFid(true));
	// }
	//
	// IFunctionInfo fi = memberService.locate("slgx1", "r1", MemberType.test,
	// "mali", false);
	// String fid = fi.getFunctionId().getFid(true);
	// if (logger.isDebugEnabled()) {
	// logger.debug("fid=" + fid);
	// }
	// }
}
