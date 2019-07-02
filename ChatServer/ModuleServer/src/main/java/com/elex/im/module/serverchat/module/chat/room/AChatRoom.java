package com.elex.im.module.serverchat.module.chat.room;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.data.chatmessage.ChatMessage;
import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.data.chatroommember.ChatRoomMember;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.chat.room.request.CreateChatRoomReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.GainChatMessageReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.ManagerChatRoomMemberReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.SendMessageReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.RoomMultiCondition;
import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.RoomPageCondition;
import com.elex.im.module.serverchat.module.chat.room.result.CreateRoomResult;
import com.elex.im.module.serverchat.module.chat.room.result.GainMessageResult;
import com.elex.im.module.serverchat.module.chat.room.result.ModifyMemberResult;
import com.elex.im.module.serverchat.module.chat.room.result.SendMessageResult;
import com.elex.im.module.serverchat.module.chat.type.MemberModifyType;
import com.elex.im.module.serverchat.module.chat.type.MemberStateType;

import java.util.LinkedList;
import java.util.List;

public abstract class AChatRoom implements IChatRoomHandler {
	protected static final ILogger logger = XLogUtil.logger();

	protected ChatMService chatMService;

	protected String Room_Name = "room";
	protected int NewCountMax = 20;// 第一次获取取最新的20条

	public AChatRoom(ChatMService chatMService) {
		this.chatMService = chatMService;
	}

	protected CreateRoomResult defaultCreateRoom(CreateChatRoomReqContext context) {
		ChatRoom chatRoom = chatMService.createChatRoom(context.getRoomId(), context.getRoomType(), context.getAdmin(),
				Room_Name);
		chatMService.insertChatRoom(chatRoom);

		if (context.getUids() != null) {
			List<ChatRoomMember> chatRoomMembers = new LinkedList<>();
			for (String uid : context.getUids()) {
				ChatRoomMember chatRoomMember = chatMService.createChatRoomMember(context.getRoomId(), uid, 0l);
				chatRoomMembers.add(chatRoomMember);
			}
			// 批量插入
			chatMService.batchInsertChatRoomMember(chatRoomMembers);
		}
		// 返回创建成功
		CreateRoomResult result = new CreateRoomResult();
		result.setChatRoom(chatRoom);
		result.setUids(context.getUids());
		return result;
	}

	protected ModifyMemberResult defaultModifyMember(ManagerChatRoomMemberReqContext context, ChatRoom chatRoom) {
		String roomId = chatRoom.getRoomId();
		switch (context.getModifyType()) {
		case Insert: {
			long maxOrder = chatMService.getMaxOrder(roomId);
			for (String uid : context.getUids()) {
				ChatRoomMember chatRoomMember = chatMService.getChatRoomMember(roomId, uid);
				if (chatRoomMember == null) {
					chatRoomMember = chatMService.createChatRoomMember(roomId, uid, maxOrder);
					// 插入
					chatMService.insertChatRoomMember(chatRoomMember);
				} else if (chatRoomMember.getState() == MemberStateType.Remove.ordinal()) {
					chatRoomMember.setState(MemberStateType.Normal.ordinal());
					chatRoomMember.setLastOrder(maxOrder);
					// 更新
					chatMService.updateChatRoomMember(chatRoomMember);
				}
				// 加入房间在线
				chatMService.modifyRoomUserOnline(uid, roomId, MemberModifyType.Insert);
			}
			break;
		}
		case Remove: {
			for (String uid : context.getUids()) {
				ChatRoomMember chatRoomMember = chatMService.getChatRoomMember(roomId, uid);
				if (chatRoomMember != null && chatRoomMember.getState() != MemberStateType.Remove.ordinal()) {
					// 设置为移除
					chatRoomMember.setState(MemberStateType.Remove.ordinal());
					// 更新
					chatMService.updateChatRoomMember(chatRoomMember);
				}
				// 移除房间在线
				chatMService.modifyRoomUserOnline(uid, roomId, MemberModifyType.Remove);
			}
			break;
		}
		default:
			break;
		}
		// 返回创建成功
		ModifyMemberResult result = new ModifyMemberResult();
		result.setModifyType(context.getModifyType());
		result.setChatRoom(chatRoom);
		result.setUids(context.getUids());
		return result;
	}

	/** 发送群聊天消息 */
	protected SendMessageResult defaultSendMessage(SendMessageReqContext context, String roomId) {
		// 消息
		ChatMessage chatMessage = chatMService.createChatMessage(context.getRoomId(), context.getUid(),
				context.getContent(), context.getAtUids(), context.getClientSendedTime(), context.getClientExt(),
				context.getServerExt());
		// 插入消息
		chatMService.insertChatMessage(chatMessage);

		SendMessageResult result = new SendMessageResult();
		result.setChatMessage(chatMessage);
		result.setContent(context.getContent());
		result.setRoomType(context.getRoomType());
		return result;
	}

	// 获取最新的消息
	protected GainMessageResult defaultGainNewChatMessage(GainChatMessageReqContext context, ChatRoom chatRoom,
			ChatRoomMember self) {
		GainMessageResult result = new GainMessageResult();
		result.setChatRoom(chatRoom);
		result.setSelf(self);

		if (self.getState() == MemberStateType.Remove.ordinal()) {
			// 不再获取
			return result;
		}

		/* 不再需要（lastOrder字段该做客户端用来通知服务端已经读到哪一条数据的记录） by huangyuanqaing
		long maxOrder = chatMService.getMaxOrder(self.getRoomId());
		result.setLastOrder(self.getLastOrder());
		self.setLastOrder(maxOrder);
		// 更新最新的order值
		chatMService.updateChatRoomMember(self);
		*/
		// 获取聊天信息
		List<ChatMessage> chatMessages = chatMService.getNewChatMessages(self.getRoomId(), NewCountMax);
		result.setChatMessages(chatMessages);

		return result;
	}

	protected GainMessageResult defaultGainNewChatMessage(GainChatMessageReqContext context, String roomId) {
		GainMessageResult result = new GainMessageResult();
		// 获取聊天信息
		List<ChatMessage> chatMessages = chatMService.getNewChatMessages(roomId, NewCountMax);
		result.setChatMessages(chatMessages);
		return result;
	}

	protected GainMessageResult defaultGainMultiChatMessage(GainChatMessageReqContext context, String roomId) {
		GainMessageResult result = new GainMessageResult();

		RoomMultiCondition condition = context.getCondition();
		// 获取聊天信息
		List<Long> orders = condition.getOrder();
		List<ChatMessage> chatMessages = chatMService.getChatMessagesByRoomIdAndOrders(roomId, orders);
		result.setChatMessages(chatMessages);
		return result;
	}

	protected GainMessageResult defaultGainPageChatMessage(GainChatMessageReqContext context, String roomId) {
		GainMessageResult result = new GainMessageResult();

		RoomPageCondition condition = context.getCondition();
		long order = condition.getOrder();
		int pageCount = condition.getCount();
		// 获取聊天信息
		List<ChatMessage> chatMessages = chatMService.getChatMessagesByRoomIdAndOrderPage(roomId, order, pageCount);
		result.setChatMessages(chatMessages);
		return result;
	}
}
