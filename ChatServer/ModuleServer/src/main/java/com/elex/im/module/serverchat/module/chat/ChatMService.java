package com.elex.im.module.serverchat.module.chat;

import com.elex.common.component.ignite.IIgniteService;
import com.elex.common.component.member.data.MemberOnline;
import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.IPlayerInitOffline;
import com.elex.common.net.service.httpclient.ARequestCallback;
import com.elex.common.net.service.httpclient.HttpAsyncClient;
import com.elex.common.net.service.httpclient.HttpClientNetConfig;
import com.elex.common.net.type.NetProtocolType;
import com.elex.common.service.GeneralConstant;
import com.elex.common.service.module.AModuleService;
import com.elex.common.service.module.ModuleServiceType;
import com.elex.common.service.type.ServiceType;
import com.elex.im.data.DaoFactory;
import com.elex.im.data.chatmessage.ChatMessage;
import com.elex.im.data.chatmessage.IChatMessageDao;
import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.data.chatroom.IChatRoomDao;
import com.elex.im.data.chatroommember.ChatRoomMember;
import com.elex.im.data.chatroommember.IChatRoomMemberDao;
import com.elex.im.data.chatuser.ChatUser;
import com.elex.im.data.roomuseronline.IRoomUserOnlineDao;
import com.elex.im.data.roomuseronline.RoomUserOnline;
import com.elex.im.module.HandleErrorType;
import com.elex.im.module.common.error.IErrorMService;
import com.elex.im.module.common.user.IUserMService;
import com.elex.im.module.serverchat.IChatContext;
import com.elex.im.module.serverchat.module.HandeResult;
import com.elex.im.module.serverchat.module.chat.content.ContentFactory;
import com.elex.im.module.serverchat.module.chat.content.IContent;
import com.elex.im.module.serverchat.module.chat.room.*;
import com.elex.im.module.serverchat.module.chat.room.request.*;
import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.RoomMultiCondition;
import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.RoomNewestCondition;
import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.RoomPageCondition;
import com.elex.im.module.serverchat.module.chat.room.result.*;
import com.elex.im.module.serverchat.module.chat.type.MemberModifyType;
import com.elex.im.module.serverchat.module.chat.type.MemberStateType;
import com.elex.im.module.serverchat.module.chat.type.ResultStateType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;
import com.elex.im.module.serverchat.module.user.IChatUserMService;
import com.elex.im.module.translation.CokTranslationService;
import com.elex.im.module.translation.ITranslationService;
import com.elex.im.module.translation.type.LanguageType;

import java.util.*;

/**
 * 聊天服务模块
 * 
 * @author mausmars
 *
 */
public class ChatMService extends AModuleService implements IChatMService, IPlayerInitOffline {
	private IChatMessageDao chatMessageDao;
	private IChatRoomDao chatRoomDao;
	private IChatRoomMemberDao chatRoomMemberDao;
	private IRoomUserOnlineDao roomUserOnlineDao;

	private Map<RoomType, IChatRoomHandler> chatRoomHandlerMap = new HashMap<>();

	private ITranslationService translationService;

	private ContentFactory contentFactory = new ContentFactory();
	private DaoFactory daoFactory = new DaoFactory();

	public ChatMService(IChatContext context) {
		super(context);
	}

	@Override
	public ModuleServiceType getModuleServiceType() {
		return ModuleServiceType.Chat;
	}

