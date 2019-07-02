package com.elex.im.module.serverclient.serveraccess.service;

import java.util.List;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.net.client.INetClientService;
import com.elex.common.component.net.config.NetCustomConfig;
import com.elex.common.component.player.IPlayerFactory;
import com.elex.common.component.player.service.DefaultPlayerFactory;
import com.elex.common.component.player.service.DefaultPlayerMgrService;
import com.elex.common.component.player.type.UserType;
import com.elex.common.message.IMessageCreater;
import com.elex.common.message.PrototMessageCreater;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.common.net.service.netty.INetNettyClient;
import com.elex.common.net.session.ISession;
import com.elex.common.net.session.ISessionPool;
import com.elex.common.net.session.SessionPool;
import com.elex.common.service.configloader.LocalServiceConfigLoader;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.initservice.InitServiceFilter;
import com.elex.common.service.type.ServiceType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.message.AModuleMessageCreater;
import com.elex.im.message.PrototModuleMessageCreater;
import com.elex.im.message.proto.ChatMessage.LastMessageOrderInChatUp;
import com.elex.im.message.proto.ChatMessage.CreateChatRoomUp;
import com.elex.im.message.proto.ChatMessage.GainChatMessageUp;
import com.elex.im.message.proto.ChatMessage.ManagerChatRoomMemberUp;
import com.elex.im.message.proto.ChatMessage.SendChatMessageUp;
import com.elex.im.message.proto.ChatMessage.TranslationMessageUp;
import com.elex.im.message.proto.UserMessage.ModifyLanguageUp;
import com.elex.im.module.listener.ClientConnectListener;
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
import com.google.protobuf.GeneratedMessageV3;

/**
 * 客户端服务
 * 
 * @author mausmars
 *
 */
public class ProtoChatClientAccessService implements IChatClientAccessService {
	protected static final ILogger logger = XLogUtil.logger();

	private volatile ISessionPool sessionPool;
	// --------------------------
	private String host;
	private int port;
	private int connectCount;
	private String token;
	private IProtoReqCallBack callBack;

	private IPlayerFactory playerFactory;

	private String serverId;

	private ChatClientService serverService;

	private IMessageCreater messageCreater;
	private AModuleMessageCreater moduleMessageCreater;
	// private CountDownLatch sessionBindingCountDownLatch;

	private IServiceStateCallBack cb;
	private IClientConnectListener clientConnectListener;

	public ProtoChatClientAccessService(String serverId) {
		this.serverId = serverId;
	}

	public ProtoChatClientAccessService(String serverId, IServiceStateCallBack cb,
			IClientConnectListener clientConnectListener) {
		this.serverId = serverId;
		this.cb = cb;
		this.clientConnectListener = clientConnectListener;
	}

	@Override
	public void startup() {
		init();
		if (cb != null) {
			Thread thread = new Thread() {
				public void run() {
					createSessionPool();
					cb.started();
				}
			};
			// 异步启动绑定连接
			thread.start();
		} else {
			createSessionPool();
		}
	}

	private void init() {
		messageCreater = new PrototMessageCreater();
		moduleMessageCreater = new PrototModuleMessageCreater();
		// sessionBindingCountDownLatch = new CountDownLatch(connectCount);

		ChatClientService serverService = new ChatClientService(FunctionType.client);
		IClientConnectListener clientConnectListener;
		if(this.clientConnectListener != null){
			clientConnectListener = this.clientConnectListener;

		}else{
			clientConnectListener = new ClientConnectListener(serverService);
		}
		
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
	}

	private void createSessionPool() {
		// 获取client服务
		INetClientService netClientService = serverService.getServiceManager().getService(ServiceType.netclient,
				ClientConstant.GateClientNetServerIdKey);

		// 创建五个连接的池
		SessionPool sp = new SessionPool(connectCount);
		for (int i = 0; i < connectCount; i++) {
			// 创建连接，就会绑定session管理
			INetNettyClient netClient = null;
			try {
				netClient = netClientService.createNetClient(host, port);
			} catch (Exception e) {
				logger.error("", e);
			}
			ISession session = netClient.getSession();
			// 绑定
			sp.bindingClient(i, serverId, token, session);
		}
		// 发送绑定消息
		sendBindingSocketUp(sp);

		sessionPool = sp;
	}

	@Override
	public void shutdown() {
		// 关闭服务
		this.serverService.shutdown();
	}

