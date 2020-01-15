package com.elex.im.module.servertest.test.zookeeper;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.zookeeper.IZookeeperService;
import com.elex.common.service.configloader.LocalServiceConfigLoader;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.initservice.InitServiceFilter;
import com.elex.common.service.type.ServiceType;
import com.elex.im.module.servertest.TestService;
import com.elex.im.module.servertest.constant.TestConstant;
import com.elex.im.module.servertest.rpcservice.TestLocalService;

/**
 * 测试rpc服务01，和02做配合
 * 
 * @author mausmars
 *
 */
public class StartZookeeperTest {
	public static void main(String[] args) throws Exception {
		// 配置加载器
		TestLocalService testLocalService = new TestLocalService();
		// {对象id:本地服务}，这个要和配置对应上

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

		IZookeeperService zookeeperService = serverService.getServiceManager().getService(ServiceType.zookeeper,
				TestConstant.TestScriptServiceIdKey);

		String path = "";
		Stat stat = zookeeperService.exists("/test_zk", false);
		if (stat == null) {
			path = zookeeperService.create("/test_zk", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println(path);
		} else {
			System.out.println(stat);
		}

		stat = zookeeperService.exists("/test_zk/test1", false);
		if (stat == null) {
			path = zookeeperService.create("/test_zk/test1", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println(path);
		} else {
			System.out.println(stat);
		}

		stat = zookeeperService.exists("/test_zk/test1/zk1", false);
		if (stat == null) {
			path = zookeeperService.create("/test_zk/test1/zk1", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println(path);
		} else {
			System.out.println(stat);
		}

		List<String> children = zookeeperService.getChildren("/test_zk", false);
		System.out.println(children);
	}
}