	@Override
	public void init() {
		super.init();
		// 用户数据dao

		IIgniteService igniteService = context.getServiceManager().getService(ServiceType.ignite,
				GeneralConstant.DefaultIngiteServiceIdKey);

		chatMessageDao = daoFactory.createChatMessageDao(igniteService);
		chatRoomDao = daoFactory.createChatRoomDao(igniteService);
		chatRoomMemberDao = daoFactory.createChatRoomMemberDao(igniteService);
		roomUserOnlineDao = daoFactory.createRoomUserOnlineDao(igniteService);

		// 初始化房间处理
		chatRoomHandlerMap.put(RoomType.Group, new GroupRoom(this));
		chatRoomHandlerMap.put(RoomType.Single, new SingleRoom(this));
		chatRoomHandlerMap.put(RoomType.World, new WorldRoom(this));
		chatRoomHandlerMap.put(RoomType.Union, new UnionRoom(this));
		chatRoomHandlerMap.put(RoomType.Region, new RegionRoom(this));

		// -------------------------------------------
		// TODO 暂时这样弄先跑起来，参数需要调整
		HttpClientNetConfig httpClientNetConfig = new HttpClientNetConfig();
		httpClientNetConfig.setConnectionRequestTimeout(300);
		httpClientNetConfig.setConnectTimeout(300);
		httpClientNetConfig.setSocketTimeout(30000);// 这里读超时，暂时30s
		httpClientNetConfig.setDefaultMaxPerRoute(100);
		httpClientNetConfig.setMaxPoolTotal(100);
		httpClientNetConfig.setNetProtocolType(NetProtocolType.http);
		// MaxtTotal=400 DefaultMaxPerRoute=200
		// 而我只连接到http://sishuok.com时，到这个主机的并发最多只有200；而不是400；而我连接到http://sishuok.com 和
		// http://qq.com时，到每个主机的并发最多只有200；即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute。

		HttpAsyncClient httpAsyncClient = new HttpAsyncClient();
		httpAsyncClient.setConfig(httpClientNetConfig);

		httpAsyncClient.startup();
		translationService = new CokTranslationService(httpAsyncClient);

		// 创建时间聊天室
		createWorldRoom();
	}

	// 创建时间聊天室
	private void createWorldRoom() {
		IChatRoomHandler chatRoomHandler = chatRoomHandlerMap.get(RoomType.World);
		// 创建聊天室
		CreateChatRoomReqContext context = new CreateChatRoomReqContext();
		context.setRoomType(RoomType.World);
		chatRoomHandler.createRoom(context);
	}

	// -----------------------
	public HandeResult setMessageLastOrder(LastMessageOrderInChatUpContext context) {
		HandeResult handeResult = null;
		Map<String,String> mapping = context.getRoomToMsgOrder();
		if(mapping == null || mapping.size() <= 0){
			// 解析内容错误
			handeResult = HandeResult.errorResult(HandleErrorType.ParameterError);
			return handeResult;
		}
		Map<String,String> returnedMapping = new HashMap<>();
		for(String roomId:mapping.keySet()){
			ChatRoomMember member = chatRoomMemberDao.selectByRoomIdAndUid(roomId,context.getUserId());
			if(member != null) {
				long currentMaxOrder = getMaxOrder(roomId);
				Long lastOrder = Long.valueOf(mapping.get(roomId));
				if (lastOrder > currentMaxOrder) {
					lastOrder = currentMaxOrder;
				}
				member.setLastOrder(Math.max(0l,lastOrder)); //lastOrder不能小于0
				updateChatRoomMember(member);
				returnedMapping.put(roomId,String.valueOf(lastOrder));
			}
		}
		if(returnedMapping.size() <= 0){
			// 解析内容错误
			handeResult = HandeResult.errorResult(HandleErrorType.ParameterError);
		}else{
			handeResult = HandeResult.successResult(returnedMapping);
		}
		return handeResult;
	}

	public HandeResult gainChatMessage(GainChatMessageReqContext context) {
		HandeResult handeResult = null;
		switch (context.getGainType()) {
		case AllNewest: {
			// 获取用户聊天相关信息
			handeResult = gainAllNewestChatMessage(context);
			break;
		}
		case RoomMulti: {
			handeResult = gainRoomMultiChatMessage(context);
			break;
		}
		case RoomPage: {
			handeResult = gainRoomPageChatMessage(context);
			break;
		}
		case RoomNewest: {
			handeResult = gainRoomNewestChatMessage(context);
			break;
		}
		default:
			break;
		}
		return handeResult;
	}

