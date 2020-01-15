package com.elex.im.module.serverchat.module.chat.room;

import com.elex.common.util.uuid.UUIDUtil;
import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.data.chatroommember.ChatRoomMember;
import com.elex.im.data.chatuser.ChatUser;
import com.elex.im.module.HandleErrorType;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.chat.room.request.CreateChatRoomReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.GainChatMessageReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.ManagerChatRoomMemberReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.SendMessageReqContext;
import com.elex.im.module.serverchat.module.chat.room.result.*;
import com.elex.im.module.serverchat.module.chat.type.MemberModifyType;
import com.elex.im.module.serverchat.module.chat.type.MemberStateType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;

import java.util.List;
import java.util.Set;

/**
 * 组聊天
 * 
 * @author mausmars
 *
 */
public class GroupRoom extends AChatRoom {
	public GroupRoom(ChatMService chatMService) {
		super(chatMService);
	}

	@Override
	public RoomResult createRoom(CreateChatRoomReqContext context) {
		// 自己产生唯一id
		String roomId = UUIDUtil.getUUID();
		context.setRoomId(roomId);
		CreateRoomResult result = defaultCreateRoom(context);
		// 加入房间在线
		for (String uid : context.getUids()) {
			chatMService.modifyRoomUserOnline(uid, roomId, MemberModifyType.Insert);
		}
		// 设置需要通知
		result.setOnlineUserInfo(chatMService.getOnlineUserInfo(context.getUids(), false));
		return result;
	}

	@Override
	public RoomResult modifyMember(ManagerChatRoomMemberReqContext context, ChatRoom chatRoom) {
		// 需要做通知
		ModifyMemberResult result = defaultModifyMember(context, chatRoom);
		// 设置需要通知
		result.setOnlineUserInfo(chatMService.getRoomOnlineUserInfo(chatRoom.getRoomId(), false));
		return result;
	}

	/** 获取聊天室信息 */
	@Override
	public GainMessageResult gainChatMessage(GainChatMessageReqContext context, ChatRoom chatRoom,
			ChatRoomMember self) {
		GainMessageResult result = null;
		switch (context.getGainType()) {
		case AllNewest:
		case RoomNewest:
			result = defaultGainNewChatMessage(context, chatRoom, self);
			// 取其他成员信息
			List<ChatUser> chatUsers = chatMService.getChatRoomMemberAllChatUser(chatRoom.getRoomId());
			result.setChatUsers(chatUsers);
			result.setChatRoom(chatRoom);
			break;
		case RoomMulti:
			result = defaultGainMultiChatMessage(context, chatRoom.getRoomId());
			List<ChatUser> chatUsers3 = chatMService.getChatRoomMemberAllChatUser(chatRoom.getRoomId());
			result.setSelf(self);
			result.setChatUsers(chatUsers3);
			result.setChatRoom(chatRoom);
			break;
		case RoomPage:
			result = defaultGainPageChatMessage(context, chatRoom.getRoomId());
			List<ChatUser> chatUsers4 = chatMService.getChatRoomMemberAllChatUser(chatRoom.getRoomId());
			result.setSelf(self);
			result.setChatUsers(chatUsers4);
			result.setChatRoom(chatRoom);
			break;
		default:
			break;
		}
		result.setRoomType(RoomType.Group.getValue());
		return result;
	}

	/** 发送群聊天消息 */
	@Override
	public RoomResult sendMessage(SendMessageReqContext context) {
		String roomId = context.getRoomId();
		Set<String> atUids = context.getAtUids();

		ChatRoom chatRoom = chatMService.getChatRoom(roomId);
		if (chatRoom == null) {
			// 房间不存在
			return ErrorResult.errorResult(HandleErrorType.RoomNoExist);
		}

		ChatRoomMember chatRoomMember = chatMService.getChatRoomMember(chatRoom.getRoomId(), context.getUid());
		if (chatRoomMember == null || chatRoomMember.getState() == MemberStateType.Remove.ordinal()) {
			// 不在房间
			return ErrorResult.errorResult(HandleErrorType.MemberNoInRoom);
		}
		SendMessageResult result = defaultSendMessage(context, chatRoom.getRoomId());
		result.setChatRoom(chatRoom);

		// 组聊按用户分组翻译
		OnlineUserInfo onlineUserInfo = chatMService.getRoomOnlineUserInfo(chatRoom.getRoomId(), true);
		// 设置需要通知
		result.setOnlineUserInfo(onlineUserInfo);
		return result;
	}
}
