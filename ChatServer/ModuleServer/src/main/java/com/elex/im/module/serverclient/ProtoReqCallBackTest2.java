package com.elex.im.module.serverclient;

import java.util.LinkedList;
import java.util.List;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.message.proto.ChatMessage.CreateChatRoomDown;
import com.elex.im.message.proto.ChatMessage.GainChatMessageDown;
import com.elex.im.message.proto.ChatMessage.ManagerChatRoomMemberDown;
import com.elex.im.message.proto.ChatMessage.SendChatMessageDown;
import com.elex.im.message.proto.ChatMessage.TranslationMessageDown;
import com.elex.im.message.proto.ErrorMessage.ErrorMessageDown;
import com.elex.im.message.proto.ErrorMessage.SuccessMessageDown;
import com.elex.im.message.proto.UserMessage.UserLoginDown;
import com.elex.im.module.serverchat.module.chat.content.TextContent;
import com.elex.im.module.serverchat.module.chat.type.RoomType;
import com.elex.im.module.serverclient.request.SendMultiChatReq;
import com.elex.im.module.serverclient.serveraccess.service.IChatClientAccessService;
import com.elex.im.module.serverclient.serveraccess.service.IProtoReqCallBack;

public class ProtoReqCallBackTest2 implements IProtoReqCallBack {
	protected static final ILogger logger = XLogUtil.logger();

	private IChatClientAccessService chatClientAccessService;

	public ProtoReqCallBackTest2(IChatClientAccessService chatClientAccessService) {
		this.chatClientAccessService = chatClientAccessService;
	}

	@Override
	public void userLoginClassBack(final IPlayer player, UserLoginDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> userLoginClassBack userId=" + player.getUserId());
		}

		FunctionType functionType = FunctionType.valueOf(message.getFunctionType());

		if (functionType != FunctionType.chat) {
			return;
		}
		if (player.getUserId().equals(ChatTestManger.userId1)) {
			// 登录完毕创建聊天室
			createChatRoom(player);
		}

		// 发送获取
		// chatClientAccessService.sendGainAllChatMessage(player.getUserId());
	}

	@Override
	public void createChatRoomClassBack(IPlayer player, CreateChatRoomDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> createChatRoomClassBack userId=" + player.getUserId() + ", rid=" + message.getRoomId());
		}

		RoomType roomType = RoomType.valueOf(message.getRoomType());
		switch (roomType) {
		case Region:
			// 狂发消息
			for (int i = 0; i < 1000000; i++) {
				TextContent content = new TextContent("Test!!! id_" + i + " roomType=" + roomType.name());

				SendMultiChatReq req = new SendMultiChatReq();
				req.setUid(player.getUserId());
				req.setContentType(content.contentType());
				req.setContent(content.content2Json());
				req.setSendedTime(System.currentTimeMillis());
				req.setClientExt("");
				req.setRoomId(message.getRoomId());
				req.setRoomType(roomType);
				req.setAtUids(new LinkedList<>());

				chatClientAccessService.sendMultiChatMessage(player.getUserId(), req);
			}
			break;
		default:
			break;
		}
	}

	// 创建组
	private void createChatRoom(IPlayer player) {
		{
			List<Long> members = new LinkedList<>();
			members.add(ChatTestManger.userId1);
			members.add(ChatTestManger.userId2);

			chatClientAccessService.sendCreateChatRoom(player.getUserId(), RoomType.Group, "", "", members);
		}
		{
			List<Long> members = new LinkedList<>();
			members.add(ChatTestManger.userId1);
			members.add(ChatTestManger.userId2);
			chatClientAccessService.sendCreateChatRoom(player.getUserId(), RoomType.Region, ChatTestManger.RegionRoomId,
					"", members);
		}
		{
			List<Long> members = new LinkedList<>();
			members.add(ChatTestManger.userId1);
			members.add(ChatTestManger.userId2);
			chatClientAccessService.sendCreateChatRoom(player.getUserId(), RoomType.Union, ChatTestManger.UnionRoomId,
					"", members);
		}
	}

	@Override
	public void gainChatMessageClassBack(IPlayer player, GainChatMessageDown message) {

	}

	@Override
	public void managerChatRoomMemberClassBack(IPlayer player, ManagerChatRoomMemberDown message) {

	}

	@Override
	public void sendChatMessageClassBack(IPlayer player, SendChatMessageDown message) {

	}

	@Override
	public void translationMessageClassBack(IPlayer player, TranslationMessageDown message) {

	}

	@Override
	public void errorMessageClassBack(IPlayer player, ErrorMessageDown message) {

	}

	@Override
	public void successMessageClassBack(IPlayer player, SuccessMessageDown message) {

	}
}