	private GainMessageResult getWorldMessageResult(GainChatMessageReqContext context) {
		// 世界聊天，世界聊天没有成员管理
		IChatRoomHandler chatRoomHandler = chatRoomHandlerMap.get(RoomType.World);
		return chatRoomHandler.gainChatMessage(context, null, null);
	}

	private HandeResult gainAllNewestChatMessage(GainChatMessageReqContext context) {
		List<GainMessageResult> chatRoomInfos = new LinkedList<>();
		{
			// 世界聊天
			GainMessageResult result = getWorldMessageResult(context);
			chatRoomInfos.add(result);
		}
		{
			// 区
			IChatRoomHandler chatRoomHandler = chatRoomHandlerMap.get(RoomType.Region);
			GainMessageResult result = chatRoomHandler.gainChatMessage(context, null, null);
			if (result != null && result.getStateType() == ResultStateType.Success) {
				chatRoomInfos.add(result);
			}
		}
		{
			// 联盟
			IChatRoomHandler chatRoomHandler = chatRoomHandlerMap.get(RoomType.Union);
			GainMessageResult result = chatRoomHandler.gainChatMessage(context, null, null);
			if (result != null && result.getStateType() == ResultStateType.Success) {
				chatRoomInfos.add(result);
			}
		}

		// 单聊，组聊
		List<ChatRoomMember> selfs = getChatRoomMemberByUid(context.getUid());
		for (ChatRoomMember self : selfs) {
			ChatRoom chatRoom = getChatRoom(self.getRoomId());
			RoomType roomType = RoomType.valueOf(chatRoom.getRoomType());
			IChatRoomHandler chatRoomHandler = chatRoomHandlerMap.get(roomType);
			GainMessageResult result = chatRoomHandler.gainChatMessage(context, chatRoom, self);
			if (result != null && result.getStateType() == ResultStateType.Success) {
				chatRoomInfos.add(result);
			}
		}
		return HandeResult.successResult(chatRoomInfos);
	}

	private HandeResult gainRoomMultiChatMessage(GainChatMessageReqContext context) {
		RoomMultiCondition condition = context.getCondition();

		RoomType roomType = condition.getRoomType();
		ChatRoom chatRoom = null;
		ChatRoomMember self = null;
		switch (condition.getRoomType()) {
		case World:
		case Single:
		case Group: {
			chatRoom = getChatRoom(condition.getRoomId());
			if (chatRoom == null) {
				// 房间不存在
				return HandeResult.errorResult(HandleErrorType.RoomNoExist);
			}
			self = getChatRoomMember(condition.getRoomId(), context.getUid());
			if (self == null) {
				// 成员不存在
				return HandeResult.errorResult(HandleErrorType.MemberNoInRoom);
			}
			roomType = RoomType.valueOf(chatRoom.getRoomType());
			break;
		}
		default:
			break;
		}
		List<GainMessageResult> chatRoomInfos = new LinkedList<>();
		IChatRoomHandler chatRoomHandler = chatRoomHandlerMap.get(roomType);
		GainMessageResult result = chatRoomHandler.gainChatMessage(context, chatRoom, self);
		if (result != null && result.getStateType() == ResultStateType.Success) {
			chatRoomInfos.add(result);
		}
		return HandeResult.successResult(chatRoomInfos);
	}

