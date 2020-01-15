package com.elex.im.module.serverchat.module.chat.room;

import com.elex.im.data.chatmessage.ChatMessage;
import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.data.chatroommember.ChatRoomMember;
import com.elex.im.data.chatuser.ChatUser;
import com.elex.im.module.HandleErrorType;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.chat.room.request.CreateChatRoomReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.GainChatMessageReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.ManagerChatRoomMemberReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.SendMessageReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.AllNewestCondition;
import com.elex.im.module.serverchat.module.chat.room.result.*;
import com.elex.im.module.serverchat.module.chat.type.MemberStateType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;

import java.util.Arrays;
import java.util.List;

/**
 * 单聊
 * 
 * @author mausmars
 *
 */
public class SingleRoom extends AChatRoom {
	private String SingleRoomId_Template = "%s_%s";

	public SingleRoom(ChatMService chatMService) {
		super(chatMService);
	}

	@Override
	public RoomResult createRoom(CreateChatRoomReqContext context) {
		if (context.getUids().size() != 2) {
			return ErrorResult.errorResult(HandleErrorType.MemberNumError);
		}
		// 单聊创建聊天室id规则
		String roomId = createSingleRoomId(context.getUids().get(0), context.getUids().get(1));
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
		// 单聊房间不支持修改操作
		return ErrorResult.errorResult(HandleErrorType.UnsupportedOperations);
	}

	/** 获取聊天室信息 */
	@Override
	public GainMessageResult gainChatMessage(GainChatMessageReqContext context, ChatRoom chatRoom,
			ChatRoomMember self) {
		GainMessageResult result = null;

		switch (context.getGainType()) {
		case AllNewest:
			AllNewestCondition condition = context.getCondition();
			result = defaultGainNewChatMessage(context, chatRoom, self,condition.getSingleRoomFetchSize());
			// 取其他成员信息
			List<ChatUser> chatUsers = chatMService.getChatRoomMemberAllChatUser(chatRoom.getRoomId());
			result.setChatUsers(chatUsers);
			result.setChatRoom(chatRoom);
			break;
		case RoomNewest:
			result = defaultGainNewChatMessage(context, chatRoom, self);
			// 取其他成员信息
			List<ChatUser> chatUsers2 = chatMService.getChatRoomMemberAllChatUser(chatRoom.getRoomId());
			result.setChatUsers(chatUsers2);
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
		result.setRoomType(RoomType.Single.getValue());
		return result;
	}
	// 获取最新的消息(指定了fetchSize)
	public GainMessageResult defaultGainNewChatMessage(GainChatMessageReqContext context, ChatRoom chatRoom,
			ChatRoomMember self, int fetchSize) {
		GainMessageResult result = new GainMessageResult();
		result.setChatRoom(chatRoom);
		result.setSelf(self);

		if (self.getState() == MemberStateType.Remove.ordinal()) {
			// 不再获取
			return result;
		}
		int count = NewCountMax;
		if(fetchSize > 0 && fetchSize < NewCountMax){
			count = fetchSize;
		}
		long maxOrder = chatMService.getMaxOrder(self.getRoomId());
		result.setLastOrder(self.getLastOrder());
		self.setLastOrder(maxOrder);
		// 更新最新的order值
		chatMService.updateChatRoomMember(self);

		// 获取聊天信息
		List<ChatMessage> chatMessages = chatMService.getNewChatMessages(self.getRoomId(), count);
		result.setChatMessages(chatMessages);

		return result;
	}

	/** 发送个人聊天消息 */
	@Override
	public RoomResult sendMessage(SendMessageReqContext context) {
		String targetUid = context.getTargetUid();
		String sourceUid = context.getUid();

		String roomId = createSingleRoomId(sourceUid, targetUid);
		context.setRoomId(roomId);

		ChatRoom chatRoom = chatMService.getChatRoom(roomId);
		if (chatRoom == null) {
			// 创建房间
			chatRoom = chatMService.createChatRoom(roomId, RoomType.Single, "", Room_Name);
			// 插入
			chatMService.insertChatRoom(chatRoom);
		}
		// 这里可能给自己发。考虑这种情况
		chatMService.getCreateChatRoomMember(roomId, sourceUid);
		if (sourceUid != targetUid) {
			chatMService.getCreateChatRoomMember(roomId, targetUid);
		}
		SendMessageResult result = defaultSendMessage(context, roomId);
		result.setChatRoom(chatRoom);

		OnlineUserInfo onlineUserInfo = chatMService.getOnlineUserInfo(targetUid, true);
		result.setOnlineUserInfo(onlineUserInfo);
		return result;
	}

	private String createSingleRoomId(String sourceUid, String targetUid) {
		String[] strs = new String[] { sourceUid, targetUid };
		Arrays.sort(strs);
		return String.format(SingleRoomId_Template, strs);
	}
}