	private void sendBindingSocketUp(ISessionPool sessionPool) {
		for (int i = 0; i < sessionPool.size(); i++) {
			try {
				IMessage msg = messageCreater.createBindingSocketUpMessage(serverId, token, i);
				// 发送消息
				sessionPool.send(i, msg);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
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
	public void sendGainAllNewestChatMessage(long userId, long clienSendedTime, String regionRoomId,
			String unionRoomId) {
		IMessage msg = moduleMessageCreater.createGainAllNewestChatMessageUp(userId, clienSendedTime, regionRoomId,
				unionRoomId);
		sendMessage(userId, msg);
	}

	@Override
	public void sendGainRoomMultiChatMessage(long userId, long clienSendedTime, String roomId, RoomType roomType,
			List<Long> orders) {
		IMessage msg = moduleMessageCreater.createGainRoomMultiChatMessageUp(userId, clienSendedTime, roomId, roomType,
				orders);
		sendMessage(userId, msg);
	}

	@Override
	public void sendGainRoomPageChatMessage(long userId, long clienSendedTime, String roomId, RoomType roomType,
			long order, int count) {
		IMessage msg = moduleMessageCreater.createGainRoomPageChatMessageUp(userId, clienSendedTime, roomId, roomType,
				order, count);
		sendMessage(userId, msg);
	}

	@Override
	public void sendGainRoomNewestChatMessage(long userId, long clienSendedTime, String roomId, RoomType roomType) {
		IMessage msg = moduleMessageCreater.createGainRoomNewestChatMessageUp(userId, clienSendedTime, roomId,
				roomType);
		sendMessage(userId, msg);
	}

	@Override
	public void sendCreateChatRoom(long userId, RoomType roomType, String roomId, String admin, List<Long> members) {
		IMessage msg = moduleMessageCreater.createCreateChatRoomUp(userId, roomType.getValue(), roomId, admin, members);
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

	@Override
	public void sendModifyLanguage(long userId, Object message) throws Exception {
		GeneratedMessageV3 pm = (GeneratedMessageV3) message;
		ModifyLanguageUp m = ModifyLanguageUp.parseFrom(pm.toByteArray());

		IMessage msg = Message.createProtoMessage(m, userId);
		sendMessage(userId, msg);
	}

	@Override
	public void sendChatMessage(long userId, Object message) throws Exception {
		GeneratedMessageV3 pm = (GeneratedMessageV3) message;
		SendChatMessageUp m = SendChatMessageUp.parseFrom(pm.toByteArray());

		IMessage msg = Message.createProtoMessage(m, userId);
		sendMessage(userId, msg);
	}

	@Override
	public void sendGainChatMessage(long userId, Object message) throws Exception {
		GeneratedMessageV3 pm = (GeneratedMessageV3) message;
		GainChatMessageUp m = GainChatMessageUp.parseFrom(pm.toByteArray());

		IMessage msg = Message.createProtoMessage(m, userId);
		sendMessage(userId, msg);
	}

	@Override
	public void sendLastMessageOrderInChat(long userId, Object message) throws Exception {
		GeneratedMessageV3 pm = (GeneratedMessageV3) message;
		LastMessageOrderInChatUp m = LastMessageOrderInChatUp.parseFrom(pm.toByteArray());

		IMessage msg = Message.createProtoMessage(m, userId);
		sendMessage(userId, msg);
	}

	@Override
	public void sendCreateChatRoom(long userId, Object message) throws Exception {
		GeneratedMessageV3 pm = (GeneratedMessageV3) message;
		CreateChatRoomUp m = CreateChatRoomUp.parseFrom(pm.toByteArray());

		IMessage msg = Message.createProtoMessage(m, userId);
		sendMessage(userId, msg);
	}

	@Override
	public void sendManagerChatRoomMember(long userId, Object message) throws Exception {
		GeneratedMessageV3 pm = (GeneratedMessageV3) message;
		ManagerChatRoomMemberUp m = ManagerChatRoomMemberUp.parseFrom(pm.toByteArray());

		IMessage msg = Message.createProtoMessage(m, userId);
		sendMessage(userId, msg);
	}

	@Override
	public void sendTranslationMessage(long userId, Object message) throws Exception {
		GeneratedMessageV3 pm = (GeneratedMessageV3) message;
		TranslationMessageUp m = TranslationMessageUp.parseFrom(pm.toByteArray());

		IMessage msg = Message.createProtoMessage(m, userId);
		sendMessage(userId, msg);
	}

	// ----------------------------------------
	private void sendMessage(long userId, Object msg) {
		if (sessionPool == null) {
			logger.error("SessionPool == null, Service unavailable!!");
			throw new NullPointerException();
		}
		// 池发送消息
		sessionPool.send(String.valueOf(userId), msg);
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

	public void setCallBack(IProtoReqCallBack callBack) {
		this.callBack = callBack;
	}

	public void setPlayerFactory(IPlayerFactory playerFactory) {
		this.playerFactory = playerFactory;
	}
}
