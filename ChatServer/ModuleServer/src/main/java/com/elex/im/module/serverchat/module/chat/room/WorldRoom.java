package com.elex.im.module.serverchat.module.chat.room;

import java.util.List;

import com.elex.im.data.chatmessage.ChatMessage;
import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.data.chatroommember.ChatRoomMember;
import com.elex.im.module.HandleErrorType;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.chat.room.request.CreateChatRoomReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.GainChatMessageReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.ManagerChatRoomMemberReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.SendMessageReqContext;
import com.elex.im.module.serverchat.module.chat.room.result.ErrorResult;
import com.elex.im.module.serverchat.module.chat.room.result.GainMessageResult;
import com.elex.im.module.serverchat.module.chat.room.result.OnlineUserInfo;
import com.elex.im.module.serverchat.module.chat.room.result.RoomResult;
import com.elex.im.module.serverchat.module.chat.room.result.SendMessageResult;
import com.elex.im.module.serverchat.module.chat.type.RoomType;
import com.elex.im.module.translation.type.LanguageType;

/**
 * 世界聊天
 * 
 * @author mausmars
 *
 */
public class WorldRoom extends AChatRoom {
	private String roomId = "### World_Room ###";
	private OnlineUserInfo onlineUserInfo;

	public WorldRoom(ChatMService chatMService) {
		super(chatMService);
		this.onlineUserInfo = new OnlineUserInfo(true);

		// TODO 世界聊天默认翻译的语言
		onlineUserInfo.getLanguageTypes().add(LanguageType.en);
		onlineUserInfo.getLanguageTypes().add(LanguageType.zh_Hans);
		onlineUserInfo.getLanguageTypes().add(LanguageType.zh_Hant);
		onlineUserInfo.getLanguageTypes().add(LanguageType.ar);
		onlineUserInfo.getLanguageTypes().add(LanguageType.bg);
		onlineUserInfo.getLanguageTypes().add(LanguageType.fr);
		onlineUserInfo.getLanguageTypes().add(LanguageType.de);
		onlineUserInfo.getLanguageTypes().add(LanguageType.el);
		onlineUserInfo.getLanguageTypes().add(LanguageType.hu);
		onlineUserInfo.getLanguageTypes().add(LanguageType.it);
		onlineUserInfo.getLanguageTypes().add(LanguageType.ja);
		onlineUserInfo.getLanguageTypes().add(LanguageType.ru);
		onlineUserInfo.getLanguageTypes().add(LanguageType.es);
		onlineUserInfo.getLanguageTypes().add(LanguageType.th);
		onlineUserInfo.getLanguageTypes().add(LanguageType.vi);
	}

	@Override
	public RoomResult createRoom(CreateChatRoomReqContext context) {
		// 世界聊天不需要房间成员管理
		ChatRoom chatRoom = chatMService.getChatRoom(roomId);
		if (chatRoom != null) {
			// 已经存在聊天室
			return ErrorResult.errorResult(HandleErrorType.RoomExist);
		}
		context.setRoomId(roomId);
		return defaultCreateRoom(context);
	}

	@Override
	public RoomResult modifyMember(ManagerChatRoomMemberReqContext context, ChatRoom chatRoom) {
		// 世界聊天不需要房间成员管理
		return ErrorResult.errorResult(HandleErrorType.UnsupportedOperations);
	}

	/** 获取聊天室信息 */
	@Override
	public GainMessageResult gainChatMessage(GainChatMessageReqContext context, ChatRoom chatRoom,
			ChatRoomMember self) {

		// 世界聊天不判断成员信息
		GainMessageResult result = new GainMessageResult();
		switch (context.getGainType()) {
		case AllNewest:
		case RoomNewest:
			chatRoom = chatMService.getChatRoom(roomId);
			if (chatRoom == null) {
				return null;
			}
			// 获取聊天信息
			List<ChatMessage> chatMessages = chatMService.getNewChatMessages(roomId, NewCountMax);
			result.setChatRoom(chatRoom);
			result.setChatMessages(chatMessages);
			result.setRoomType(RoomType.World.getValue());
			break;
		case RoomMulti:
			result = defaultGainMultiChatMessage(context, chatRoom.getRoomId());
			break;
		case RoomPage:
			result = defaultGainPageChatMessage(context, chatRoom.getRoomId());
			break;
		default:
			break;
		}
		result.setRoomType(RoomType.World.getValue());
		return result;
	}

	/** 发送世界聊天消息 */
	@Override
	public RoomResult sendMessage(SendMessageReqContext context) {
		ChatRoom chatRoom = chatMService.getChatRoom(roomId);
		if (chatRoom == null) {
			// 房间不存在
			return ErrorResult.errorResult(HandleErrorType.RoomNoExist);
		}
		// 这个都能发不判断成员
		context.setRoomId(roomId);

		SendMessageResult result = defaultSendMessage(context, roomId);
		result.setChatRoom(chatRoom);

		// 设置需要翻译
		result.setOnlineUserInfo(onlineUserInfo);
		return result;
	}
}
