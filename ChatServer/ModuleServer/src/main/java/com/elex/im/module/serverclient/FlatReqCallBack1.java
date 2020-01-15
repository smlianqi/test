package com.elex.im.module.serverclient;

import com.elex.common.component.player.IPlayer;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.message.flat.CreateChatRoomDown;
import com.elex.im.message.flat.ErrorMessageDown;
import com.elex.im.message.flat.GainChatMessageDown;
import com.elex.im.message.flat.ManagerChatRoomMemberDown;
import com.elex.im.message.flat.SendChatMessageDown;
import com.elex.im.message.flat.SuccessMessageDown;
import com.elex.im.message.flat.TranslationMessageDown;
import com.elex.im.message.flat.UserLoginDown;
import com.elex.im.module.serverclient.request.SendSingleChatReq;
import com.elex.im.module.serverclient.serveraccess.service.IChatClientAccessService;
import com.elex.im.module.serverclient.serveraccess.service.IFlatReqCallBack;

public class FlatReqCallBack1 implements IFlatReqCallBack {
	protected static final ILogger logger = XLogUtil.logger();

	private String testRoomId = "Room-1";
	private long userId1 = 10001;
	private long userId2 = 10002;

	private IChatClientAccessService chatClientAccessService;

	public FlatReqCallBack1(IChatClientAccessService chatClientAccessService) {
		this.chatClientAccessService = chatClientAccessService;
	}

	@Override
	public void userLoginClassBack(IPlayer player, UserLoginDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug("userLoginClassBack userId=" + player.getUserId());
		}
		long targetUid = userId2;
		if (player.getUserId() == userId2) {
			targetUid = userId1;
		}
		// 发送单聊指令

		SendSingleChatReq req = new SendSingleChatReq();
		req.setTargetUid(targetUid);
		req.setContent("Hello World!!!!!!");
		req.setSendedTime(System.currentTimeMillis());
		req.setTargetUid(targetUid);
		req.setClientExt("");
		req.setUid(player.getUserId());
		chatClientAccessService.sendSingleChatMessage(player.getUserId(), req);

		// List<Long> members = new LinkedList<>();
		// members.add(userId1);
		// members.add(userId2);
		// FlatModuleMessageCreater.createCreateChatRoomUp(player.getUserId(),
		// testRoomId,
		// "", members);
	}

	@Override
	public void gainChatMessageClassBack(IPlayer player, GainChatMessageDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug("gainChatMessageClassBack userId=" + player.getUserId());
		}
	}

	@Override
	public void createChatRoomClassBack(IPlayer player, CreateChatRoomDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug("createChatRoomClassBack userId=" + player.getUserId());
		}
	}

	@Override
	public void managerChatRoomMemberClassBack(IPlayer player, ManagerChatRoomMemberDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug("managerChatRoomMemberClassBack userId=" + player.getUserId());
		}
	}

	@Override
	public void sendChatMessageClassBack(IPlayer player, SendChatMessageDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug("sendChatMessage userId=" + player.getUserId() + " message=" + message);
		}
	}

	@Override
	public void errorMessageClassBack(IPlayer player, ErrorMessageDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug("errorMessage userId=" + player.getUserId() + " message=" + message);
		}
	}

	@Override
	public void successMessageClassBack(IPlayer player, SuccessMessageDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug("successMessage userId=" + player.getUserId() + " message=" + message);
		}
	}

	@Override
	public void translationMessageClassBack(IPlayer player, TranslationMessageDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug("translationMessage userId=" + player.getUserId() + " message=" + message);
		}
	}
}
