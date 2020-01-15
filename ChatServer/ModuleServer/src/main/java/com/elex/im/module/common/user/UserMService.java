package com.elex.im.module.common.user;

import com.elex.common.component.event.IEventService;
import com.elex.common.component.function.IFunctionService;
import com.elex.common.component.function.event.MasterChangeEvent;
import com.elex.common.component.function.event.NodeDeleteEvent;
import com.elex.common.component.function.info.IFunctionInfo;
import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.member.IMemberService;
import com.elex.common.component.member.data.MemberOnline;
import com.elex.common.component.member.type.MemberType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.IPlayerFactory;
import com.elex.common.component.player.IPlayerInitOffline;
import com.elex.common.component.player.type.PlayerAttachType;
import com.elex.common.component.player.type.UserType;
import com.elex.common.message.BroadcastType;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.session.ISession;
import com.elex.common.net.session.ISessionPool;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.service.GeneralConstant;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.module.AModuleService;
import com.elex.common.service.module.ModuleServiceType;
import com.elex.common.service.type.ServiceType;
import com.elex.im.module.translation.type.LanguageType;
import com.google.common.eventbus.Subscribe;

import java.util.List;

public class UserMService extends AModuleService implements IUserMService {
	private IMemberService memberService;
	private IFunctionService functionService;

	private IPlayerInitOffline playerInitOffline;

	public UserMService(IGlobalContext context) {
		super(context);
	}

	@Override
	public ModuleServiceType getModuleServiceType() {
		return ModuleServiceType.User;
	}

	@Override
	public void init() {
		super.init();
		// 用户数据dao

		IEventService eventService = context.getServiceManager().getService(ServiceType.event,
				GeneralConstant.DefaultEventServiceIdKey);
		if (eventService != null) {
			// 节点变更监听器
			eventService.registerSync(this);
		}
		memberService = context.getServiceManager().getService(ServiceType.member,
				GeneralConstant.DefaultMemberServiceIdKey);

		functionService = context.getServiceManager().getService(ServiceType.function,
				GeneralConstant.DefaultFunctionServiceIdKey);


	}

	@Subscribe
	public void listen(MasterChangeEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("EventListener MasterChangeEvent event=" + event);
		}
		// 主节点变更事件
	}

	@Subscribe
	public void listen(NodeDeleteEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("EventListener NodeDeleteEvent event=" + event);
		}
		// 节点被删除事件
		String fid = event.getFunctionId().getFid(true);
	}

	public IPlayer createPlayer(ISession session, long userId, UserType userType) {
		SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);

		IPlayerFactory playerFactory = sessionBox.getPlayerFactory();

		IPlayer player = playerFactory.createPlayer(userId, userType);
		// 设置用户类型
		ISession oldSession = player.getSession();
		if (oldSession == null) {
			player.replaceSession(session);
			// 添加映射关系
			sessionBox.bindingPlayer(player);
		}
		return player;
	}

	@Override
	public void registerInit(long userId) {
		if (playerInitOffline != null) {
			playerInitOffline.registerInit(userId);
		}
	}

	@Override
	public void uploadData(IPlayer player) {
		if (playerInitOffline != null) {
			playerInitOffline.uploadData(player);
		}
	}

	@Override
	public MemberOnline getUserOnline(String uid, FunctionType functionType) {
		return memberService.getMemberOnlineByMemberIdAndFunctionType(uid, functionType.name());
	}

	@Override
	public MemberOnline getUserOnlineInGate(String uid) {
		return memberService.getMemberOnlineByMemberIdAndFunctionType(uid, FunctionType.gate.name());
	}

	@Override
	public void loginInit(IPlayer player) {
		// 登录本服
	}

	@Override
	public MemberOnline bindMember(IPlayer player, String extend) {
		MemberOnline memberOnline = memberService.loginLocalServer(String.valueOf(player.getUserId()), MemberType.user,
				extend);
		if (playerInitOffline != null) {
			playerInitOffline.loginInit(player);
		}
		return memberOnline;
	}

	@Override
	public void offline(IPlayer player, long time) {
		String uid = String.valueOf(player.getUserId());

		// 登出当前服务
		memberService.logoutLocalServer(uid);

		if (playerInitOffline != null) {
			playerInitOffline.offline(player, time);
		}
	}

	@Override
	public void modifyLanguage(IPlayer player, LanguageType languageType) {
		player.setAttach(PlayerAttachType.LanguageType, languageType);
	}

	// ----------------------------------------------
	@Override
	public void forwardSingle(Object message, String userId) {
		IFunctionInfo functionInfo = memberService.getFunctionInfoByMemberId(userId, FunctionType.gate);
		if (functionInfo == null) {
			logger.error("forwardSingle functionInfo == null!!!");
			return;
		}
		ISessionPool sessionPool = functionInfo.getService(FServiceType.NettyForward, FServiceType.NettyForward.name());
		// 发送
		sessionPool.send(userId, message);
	}

	@Override
	public void forwardGroup(int commandId, byte[] content, List<String> userIds, String sid) {
		IFunctionInfo functionInfo = functionService.getFunctionInfo(sid);
		if (functionInfo == null) {
			logger.error("forwardGroup functionInfo == null!!!");
			return;
		}
		ISessionPool sessionPool = functionInfo.getService(FServiceType.NettyForward, FServiceType.NettyForward.name());
		// 发送
		ISession session = sessionPool.getRandomSession();

		SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
		IMessage msg = sessionBox.getMessageCreater().createBroadcastUpMessage(BroadcastType.Users, commandId, content,
				userIds);
		session.send(msg);
	}

	/**
	 * 转发全局
	 * 
	 * @param commandId
	 * @param content
	 */
	@Override
	public void forwardWhole(int commandId, byte[] content) {
		// 获得所有gate服
		List<IFunctionInfo> functionInfos = functionService.getRemoteFunctionInfos(FunctionType.gate);

		for (IFunctionInfo functionInfo : functionInfos) {
			ISessionPool sessionPool = functionInfo.getService(FServiceType.NettyForward,
					FServiceType.NettyForward.name());

			// 随机选一个连接发送
			ISession session = sessionPool.getRandomSession();

			SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
			IMessage msg = sessionBox.getMessageCreater().createBroadcastUpMessage(BroadcastType.Whole, commandId,
					content, null);
			session.send(msg);
		}
	}

	// -------------------------------------
	public void setPlayerInitOffline(IPlayerInitOffline playerInitOffline) {
		this.playerInitOffline = playerInitOffline;
	}
}
