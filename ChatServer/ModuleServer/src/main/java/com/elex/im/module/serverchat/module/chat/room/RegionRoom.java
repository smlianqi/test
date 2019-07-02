package com.elex.im.module.serverchat.module.chat.room;

import java.util.Set;

import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.data.chatroommember.ChatRoomMember;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.chat.room.request.CreateChatRoomReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.GainChatMessageReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.ManagerChatRoomMemberReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.SendMessageReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.AllNewestCondition;
import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.RoomCondition;
import com.elex.im.module.serverchat.module.chat.room.result.GainMessageResult;
import com.elex.im.module.serverchat.module.chat.room.result.OnlineUserInfo;
import com.elex.im.module.serverchat.module.chat.room.result.RoomResult;
import com.elex.im.module.serverchat.module.chat.room.result.SendMessageResult;
import com.elex.im.module.serverchat.module.chat.type.RoomType;
import com.elex.im.module.translation.type.LanguageType;

/**
 * 区聊天室
 * 
 * @author mausmars
 *
 */
public class RegionRoom extends AChatRoom {
	private OnlineUserInfo onlineUserInfo;

	public RegionRoom(ChatMService chatMService) {
		super(chatMService);
		// TODO 区聊默认翻译的语言
		this.onlineUserInfo = new OnlineUserInfo(true);

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
		return null;
	}

	@Override
	public RoomResult modifyMember(ManagerChatRoomMemberReqContext context, ChatRoom chatRoom) {
		return null;
	}

	@Override
	public GainMessageResult gainChatMessage(GainChatMessageReqContext context, ChatRoom chatRoom,
			ChatRoomMember self) {
		// 区，不能走通用流程
		GainMessageResult result = null;

		switch (context.getGainType()) {
		case AllNewest: {
			AllNewestCondition condition = context.getCondition();
			result = defaultGainNewChatMessage(context, condition.getRegionRoomId());
			break;
		}
		case RoomNewest: {
			RoomCondition condition = context.getCondition();
			result = defaultGainNewChatMessage(context, condition.getRoomId());
			break;
		}
		case RoomMulti: {
			RoomCondition condition = context.getCondition();
			result = defaultGainMultiChatMessage(context, condition.getRoomId());
			break;
		}
		case RoomPage: {
			RoomCondition condition = context.getCondition();
			result = defaultGainPageChatMessage(context, condition.getRoomId());
			break;
		}
		default:
			break;
		}
		result.setRoomType(RoomType.Region.getValue());
		return result;
	}

	/** 发送群聊天消息 */
	@Override
	public RoomResult sendMessage(SendMessageReqContext context) {
		String roomId = context.getRoomId();
		Set<String> atUids = context.getAtUids();

		SendMessageResult result = defaultSendMessage(context, roomId);

		// 区的需要走广播，人太多了
		// OnlineUserInfo onlineUserInfo =
		// chatMService.getRoomOnlineUserInfo(chatRoom.getRoomId(), false);
		// 设置需要通知
		result.setOnlineUserInfo(onlineUserInfo);
		return result;
	}
}
