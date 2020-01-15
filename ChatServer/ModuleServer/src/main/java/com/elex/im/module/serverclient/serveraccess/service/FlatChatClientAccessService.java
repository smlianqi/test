package com.elex.im.module.serverclient.serveraccess.service;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.net.client.INetClientService;
import com.elex.common.component.net.config.NetCustomConfig;
import com.elex.common.component.player.IPlayerFactory;
import com.elex.common.component.player.service.DefaultPlayerFactory;
import com.elex.common.component.player.service.DefaultPlayerMgrService;
import com.elex.common.component.player.type.UserType;
import com.elex.common.message.FlatMessageCreater;
import com.elex.common.message.IMessageCreater;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.service.netty.INetNettyClient;
import com.elex.common.net.session.ISession;
import com.elex.common.net.session.ISessionPool;
import com.elex.common.net.session.SessionPool;
import com.elex.common.service.configloader.LocalServiceConfigLoader;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.initservice.InitServiceFilter;
import com.elex.common.service.type.ServiceType;
import com.elex.im.message.AModuleMessageCreater;
import com.elex.im.message.FlatModuleMessageCreater;
import com.elex.im.module.listener.IClientConnectListener;
import com.elex.im.module.serverchat.module.chat.type.MemberModifyType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;
import com.elex.im.module.serverclient.imitateclient.ClientConstant;
import com.elex.im.module.serverclient.request.SendMultiChatReq;
import com.elex.im.module.serverclient.request.SendSingleChatReq;
import com.elex.im.module.serverclient.request.SendWorldChatReq;
import com.elex.im.module.serverclient.serveraccess.ChatClientConstant;
import com.elex.im.module.serverclient.serveraccess.ChatClientService;
import com.elex.im.module.serverclient.serveraccess.module.ChatClient2GateHandlerConfig;
import com.elex.im.module.translation.type.LanguageType;

/**
 * 客户端服务
 * 
 * @author mausmars
 *
 */
public class FlatChatClientAccessService implements IChatClientAccessService {
	private ISessionPool sessionPool;

	// --------------------------
	private String host;
	private int port;
	private int connectCount;
	private String token;
	private IFlatReqCallBack callBack;

	private IPlayerFactory playerFactory;

	private String serverId;

	private ChatClientService serverService;

	private IMessageCreater messageCreater;
	private AModuleMessageCreater moduleMessageCreater;

	private IClientConnectListener clientConnectListener;

	public FlatChatClientAccessService(String serverId, IClientConnectListener clientConnectListener) {
		this.serverId = serverId;
		this.clientConnectListener = clientConnectListener;
	}

