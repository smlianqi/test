package com.elex.im.module.serverclient.imitateclient;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.net.client.INetClientService;
import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.IPlayerFactory;
import com.elex.common.component.player.service.DefaultPlayerFactory;
import com.elex.common.component.player.service.DefaultPlayerMgrService;
import com.elex.common.component.player.service.IPlayerMgrService;
import com.elex.common.component.player.type.UserType;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.service.netty.INetNettyClient;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.service.ServerService;
import com.elex.common.service.type.ServiceType;
import com.elex.common.util.file.FileUtil;
import com.elex.im.message.AModuleMessageCreater;
import com.elex.im.message.FlatModuleMessageCreater;
import com.elex.im.message.PrototModuleMessageCreater;
import com.elex.im.module.serverclient.imitateclient.module.flowchart.FlowChartType;
import com.elex.im.module.serverclient.imitateclient.module.usertestconfig.IUserTestConfigManager;
import com.elex.im.module.serverclient.imitateclient.module.usertestconfig.UserTestConfig;
import com.elex.im.module.serverclient.imitateclient.module.usertestconfig.UserTestConfigManager;
import com.elex.im.module.translation.type.LanguageType;

public class ClientService extends ServerService implements IClientContext {
	private Map<FunctionType, DefaultPlayerMgrService> playerManagerMap;

	private IUserTestConfigManager userTestConfigManager;

	private AModuleMessageCreater protoModuleMessageCreater = new PrototModuleMessageCreater();
	private AModuleMessageCreater flatModuleMessageCreater = new FlatModuleMessageCreater();

	public ClientService(FunctionType functionType) {
		super(functionType);
		playerManagerMap = new HashMap<>();
		userTestConfigManager = new UserTestConfigManager();
		init();
	}

	private void init() {
		initPlayerFactoryMananger(FunctionType.logic);
		initPlayerFactoryMananger(FunctionType.battle);

		// 加载配置
		initUserTestConfig();
	}

	// 临时的
	private void initUserTestConfig() {
		File file = new File("propertiesconfig/client/clientconfig.properties");
		Properties p = FileUtil.createProperties(file);

		String flowChartTypeStr = p.getProperty("flow_chart_type");
		String rid = p.getProperty("rid");

		FlowChartType flowChartType = FlowChartType.valueOf(flowChartTypeStr);
		switch (flowChartType) {
		case Single: {
			String name = p.getProperty("single_user_names");
			String className = p.getProperty("single_class_name");
			String params = p.getProperty("single_params");

			UserTestConfig userTestConfig = createUserTestConfig(0, name, rid, flowChartType);
			userTestConfig.putAttach("class_name", className);
			userTestConfig.putAttach("params", params);

			userTestConfigManager.insert(userTestConfig);
			break;
		}
		case FlowChart: {
			// TODO 如果走大厅这里的测试流程需要修改
			String countStr = p.getProperty("user_count");
			String name = p.getProperty("flowchart_user_names");

			int count = Integer.parseInt(countStr);
			for (int i = 1; i <= count; i++) {
				UserTestConfig utc = createUserTestConfig(i, name + i, rid, flowChartType);
				userTestConfigManager.insert(utc);
			}
			break;
		}
		case Battle: {
			String userId1 = p.getProperty("battle_userId1");
			String name1 = p.getProperty("battle_user_names1");
			UserTestConfig userTestConfig1 = createUserTestConfig(Long.parseLong(userId1), name1, rid, flowChartType);
			userTestConfigManager.insert(userTestConfig1);

			String userId2 = p.getProperty("battle_userId2");
			String name2 = p.getProperty("battle_user_names2");
			UserTestConfig userTestConfig2 = createUserTestConfig(Long.parseLong(userId2), name2, rid, flowChartType);
			userTestConfigManager.insert(userTestConfig2);

			// String name3 = p.getProperty("battle_user_names3");
			// UserTestConfig userTestConfig3 = createUserTestConfig(name3, rid,
			// flowChartType);
			// userTestConfigManager.insert(userTestConfig3);
			//
			// String name4 = p.getProperty("battle_user_names4");
			// UserTestConfig userTestConfig4 = createUserTestConfig(name4, rid,
			// flowChartType);
			// userTestConfigManager.insert(userTestConfig4);
			break;
		}
		default:
			break;
		}
	}

	private UserTestConfig createUserTestConfig(long userId, String name, String rid, FlowChartType flowChartType) {
		UserTestConfig userTestConfig = new UserTestConfig();
		userTestConfig.setFlowChartType(flowChartType);
		userTestConfig.setRid(rid);
		userTestConfig.setUserId(userId);
		userTestConfig.setName(name);
		return userTestConfig;
	}

	private void initPlayerFactoryMananger(FunctionType functionType) {
		DefaultPlayerMgrService playerManager = new DefaultPlayerMgrService();
		DefaultPlayerFactory playerFactory = new DefaultPlayerFactory();
		playerFactory.setPlayerManager(playerManager);
		playerFactory.setContext(this);

		playerManagerMap.put(functionType, playerManager);
	}

	/**
	 * 开始服务
	 */
	@Override
	public void startup(boolean isBlock) {
		if (logger.isInfoEnabled()) {
			logger.info("ProtoChatClientAccessService startup!");
		}
		// 调用父类启动流程
		super.startup(isBlock);

		// 开始测试流程
		startTest();
	}

	private void startTest() {
		INetClientService netClientService = getServiceManager().getService(ServiceType.netclient,
				ClientConstant.GateClientNetServerIdKey);
		for (UserTestConfig userTestConfig : userTestConfigManager.getUserTestConfigs()) {
			INetNettyClient netClient = netClientService.createNetClient();

			// 配置放入附件中
			netClient.setSessionAttachment(SessionAttachType.UserTestConfig, userTestConfig);

			SessionBox sessionBox = netClient.getSession().getAttach(SessionAttachType.SessionBox);
			IPlayerFactory playerFactory = sessionBox.getPlayerFactory();

			IPlayer player = playerFactory.createPlayer(userTestConfig.getUserId(), UserType.Client);
			// 设置用户类型
			ISession oldSession = player.getSession();
			if (oldSession == null) {
				player.replaceSession(netClient.getSession());
				// 添加映射关系
				sessionBox.bindingPlayer(player);
			}
			// 发送登录消息
			// sendFlatUserLoginUp(userTestConfig, player);
			sendProtoUserLoginUp(userTestConfig, player, LanguageType.en);
		}
	}

	private void sendFlatUserLoginUp(UserTestConfig userTestConfig, IPlayer player, LanguageType languageType) {
		IMessage msg = flatModuleMessageCreater.createUserLoginUp(userTestConfig.getUserId(),
				player.getUserType().ordinal(), languageType.getIsoCode());
		// 发送登录消息
		player.send(msg);
	}

	private void sendProtoUserLoginUp(UserTestConfig userTestConfig, IPlayer player, LanguageType languageType) {
		IMessage msg = protoModuleMessageCreater.createUserLoginUp(userTestConfig.getUserId(),
				player.getUserType().ordinal(), languageType.getIsoCode());
		// 发送登录消息
		player.send(msg);
	}

	@Override
	public IPlayerMgrService getPlayerManager(FunctionType functionType) {
		return playerManagerMap.get(functionType);
	}

	@Override
	public IUserTestConfigManager getUserTestConfigManager() {
		return userTestConfigManager;
	}
}