	public HandeResult gainRoomPageChatMessage(GainChatMessageReqContext context) {
		RoomPageCondition condition = context.getCondition();

		RoomType roomType = condition.getRoomType();
		ChatRoom chatRoom = null;
		ChatRoomMember self = null;
		switch (condition.getRoomType()) {
		case World:
		case Single:
		case Group: {
			chatRoom = getChatRoom(condition.getRoomId());
			if (chatRoom == null) {
				// 房间不存在
				return HandeResult.errorResult(HandleErrorType.RoomNoExist);
			}
			self = getChatRoomMember(condition.getRoomId(), context.getUid());
			if (self == null) {
				// 成员不存在
				return HandeResult.errorResult(HandleErrorType.MemberNoInRoom);
			}
			roomType = RoomType.valueOf(chatRoom.getRoomType());
			break;
		}
		default:
			break;
		}
		List<GainMessageResult> chatRoomInfos = new LinkedList<>();
		IChatRoomHandler chatRoomHandler = chatRoomHandlerMap.get(roomType);
		GainMessageResult result = chatRoomHandler.gainChatMessage(context, chatRoom, self);
		if (result != null && result.getStateType() == ResultStateType.Success) {
			chatRoomInfos.add(result);
		}
		return HandeResult.successResult(chatRoomInfos);
	}

	private HandeResult gainRoomNewestChatMessage(GainChatMessageReqContext context) {
		RoomNewestCondition condition = context.getCondition();

		RoomType roomType = condition.getRoomType();
		ChatRoom chatRoom = null;
		ChatRoomMember self = null;

		List<GainMessageResult> chatRoomInfos = new LinkedList<>();
		switch (condition.getRoomType()) {
		case World: {
			// 世界服 不验证用户成员
			GainMessageResult result = getWorldMessageResult(context);
			chatRoomInfos.add(result);
			break;
		}
		case Region:
		case Union: {
			IChatRoomHandler chatRoomHandler = chatRoomHandlerMap.get(roomType);

			GainMessageResult result = chatRoomHandler.gainChatMessage(context, null, null);
			if (result != null && result.getStateType() == ResultStateType.Success) {
				chatRoomInfos.add(result);
			}
			break;
		}
		case Single:
		case Group: {
			chatRoom = getChatRoom(condition.getRoomId());
			if (chatRoom == null) {
				// 房间不存在
				return HandeResult.errorResult(HandleErrorType.RoomNoExist);
			}
			self = getChatRoomMember(condition.getRoomId(), context.getUid());
			if (self == null) {
				// 成员不存在
				return HandeResult.errorResult(HandleErrorType.MemberNoInRoom);
			}
			roomType = RoomType.valueOf(chatRoom.getRoomType());
			IChatRoomHandler chatRoomHandler = chatRoomHandlerMap.get(roomType);
			GainMessageResult result = chatRoomHandler.gainChatMessage(context, chatRoom, self);
			if (result != null && result.getStateType() == ResultStateType.Success) {
				chatRoomInfos.add(result);
			}
			break;
		}
		default:
			break;
		}
		return HandeResult.successResult(chatRoomInfos);
	}

	// -----------------------
	public HandeResult createRoom(CreateChatRoomReqContext context) {
		IChatRoomHandler chatRoomHandler = chatRoomHandlerMap.get(context.getRoomType());

		RoomResult result = chatRoomHandler.createRoom(context);

		HandeResult handeResult = null;
		switch (result.getStateType()) {
		case Fail:
			ErrorResult r = (ErrorResult) result;
			handeResult = HandeResult.errorResult(r.getErrorType());
			break;
		case Success:
			handeResult = HandeResult.successResult(result);
			break;
		default:
			break;
		}
		return handeResult;
	}

	public HandeResult modifyMember(ManagerChatRoomMemberReqContext context) {
		ChatRoom chatRoom = getChatRoom(context.getRoomId());
		if (chatRoom == null) {
			// 房间不存在
			return HandeResult.errorResult(HandleErrorType.RoomNoExist);
		}
		if (context.getModifyType() == null) {
			// 操作类型错误，参数错误
			return HandeResult.errorResult(HandleErrorType.ParameterError);
		}

		RoomType roomType = RoomType.valueOf(chatRoom.getRoomType());
		IChatRoomHandler chatRoomHandler = chatRoomHandlerMap.get(roomType);
		RoomResult result = chatRoomHandler.modifyMember(context, chatRoom);

		HandeResult handeResult = null;
		switch (result.getStateType()) {
		case Fail:
			ErrorResult r = (ErrorResult) result;
			handeResult = HandeResult.errorResult(r.getErrorType());
			break;
		case Success:
			handeResult = HandeResult.successResult(result);
			break;
		default:
			break;
		}
		return handeResult;
	}

