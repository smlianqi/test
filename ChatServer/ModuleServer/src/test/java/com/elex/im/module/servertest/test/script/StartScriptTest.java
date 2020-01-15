package com.elex.im.module.servertest.test.script;

import java.io.File;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.script.IScriptEngine;
import com.elex.common.component.script.IScriptService;
import com.elex.common.component.script.type.ScriptType;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.service.configloader.LocalServiceConfigLoader;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.initservice.InitServiceFilter;
import com.elex.common.service.type.ServiceType;
import com.elex.common.util.file.FileUtil;
import com.elex.im.module.servertest.TestService;
import com.elex.im.module.servertest.constant.TestConstant;
import com.elex.im.module.servertest.rpcservice.TestLocalService;
import com.elex.im.module.servertest.test.script.bean.TestBean;

/**
 * 测试rpc服务01，和02做配合
 * 
 * @author mausmars
 *
 */
public class StartScriptTest {
	public static void main(String[] args) {
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

		testService(serverService);
	}

	private static void testService(TestService serverService) {
		// testJnlua(serverService);
		System.out.println("===========================================");
		// testJs(serverService);
		System.out.println("===========================================");
		testLuaj(serverService);
	}

	private static void testJnlua(TestService serverService) {
		IScriptService scriptService = serverService.getServiceManager().getService(ServiceType.script,
				TestConstant.TestScriptServiceIdKey);

		Thread[] threads = new Thread[20];
		for (int i = 0; i < threads.length; i++) {
			IScriptEngine scriptEngine = scriptService.createAndGetEngine("test", ScriptType.jnlua);
			String content = FileUtil.readFileContent(new File("test_script/script_test.lua"), true);
			scriptEngine.resetConfig(true, content, null);
			IScriptTest scriptTest = scriptEngine.getInterface(IScriptTest.class);

			Thread thread = new Thread() {
				public void run() {
					for (int k = 0; k < 100; k++) {
						HeroBean heroBean = new HeroBean();
						heroBean.setId(11);
						heroBean.setLevel(11);
						heroBean.setName("test11");

						long startTime = System.currentTimeMillis();
						HeroBean v = scriptTest.printTest(Integer.parseInt(Thread.currentThread().getName()), k,
								heroBean);
						long temp = System.currentTimeMillis() - startTime;
						if (temp > 0) {
							System.out.println(temp + " ms");
						}
						System.out.println("v=" + v);
					}
				}
			};
			thread.setName(String.valueOf(i));
			threads[i] = thread;
		}

		for (Thread thread : threads) {
			thread.start();
		}
	}

	private static void testJs(TestService serverService) {
		IScriptService scriptService = serverService.getServiceManager().getService(ServiceType.script,
				TestConstant.TestScriptServiceIdKey);

		Thread[] threads = new Thread[20];
		for (int i = 0; i < threads.length; i++) {
			IScriptEngine scriptEngine = scriptService.createAndGetEngine("test", ScriptType.js);
			String content = FileUtil.readFileContent(new File("test_script/script_test.js"), true);
			scriptEngine.resetConfig(true, content, null);
			IScriptTest scriptTest = scriptEngine.getInterface(IScriptTest.class);

			Thread thread = new Thread() {
				public void run() {
					for (int k = 0; k < 100; k++) {
						HeroBean heroBean = new HeroBean();
						heroBean.setId(11);
						heroBean.setLevel(11);
						heroBean.setName("test11");

						long startTime = System.currentTimeMillis();
						HeroBean v = scriptTest.printTest(Integer.parseInt(Thread.currentThread().getName()), k,
								heroBean);
						long temp = System.currentTimeMillis() - startTime;
						if (temp > 0) {
							System.out.println(temp + " ms");
						}
					}
				}
			};
			thread.setName(String.valueOf(i));
			threads[i] = thread;
		}
		for (Thread thread : threads) {
			thread.start();
		}
	}

	private static void testLuaj(TestService serverService) {
		IScriptService scriptService = serverService.getServiceManager().getService(ServiceType.script,
				TestConstant.TestScriptServiceIdKey);

		Thread[] threads = new Thread[20];
		for (int i = 0; i < threads.length; i++) {
			IScriptEngine scriptEngine = scriptService.createAndGetEngine("test", ScriptType.luaj);
			String content = FileUtil.readFileContent(new File("test_script/script_test.lua"), true);
			scriptEngine.resetConfig(true, content, null);
			IScriptTest scriptTest = scriptEngine.getInterface(IScriptTest.class);

			Thread thread = new Thread() {
				public void run() {
					for (int k = 0; k < 100; k++) {
						HeroBean heroBean = new HeroBean();
						heroBean.setId(11);
						heroBean.setLevel(11);
						heroBean.setName("test11");

						heroBean.setMessageConfig(new MessageConfig());
						heroBean.setTestBean(new TestBean());

						long startTime = System.currentTimeMillis();
						HeroBean v = scriptTest.printTest(Integer.parseInt(Thread.currentThread().getName()), k,
								heroBean);
						long temp = System.currentTimeMillis() - startTime;
						if (temp > 0) {
							System.out.println(temp + " ms");
							System.out.println("被修改的level值 level=" + v.getLevel());
						}
					}
				}
			};
			thread.setName(String.valueOf(i));
			threads[i] = thread;
		}
		for (Thread thread : threads) {
			thread.start();
		}

	}
}
