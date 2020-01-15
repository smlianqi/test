package com.elex.im.module.servertest.test.ignite_mysql;

import java.util.List;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.ignite.IIgniteService;
import com.elex.common.component.ignite.config.IgniteServiceConfig;
import com.elex.common.component.ignite.config.ScIgnite;
import com.elex.common.service.configloader.LocalServiceConfigLoader;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.initservice.InitServiceFilter;
import com.elex.common.service.type.ServiceType;
import com.elex.im.module.servertest.TestService;
import com.elex.im.module.servertest.test.ignite_mysql.chatmessage.ChatMessage;
import com.elex.im.module.servertest.test.ignite_mysql.chatmessage.ChatMessageDao;

public class StartTestIgniteMysql {
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

		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void testService(TestService serverService) {
		IIgniteService service = serverService.getServiceManager().getService(ServiceType.ignite, "common");

		IgniteServiceConfig serviceConfig = service.getConfig();
		ScIgnite scIgnite = serviceConfig.getConfig();
		String swapDirectory = scIgnite.getSwapDirectory();
		// 创建分区配置
		// ChatMessageTestDao.createDataRegionConfiguration(swapDirectory);

		ChatMessageDao dao = new ChatMessageDao(service);

		 testInsert(dao);
//		selectByRoomIdAndCount(dao, "roomId_1", 7);
//		selectByRoomIdAndCount(dao, "roomId_1", 7);// 第二缓存了 就应该很快了

//		testSelectByRoomIdAndOrderPage(dao, "roomId_1", 5, 3);
//		testSelectByRoomIdAndOrderPage(dao, "roomId_1", 5, 3);// 第二缓存了 就应该很快了
//		testSelectByRoomIdAndOrderPage(dao, "roomId_1", 9, 3);// 查询条件变化看结果
	}

	private static void selectByRoomIdAndCount(ChatMessageDao dao, String roomId, int newMaxCount) {
		System.out.println("=== selectByRoomIdAndCount ===");

		long startTime = System.currentTimeMillis();
		List<ChatMessage> chatMessage2s = dao.selectByRoomIdAndCount(roomId, newMaxCount);
		System.out.println((System.currentTimeMillis() - startTime) + "ms");
		for (ChatMessage chatMessage2 : chatMessage2s) {
			System.out.println(chatMessage2);
		}
	}

	private static void testSelectByRoomIdAndOrderPage(ChatMessageDao dao, String roomId, long orderId, int pageCount) {
		System.out.println("=== testSelectByRoomIdAndOrderPage ===");
		long startTime = System.currentTimeMillis();
		List<ChatMessage> chatMessages = dao.selectByRoomIdAndOrderPage(roomId, orderId, pageCount);
		System.out.println((System.currentTimeMillis() - startTime) + "ms");

		for (ChatMessage chatMessage : chatMessages) {
			System.out.println(chatMessage);
		}
	}

	private static void testInsert(ChatMessageDao dao) {
		System.out.println("=== testInsert ===");
		long startTime = System.currentTimeMillis();
		for (int k = 0; k < 10; k++) {
			for (int i = 0; i < 10; i++) {
				ChatMessage entity = new ChatMessage();
				entity.setUid("userId_" + k);
				entity.setRoomId("roomId_" + k);
				entity.setOrderId(i);
				entity.setContentType(1);
				entity.setContent("content");
				entity.setReceivedTime(System.currentTimeMillis());
				entity.setSendedTime(System.currentTimeMillis());
				entity.setClientExt("");
				entity.setServerExt("");

				// 插入
				dao.insert(entity, null);
			}
		}
		System.out.println((System.currentTimeMillis() - startTime) + "ms");
	}
}