	// -----------------------
	public HandeResult sendMessage(SendMessageReqContext context) {
		IContent content = contentFactory.createContent(context.getContentType(), context.getMessageContent());

		HandeResult handeResult = null;
		if (content == null) {
			// 解析内容错误
			handeResult = HandeResult.errorResult(HandleErrorType.ParameterError);
			return handeResult;
		}
		context.setContent(content);

		IChatRoomHandler chatRoomHandler = chatRoomHandlerMap.get(context.getRoomType());
		RoomResult result = chatRoomHandler.sendMessage(context);

		switch (result.getStateType()) {
		case Fail:
			ErrorResult r = (ErrorResult) result;
			handeResult = HandeResult.errorResult(r.getErrorType());
			break;
		case Success:
			handeResult = HandeResult.successResult(result);
			break;
		default:
			break;
		}
		return handeResult;
	}

	public HandeResult translationMessage(TranslationMessageReqContext context) {
		HandeResult handeResult = null;

		ChatMessage chatMessage = getChatMessage(context.getRoomId(), context.getOrder());
		if (chatMessage == null) {
			// 消息不存在
			handeResult = HandeResult.errorResult(HandleErrorType.ChatMessageNoExist);
		} else {
			ChatRoom chatRoom = getChatRoom(context.getRoomId());

			TranslationMessageResult result = new TranslationMessageResult();
			result.setChatMessage(chatMessage);
			// 联盟，区不保存用户信息。这里需要逻辑服传送
			result.setRoomType(chatRoom != null ? chatRoom.getRoomType() : context.getRoomType());
			handeResult = HandeResult.successResult(result);
		}
		return handeResult;
	}

	@Override
	public void registerInit(long userId) {
	}

	@Override
	public void uploadData(IPlayer player) {
	}

	@Override
	public void loginInit(IPlayer player) {
		String uid = String.valueOf(player.getUserId());
		List<ChatRoomMember> chatRoomMembers = chatRoomMemberDao.selectByUid(uid);
		for (ChatRoomMember chatRoomMember : chatRoomMembers) {
			RoomUserOnline roomUserOnline = createRoomUserOnline(uid, chatRoomMember.getRoomId());
			// 房间在线人员
			roomUserOnlineDao.insert(roomUserOnline, null);
		}
	}

	@Override
	public void offline(IPlayer player, long time) {
		String uid = String.valueOf(player.getUserId());
		List<RoomUserOnline> roomUserOnlines = roomUserOnlineDao.selectByUid(uid);
		for (RoomUserOnline roomUserOnline : roomUserOnlines) {
			// 移除房间在线信息
			roomUserOnlineDao.remove(roomUserOnline, null);
		}
	}

	public List<ChatUser> getChatRoomMemberAllChatUser(String roomId) {
		List<ChatUser> chatUsers = new LinkedList<>();

		List<ChatRoomMember> entitys = getChatRoomMemberByRoomId(roomId);
		for (ChatRoomMember entity : entitys) {
			if (entity.getState() != MemberStateType.Normal.ordinal()) {
				continue;
			}
			IChatUserMService userMService = getUserMService();
			ChatUser chatUser = userMService.getChatUser(entity.getUid());
			if(chatUser != null) {
				chatUsers.add(chatUser);
			}
		}
		return chatUsers;
	}

	public void translate(LanguageType fromLang, String content, Set<LanguageType> toLangs, ARequestCallback callback) {
		translationService.translate(fromLang, content, toLangs, callback);
	}

	public void translate(LanguageType fromLang, String content, LanguageType toLangs, ARequestCallback callback) {
		translationService.translate(fromLang, content, toLangs, callback);
	}