	@Override
	public void startup() {
		// 绑定完毕才可以发送
		CountDownLatch sessionBindingCountDownLatch = new CountDownLatch(connectCount);

		messageCreater = new FlatMessageCreater();
		moduleMessageCreater = new FlatModuleMessageCreater();

		ChatClientService serverService = new ChatClientService(FunctionType.client);
		NetCustomConfig netCustomConfig = new NetCustomConfig();
		netCustomConfig.insertMessageCreater(messageCreater);
		netCustomConfig.insertModuleMessageCreater(moduleMessageCreater);

		if (playerFactory == null) {
			DefaultPlayerFactory playerFactory = new DefaultPlayerFactory();
			playerFactory.setPlayerManager(new DefaultPlayerMgrService());
			playerFactory.setContext(serverService);
			this.playerFactory = playerFactory;
		}

		// 设置处理器
		ChatClient2GateHandlerConfig client2GateHandlerConfig = new ChatClient2GateHandlerConfig();
		client2GateHandlerConfig.setCallBack(callBack);
		// client2GateHandlerConfig.setSessionBindingCountDownLatch(sessionBindingCountDownLatch);
		client2GateHandlerConfig.setPlayerFactory(playerFactory);
		client2GateHandlerConfig.createMessageConfigs(serverService);
		client2GateHandlerConfig.setListener(clientConnectListener);

		// 设置player工厂
		netCustomConfig.putNetHandlerConfig(ChatClientConstant.GateClientNetServerIdKey, client2GateHandlerConfig);

		// 配置加载器
		LocalServiceConfigLoader serviceConfigLoader = new LocalServiceConfigLoader();
		serviceConfigLoader.putCustomConfig(netCustomConfig);// 网络通信配置

		// 处理链
		DefaultFilterChain initFilterChain = new DefaultFilterChain("init_chain");
		initFilterChain.insertNodeToLast(new InitServiceFilter());

		serverService.setModuleServiceConfig(client2GateHandlerConfig);
		serverService.setInitFilterChain(initFilterChain);
		serverService.setServiceConfigLoader(serviceConfigLoader);

		// 启动
		serverService.startup(false);

		this.serverService = serverService;

		// 获取client服务
		INetClientService netClientService = serverService.getServiceManager().getService(ServiceType.netclient,
				ClientConstant.GateClientNetServerIdKey);

		// 创建五个连接的池
		SessionPool sessionPool = new SessionPool(connectCount);
		for (int i = 0; i < connectCount; i++) {
			// 创建连接，就会绑定session管理
			INetNettyClient netClient = netClientService.createNetClient(host, port);
			ISession session = netClient.getSession();
			// 绑定
			sessionPool.bindingClient(i, serverId, token, session);
		}

		// 发送绑定消息
		sendBindingSocketUp(sessionPool);

		try {
			// 绑定完毕才可以发送
			sessionBindingCountDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.sessionPool = sessionPool;

	}

	@Override
	public void shutdown() {
		// 关闭服务
		this.serverService.shutdown();
	}

	@Override
	public void sendUserLogin(long userId, UserType userType, LanguageType languageType) {
		IMessage msg = moduleMessageCreater.createUserLoginUp(userId, userType.ordinal(), languageType.getIsoCode());
		sendMessage(userId, msg);
	}

	@Override
	public void sendUserLogout(long userId) {
		IMessage msg = moduleMessageCreater.createUserLogoutUp(userId);
		sendMessage(userId, msg);
	}

	@Override
	public void sendModifyLanguage(long userId, LanguageType languageType) {
		IMessage msg = moduleMessageCreater.createModifyLanguageUp(userId, languageType.getIsoCode());
		sendMessage(userId, msg);
	}

	@Override
	public void sendWorldChatMessage(long userId, SendWorldChatReq req) {
		IMessage msg = moduleMessageCreater.createSendWorldChatMessageUp(userId, req);
		sendMessage(userId, msg);
	}

	@Override
	public void sendSingleChatMessage(long userId, SendSingleChatReq req) {
		IMessage msg = moduleMessageCreater.createSendSingleChatMessageUp(userId, req);
		sendMessage(userId, msg);
	}

	@Override
	public void sendMultiChatMessage(long userId, SendMultiChatReq req) {
		IMessage msg = moduleMessageCreater.createSendMultiChatMessageUp(userId, req);
		sendMessage(userId, msg);
	}

	@Override
	public void sendCreateChatRoom(long userId, RoomType roomType, String roomId, String admin, List<Long> members) {
		IMessage msg = moduleMessageCreater.createCreateChatRoomUp(userId, roomType.ordinal(), roomId, admin, members);
		sendMessage(userId, msg);
	}

	@Override
	public void sendManagerChatRoomMember(long userId, String roomId, MemberModifyType memberModifyType,
			List<Long> members) {
		IMessage msg = moduleMessageCreater.createManagerChatRoomMemberUp(userId, roomId, memberModifyType, members);
		sendMessage(userId, msg);
	}

	@Override
	public void sendTranslationMessage(long userId, String roomId, int roomType, long order) {
		IMessage msg = moduleMessageCreater.createTranslationMessageUp(userId, roomId, roomType, order);
		sendMessage(userId, msg);
	}

	private void sendBindingSocketUp(ISessionPool sessionPool) {
		for (int i = 0; i < sessionPool.size(); i++) {
			IMessage msg = messageCreater.createBindingSocketUpMessage(serverId, token, i);
			// 发送消息
			sessionPool.send(i, msg);
		}
	}

	@Override
	public void sendModifyLanguage(long userId, Object message) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendChatMessage(long userId, Object message) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendGainChatMessage(long userId, Object message) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override public void sendLastMessageOrderInChat(long userId, Object message) throws Exception {

	}

	@Override
	public void sendCreateChatRoom(long userId, Object message) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendManagerChatRoomMember(long userId, Object message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendTranslationMessage(long userId, Object message) throws Exception {
		// TODO Auto-generated method stub

	}

	// ----------------------------------------
	private void sendMessage(long userId, IMessage message) {
		if (sessionPool == null) {
			return;
		}
		// 池发送消息
		sessionPool.send(String.valueOf(userId), message);
	}

	// ----------------------------------------
	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setConnectCount(int connectCount) {
		this.connectCount = connectCount;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setCallBack(IFlatReqCallBack callBack) {
		this.callBack = callBack;
	}

	public void setPlayerFactory(IPlayerFactory playerFactory) {
		this.playerFactory = playerFactory;
	}

	@Override
	public void sendGainAllNewestChatMessage(long userId, long clienSendedTime, String regionRoomId,
			String unionRoomId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendGainRoomMultiChatMessage(long userId, long clienSendedTime, String roomId, RoomType roomType,
			List<Long> orders) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendGainRoomPageChatMessage(long userId, long clienSendedTime, String roomId, RoomType roomType,
			long order, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendGainRoomNewestChatMessage(long userId, long clienSendedTime, String roomId, RoomType roomType) {
		// TODO Auto-generated method stub

	}
}