	// ------ 数据操作方法 ------
	public ChatRoom getChatRoom(String roomId) {
		return chatRoomDao.selectByRoomId(roomId);
	}

	public List<ChatRoomMember> getChatRoomMemberByUid(String uid) {
		return chatRoomMemberDao.selectByUid(uid);
	}

	public List<ChatRoomMember> getChatRoomMemberByRoomId(String roomId) {
		return chatRoomMemberDao.selectByRoomId(roomId);
	}

	public long getMaxOrder(String roomId) {
		return chatMessageDao.getMaxOrder(roomId);
	}

	public List<ChatMessage> getNewChatMessages(String roomId, int newCountMax) {
		return chatMessageDao.selectByRoomIdAndCount(roomId, newCountMax);
	}

	public List<ChatMessage> getChatMessagesByRoomIdAndOrders(String roomId, List<Long> orders) {
		return chatMessageDao.selectByRoomIdAndOrders(roomId, orders);
	}

	public List<ChatMessage> getChatMessagesByRoomIdAndOrderPage(String roomId, long order, int pageCount) {
		return chatMessageDao.selectByRoomIdAndOrderPage(roomId, order, pageCount);
	}

	public ChatMessage getChatMessage(String roomId, long order) {
		return chatMessageDao.selectByRoomIdAndOrder(roomId, order);
	}

	public ChatRoomMember getChatRoomMember(String roomId, String uid) {
		return chatRoomMemberDao.selectByRoomIdAndUid(roomId, uid);
	}

	public List<RoomUserOnline> getRoomUserOnlines(String roomId) {
		return roomUserOnlineDao.selectByRoomId(roomId);
	}

	public void insertChatRoomMember(ChatRoomMember pojo) {
		chatRoomMemberDao.insert(pojo, null);
	}

	public void updateChatRoomMember(ChatRoomMember pojo) {
		chatRoomMemberDao.update(pojo, null);
	}

	public void insertChatRoom(ChatRoom pojo) {
		chatRoomDao.insert(pojo, null);
	}

	public void insertChatMessage(ChatMessage pojo) {
		chatMessageDao.insert(pojo, null);
	}

	public void batchInsertChatRoomMember(List<ChatRoomMember> pojos) {
		chatRoomMemberDao.batchInsert(pojos, null);
	}

	public MemberOnline getUserOnlineInGate(String uid) {
		IUserMService userMService = getUserMService();
		MemberOnline userOnline = userMService.getUserOnlineInGate(uid);
		return userOnline;
	}

	public void modifyRoomUserOnline(String uid, String roomId, MemberModifyType memberModifyType) {
		MemberOnline userOnline = getUserOnlineInGate(uid);
		if (userOnline != null) {
			switch (memberModifyType) {
			case Insert:
				RoomUserOnline roomUserOnline = createRoomUserOnline(uid, roomId);
				// 房间在线人员
				roomUserOnlineDao.insert(roomUserOnline, null);
				break;
			case Remove:
				RoomUserOnline entity = roomUserOnlineDao.selectByRoomIdAndUid(roomId, uid);
				if (entity != null) {
					roomUserOnlineDao.remove(entity, null);
				}
				break;
			default:
				break;
			}
		}
	}

	// 在线用户所在服务sid分类
	public OnlineUserInfo getOnlineUserInfo(Collection<String> uids, boolean isTranslation) {
		OnlineUserInfo onlineUserInfo = new OnlineUserInfo(isTranslation);
		for (String uid : uids) {
			MemberOnline userOnline = getUserOnlineInGate(uid);
			if (userOnline == null) {
				continue;
			}
			IChatUserMService userMService = getUserMService();
			ChatUser chatUser = userMService.getChatUser(uid);
			onlineUserInfo.insertUserOnline(userOnline, chatUser);
		}
		return onlineUserInfo;
	}

	public OnlineUserInfo getOnlineUserInfo(String uid, boolean isTranslation) {
		OnlineUserInfo onlineUserInfo = new OnlineUserInfo(isTranslation);
		MemberOnline userOnline = getUserOnlineInGate(uid);
		if (userOnline == null) {
			return onlineUserInfo;
		}
		IChatUserMService userMService = getUserMService();
		ChatUser chatUser = userMService.getChatUser(uid);
		onlineUserInfo.insertUserOnline(userOnline, chatUser);
		return onlineUserInfo;
	}

	public OnlineUserInfo getRoomOnlineUserInfo(String roomId, boolean isTranslation) {
		// 把用户按sid分类
		OnlineUserInfo onlineUserInfo = new OnlineUserInfo(isTranslation);

		List<RoomUserOnline> roomUserOnlines = getRoomUserOnlines(roomId);
		for (RoomUserOnline roomUserOnline : roomUserOnlines) {
			MemberOnline userOnline = getUserOnlineInGate(roomUserOnline.getUid());
			if (userOnline == null) {
				continue;
			}
			IChatUserMService userMService = getUserMService();
			ChatUser chatUser = userMService.getChatUser(userOnline.getMemberId());
			onlineUserInfo.insertUserOnline(userOnline, chatUser);
		}
		return onlineUserInfo;
	}

	// ------ 创建数据方法 ------
	public ChatRoomMember getCreateChatRoomMember(String roomId, String uid) {
		ChatRoomMember chatRoomMember = chatRoomMemberDao.selectByRoomIdAndUid(roomId, uid);
		if (chatRoomMember == null) {
			chatRoomMember = createChatRoomMember(roomId, uid, 0l);
		}
		chatRoomMemberDao.insert(chatRoomMember, null);
		return chatRoomMember;
	}

	public ChatRoomMember createChatRoomMember(String roomId, String uid, long lastOrder) {
		ChatRoomMember entity = new ChatRoomMember();
		entity.setUid(uid);
		entity.setState(MemberStateType.Normal.ordinal());
		entity.setRoomId(roomId);
		entity.setLastOrder(lastOrder);
		// entity.setAtOrders(atOrders);//初始没有at信息
		return entity;
	}

	public ChatRoom createChatRoom(String roomId, RoomType roomType, String admin, String name) {
		long createTime = System.currentTimeMillis();
		ChatRoom entity = new ChatRoom();
		entity.setRoomId(roomId);
		entity.setRoomType(roomType.getValue());
		entity.setCreateTime(createTime);
		if (admin == null) {
			admin = "";
		}
		entity.setAdmin(admin);

		if (name == null) {
			name = "";
		}
		entity.setName(name);
		return entity;
	}

	private RoomUserOnline createRoomUserOnline(String uid, String roomId) {
		RoomUserOnline roomUserOnline = new RoomUserOnline();
		roomUserOnline.setRoomId(roomId);
		roomUserOnline.setUid(uid);
		return roomUserOnline;
	}

	public ChatMessage createChatMessage(String roomId, String uid, IContent content, Set<String> atUids,
			long clientSendedTime, String clientExt, String serverExt) {

		ChatMessage entity = new ChatMessage();
		if (atUids == null) {
			atUids = new HashSet<>(0);
		}
		// 获得下个order
		long order = chatMessageDao.getNextOrder(roomId);

		entity.setAtUidsSet(atUids);
		entity.setContent(content.getContent());
		entity.setContentType(content.contentType().ordinal());
		entity.setOrderId(order);
		entity.setReceivedTime(System.currentTimeMillis());
		entity.setRoomId(roomId);
		entity.setSendedTime(clientSendedTime);
		entity.setClientExt(clientExt);
		entity.setServerExt(serverExt);
		entity.setUid(uid);
		return entity;
	}

	private IChatUserMService getUserMService() {
		return getModuleService(ModuleServiceType.User);
	}

	public IErrorMService getErrorMService() {
		return getModuleService(ModuleServiceType.Error);
	}
}
