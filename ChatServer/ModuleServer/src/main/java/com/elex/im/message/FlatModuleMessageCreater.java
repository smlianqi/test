package com.elex.im.message;

import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.util.ByteBufferUtil;
import com.elex.im.data.chatmessage.ChatMessage;
import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.data.chatroommember.ChatRoomMember;
import com.elex.im.data.chatuser.ChatUser;
import com.elex.im.message.flat.*;
import com.elex.im.module.serverchat.module.chat.room.result.CreateRoomResult;
import com.elex.im.module.serverchat.module.chat.room.result.GainMessageResult;
import com.elex.im.module.serverchat.module.chat.room.result.ModifyMemberResult;
import com.elex.im.module.serverchat.module.chat.type.GainType;
import com.elex.im.module.serverchat.module.chat.type.MemberModifyType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;
import com.elex.im.module.serverclient.request.SendMultiChatReq;
import com.elex.im.module.serverclient.request.SendSingleChatReq;
import com.elex.im.module.serverclient.request.SendWorldChatReq;
import com.google.flatbuffers.FlatBufferBuilder;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FlatModuleMessageCreater implements AModuleMessageCreater {
	@Override
	public MegProtocolType getMegProtocolType() {
		return MegProtocolType.flat;
	}

	// =============================================
	// 用户模块
	@Override
	public IMessage createUserLoginUp(long userId, int userType, String languageType) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int languageTypeOffset = builder.createString(languageType);

		UserLoginUp.startUserLoginUp(builder);
		UserLoginUp.addUserId(builder, userId);
		UserLoginUp.addUserType(builder, userType);
		UserLoginUp.addLanguageType(builder, languageTypeOffset);

		int john = UserLoginUp.endUserLoginUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), UserLoginUp.class, userId);
		return msg;
	}

	@Override
	public IMessage createUserLogoutUp(long userId) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		UserLogoutUp.startUserLogoutUp(builder);
		UserLogoutUp.addUserId(builder, userId);

		int john = UserLogoutUp.endUserLogoutUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), UserLogoutUp.class, userId);
		return msg;
	}

	@Override
	public IMessage createModifyLanguageUp(long userId, String languageType) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int languageTypeOffset = builder.createString(languageType);

		ModifyLanguageUp.startModifyLanguageUp(builder);
		ModifyLanguageUp.addLanguageType(builder, languageTypeOffset);
		int john = ModifyLanguageUp.endModifyLanguageUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), ModifyLanguageUp.class, userId);
		return msg;
	}

	@Override
	public IMessage createUserLoginDown(long userId, int userType, String functionType, String sid) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int functionTypeOffset = builder.createString(functionType);
		int sidOffset = builder.createString(sid);

		UserLoginDown.startUserLoginDown(builder);
		UserLoginDown.addFunctionType(builder, functionTypeOffset);
		UserLoginDown.addSid(builder, sidOffset);
		int john = UserLoginDown.endUserLoginDown(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), UserLoginDown.class, userId);
		return msg;
	}

	// =============================================
	@Override
	public IMessage createErrorMessageDown(Long userId, int commandId, int errorDetail, int errorType) {
		FlatBufferBuilder builder = createErrorMessageDown(commandId, errorDetail, errorType);

		IMessage msg = null;
		if (userId == null) {
			msg = Message.createFlatMessage(builder.dataBuffer(), ErrorMessageDown.class);
		} else {
			msg = Message.createFlatMessage(builder.dataBuffer(), ErrorMessageDown.class, userId);
		}
		return msg;
	}

	@Override
	public IMessage createSuccessMessageDown(Long userId, int commandId) {
		FlatBufferBuilder builder = createSuccessMessageDown(commandId);
		IMessage msg = null;
		if (userId == null) {
			msg = Message.createFlatMessage(builder.dataBuffer(), SuccessMessageDown.class);
		} else {
			msg = Message.createFlatMessage(builder.dataBuffer(), SuccessMessageDown.class, userId);
		}

		return msg;
	}

	// -------------------------------
	// 修改聊天室成员
	@Override
	public IMessage createManagerChatRoomMemberUp(long userId, String roomId, MemberModifyType memberModifyType,
			List<Long> members) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int[] data = new int[members.size()];
		int i = 0;
		for (Long member : members) {
			data[i] = builder.createString(String.valueOf(member));
			i++;
		}
		int roomIdOffset = builder.createString(roomId);
		int uidOffset = ManagerChatRoomMemberUp.createUidVector(builder, data);

		ManagerChatRoomMemberUp.startManagerChatRoomMemberUp(builder);
		ManagerChatRoomMemberUp.addModifyType(builder, memberModifyType.ordinal());
		ManagerChatRoomMemberUp.addRoomId(builder, roomIdOffset);
		ManagerChatRoomMemberUp.addUid(builder, uidOffset);
		int john = ManagerChatRoomMemberUp.endManagerChatRoomMemberUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), ManagerChatRoomMemberUp.class, userId);
		return msg;
	}

	// 创建房间
	@Override
	public IMessage createCreateChatRoomUp(long userId, int roomType, String roomId, String admin, List<Long> members) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int adminOffset = builder.createString(admin);
		int roomIdOffset = builder.createString(roomId);
		int[] data = new int[members.size()];
		int i = 0;
		for (Long member : members) {
			data[i] = builder.createString(String.valueOf(member));
			i++;
		}
		int uidOffset = CreateChatRoomUp.createUidVector(builder, data);

		CreateChatRoomUp.startCreateChatRoomUp(builder);
		CreateChatRoomUp.addAdmin(builder, adminOffset);
		CreateChatRoomUp.addRoomId(builder, roomIdOffset);
		CreateChatRoomUp.addRoomType(builder, roomType);
		CreateChatRoomUp.addUid(builder, uidOffset);
		int john = CreateChatRoomUp.endCreateChatRoomUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), CreateChatRoomUp.class, userId);
		return msg;
	}

	// 发送单聊
	@Override
	public IMessage createSendSingleChatMessageUp(long userId, SendSingleChatReq req) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int contentOffset = builder.createString(req.getContent());
		int uidOffset = builder.createString(String.valueOf(req.getUid()));
		int clientExtOffset = builder.createString(req.getClientExt());
		int serverExtOffset = builder.createString(req.getServerExt());
		int targetUidOffset = builder.createString(String.valueOf(req.getTargetUid()));

		SendChatMessageUp.startSendChatMessageUp(builder);
		SendChatMessageUp.addUid(builder, uidOffset);
		SendChatMessageUp.addContentType(builder, req.getContentType().ordinal());
		SendChatMessageUp.addContent(builder, contentOffset);
		SendChatMessageUp.addSendedTime(builder, req.getSendedTime());
		SendChatMessageUp.addClientExt(builder, clientExtOffset);
		SendChatMessageUp.addServerExt(builder, serverExtOffset);
		SendChatMessageUp.addRoomType(builder, req.getRoomType().getValue());

		SendChatMessageUp.addTargetUid(builder, targetUidOffset);
		int john = SendChatMessageUp.endSendChatMessageUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), SendChatMessageUp.class, userId);
		return msg;
	}

	private FlatBufferBuilder createCreateChatRoomDown(int roomType, String roomId, String admin,
			List<String> members) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int uidOffset = 0;
		if (members != null) {
			int[] data = new int[members.size()];
			int i = 0;
			for (String member : members) {
				data[i] = builder.createString(member);
				i++;
			}
			uidOffset = ManagerChatRoomMemberDown.createUidVector(builder, data);
		}

		int roomIdOffset = builder.createString(roomId);

		CreateChatRoomDown.startCreateChatRoomDown(builder);
		CreateChatRoomDown.addRoomType(builder, roomType);
		CreateChatRoomDown.addRoomId(builder, roomIdOffset);
		if (uidOffset > 0) {
			CreateChatRoomDown.addUid(builder, uidOffset);
		}
		int john = CreateChatRoomDown.endCreateChatRoomDown(builder);
		builder.finish(john);
		return builder;
	}

	// 发送群聊
	@Override
	public IMessage createSendMultiChatMessageUp(long userId, SendMultiChatReq req) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int contentOffset = builder.createString(req.getContent());
		int roomIdOffset = builder.createString(req.getRoomId());
		int uidOffset = builder.createString(String.valueOf(req.getUid()));
		int clientExtOffset = builder.createString(req.getClientExt());
		int serverExtOffset = builder.createString(req.getServerExt());
		int atUidsOffset = 0;
		if (req.getAtUids() != null) {
			int[] data = new int[req.getAtUids().size()];
			int i = 0;
			for (Long atUid : req.getAtUids()) {
				data[i] = builder.createString(String.valueOf(atUid));
				i++;
			}
			atUidsOffset = SendChatMessageUp.createAtUidsVector(builder, data);
		}

		SendChatMessageUp.startSendChatMessageUp(builder);
		SendChatMessageUp.addUid(builder, uidOffset);
		SendChatMessageUp.addContentType(builder, req.getContentType().ordinal());
		SendChatMessageUp.addContent(builder, contentOffset);
		SendChatMessageUp.addSendedTime(builder, req.getSendedTime());
		SendChatMessageUp.addClientExt(builder, clientExtOffset);
		SendChatMessageUp.addServerExt(builder, serverExtOffset);
		SendChatMessageUp.addRoomType(builder, req.getRoomType().getValue());

		SendChatMessageUp.addRoomId(builder, roomIdOffset);
		if (atUidsOffset > 0) {
			SendChatMessageUp.addAtUids(builder, atUidsOffset);
		}
		int john = SendChatMessageUp.endSendChatMessageUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), SendChatMessageUp.class, userId);
		return msg;
	}

	// 发送世界聊
	@Override
	public IMessage createSendWorldChatMessageUp(long userId, SendWorldChatReq req) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int contentOffset = builder.createString(req.getContent());
		int roomIdOffset = builder.createString(req.getRoomId());
		int clientExtOffset = builder.createString(req.getClientExt());
		int serverExtOffset = builder.createString(req.getServerExt());
		int uidOffset = builder.createString(String.valueOf(req.getUid()));

		SendChatMessageUp.startSendChatMessageUp(builder);
		SendChatMessageUp.addUid(builder, uidOffset);
		SendChatMessageUp.addContentType(builder, req.getContentType().ordinal());
		SendChatMessageUp.addContent(builder, contentOffset);
		SendChatMessageUp.addSendedTime(builder, req.getSendedTime());
		SendChatMessageUp.addClientExt(builder, clientExtOffset);
		SendChatMessageUp.addServerExt(builder, serverExtOffset);
		SendChatMessageUp.addRoomType(builder, req.getRoomType().getValue());

		SendChatMessageUp.addRoomId(builder, roomIdOffset);
		int john = SendChatMessageUp.endSendChatMessageUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), SendChatMessageUp.class, userId);
		return msg;
	}

	// -------------------------------
	@Override
	public byte[] createSendChatMessageDownBytes(ChatRoom chatRoom, ChatMessage chatMessage,
			Map<String, String> translate, RoomType roomType) {
		FlatBufferBuilder builder = createSendChatMessageDownBuilder(chatRoom, chatMessage, translate, roomType);
		byte[] data = ByteBufferUtil.byteBuffer2Bytes(builder.dataBuffer());
		return data;
	}

	// 聊天回复
	@Override
	public IMessage createSendChatMessageDown(long userId, ChatRoom chatRoom, ChatMessage chatMessage,
			Map<String, String> translate, RoomType roomType) {
		FlatBufferBuilder builder = createSendChatMessageDownBuilder(chatRoom, chatMessage, translate, roomType);
		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), SendChatMessageDown.class, userId);
		return msg;
	}

	@Override
	public IMessage createGainChatMessageDown(long userId, long clientSendedTime,
			List<GainMessageResult> resultChatRoomInfos) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int[] data = new int[resultChatRoomInfos.size()];
		int k = 0;
		for (GainMessageResult resultChatRoomInfo : resultChatRoomInfos) {
			int chatRoomOffset = 0, usersOffset = 0, chatMessageOffset = 0, selfOffset = 0;
			chatRoomOffset = createChatRoomInfo(resultChatRoomInfo.getChatRoom(), builder);
			if (resultChatRoomInfo.getSelf() != null) {
				selfOffset = createChatRoomMemberInfo(resultChatRoomInfo.getSelf(), builder);
			}

			if (resultChatRoomInfo.getChatUsers() != null) {
				int i = 0;
				int[] data2 = new int[resultChatRoomInfo.getChatUsers().size()];
				for (ChatUser chatUser : resultChatRoomInfo.getChatUsers()) {
					int offset = createChatUserInfo(chatUser, builder);
					data2[i] = offset;
					i++;
				}
				usersOffset = ChatRoomMessageInfo.createUsersVector(builder, data2);
			}

			if (resultChatRoomInfo.getChatMessages() != null) {
				int i = 0;
				int[] data2 = new int[resultChatRoomInfo.getChatMessages().size()];
				for (ChatMessage chatMessage : resultChatRoomInfo.getChatMessages()) {
					// TODO 暂时没有翻译
					int offset = createChatMessageInfo(chatMessage, null, builder);
					data2[i] = offset;
					i++;
				}
				chatMessageOffset = ChatRoomMessageInfo.createChatMessageVector(builder, data2);
			}
			ChatRoomMessageInfo.startChatRoomMessageInfo(builder);
			ChatRoomMessageInfo.addChatRoom(builder, chatRoomOffset);
			if (selfOffset > 0) {
				ChatRoomMessageInfo.addSelf(builder, selfOffset);
			}
			if (usersOffset > 0) {
				ChatRoomMessageInfo.addUsers(builder, usersOffset);
			}
			if (chatMessageOffset > 0) {
				ChatRoomMessageInfo.addLastOrder(builder, resultChatRoomInfo.getLastOrder());
				ChatRoomMessageInfo.addChatMessage(builder, chatMessageOffset);
			}
			data[k] = ChatRoomMessageInfo.endChatRoomMessageInfo(builder);
			k++;
		}
		int chatRoomMessageOffset = GainChatMessageDown.createChatRoomMessageVector(builder, data);

		GainChatMessageDown.startGainChatMessageDown(builder);
		GainChatMessageDown.addChatRoomMessage(builder, chatRoomMessageOffset);
		int john = GainChatMessageDown.endGainChatMessageDown(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), GainChatMessageDown.class);
		return msg;
	}

	@Override public IMessage createLastMessageOrderInChatDown(long userId, Map<String, String> data) {
		return null;
	}

	@Override
	public IMessage createGainChatMessageDown(long userId) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		GainChatMessageDown.startGainChatMessageDown(builder);
		int john = GainChatMessageDown.endGainChatMessageDown(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), GainChatMessageDown.class);
		return msg;
	}

	@Override
	public IMessage createManagerChatRoomMemberDown(long userId, ModifyMemberResult result) {
		FlatBufferBuilder builder = createManagerChatRoomMemberDown(result.getChatRoom().getRoomId(),
				result.getModifyType(), result.getUids(), result.getChatRoom().getRoomType());
		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), ManagerChatRoomMemberDown.class, userId);
		return msg;
	}

	@Override
	public byte[] createManagerChatRoomMemberDownBytes(ModifyMemberResult result) {
		FlatBufferBuilder builder = createManagerChatRoomMemberDown(result.getChatRoom().getRoomId(),
				result.getModifyType(), result.getUids(), result.getChatRoom().getRoomType());
		return ByteBufferUtil.byteBuffer2Bytes(builder.dataBuffer());
	}

	@Override
	public IMessage createCreateChatRoomDown(long userId, CreateRoomResult result) {
		FlatBufferBuilder builder = createCreateChatRoomDown(result.getChatRoom().getRoomType(),
				result.getChatRoom().getRoomId(), result.getChatRoom().getAdmin(), result.getUids());
		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), CreateChatRoomDown.class, userId);
		return msg;
	}

	@Override
	public byte[] createCreateChatRoomDownBytes(long userId, CreateRoomResult result) {
		FlatBufferBuilder builder = createCreateChatRoomDown(result.getChatRoom().getRoomType(),
				result.getChatRoom().getRoomId(), result.getChatRoom().getAdmin(), result.getUids());
		return ByteBufferUtil.byteBuffer2Bytes(builder.dataBuffer());
	}

	@Override
	public IMessage createTranslationMessageUp(long userId, String roomId, int roomType, long order) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int roomIdOffset = builder.createString(roomId);

		TranslationMessageUp.startTranslationMessageUp(builder);
		TranslationMessageUp.addOrder(builder, order);
		TranslationMessageUp.addRoomId(builder, roomIdOffset);
		TranslationMessageUp.addRoomType(builder, roomType);
		TranslationMessageUp.endTranslationMessageUp(builder);

		int john = CreateChatRoomDown.endCreateChatRoomDown(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), TranslationMessageUp.class, userId);
		return msg;

	}

	@Override
	public IMessage createTranslationMessageDown(long userId, ChatMessage chatMessage, Map<String, String> translate,
			int roomType, boolean isActive) {
		FlatBufferBuilder builder = createTranslationMessageDown(chatMessage, translate, roomType, isActive);
		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), TranslationMessageDown.class, userId);
		return msg;
	}

	@Override
	public byte[] createTranslationMessageDownBytes(ChatMessage chatMessage, Map<String, String> translate,
			int roomType, boolean isActive) {
		FlatBufferBuilder builder = createTranslationMessageDown(chatMessage, translate, roomType, isActive);
		byte[] data = ByteBufferUtil.byteBuffer2Bytes(builder.dataBuffer());
		return data;
	}

	// -------------------------------
	private FlatBufferBuilder createErrorMessageDown(int commandId, int errorDetail, int errorType) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);
		ErrorMessageDown.startErrorMessageDown(builder);
		ErrorMessageDown.addCommandId(builder, commandId);
		ErrorMessageDown.addErrorDetail(builder, errorDetail);
		ErrorMessageDown.addErrorType(builder, errorType);
		int john = ErrorMessageDown.endErrorMessageDown(builder);
		builder.finish(john);
		return builder;
	}

	private FlatBufferBuilder createSuccessMessageDown(int commandId) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);
		SuccessMessageDown.startSuccessMessageDown(builder);
		SuccessMessageDown.addCommandId(builder, commandId);
		int john = SuccessMessageDown.endSuccessMessageDown(builder);
		builder.finish(john);
		return builder;
	}

	private FlatBufferBuilder createTranslationMessageDown(ChatMessage chatMessage, Map<String, String> translate,
			int roomType, boolean isActive) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int roomIdOffset = builder.createString(chatMessage.getRoomId());
		int translationOffset = 0;
		{
			if (translate != null) {
				int[] data = new int[translate.size()];
				int i = 0;
				for (Entry<String, String> entry : translate.entrySet()) {
					int languageTypeOffset = builder.createString(entry.getKey());
					int translationContentOffset = builder.createString(entry.getValue());

					MessageContent.startMessageContent(builder);
					MessageContent.addContent(builder, translationContentOffset);
					MessageContent.addLanguageType(builder, languageTypeOffset);
					data[i] = MessageContent.endMessageContent(builder);
					i++;
				}
				translationOffset = ChatMessageInfo.createTranslationVector(builder, data);
			}
		}

		TranslationMessageDown.startTranslationMessageDown(builder);
		TranslationMessageDown.addOrder(builder, chatMessage.getOrderId());
		TranslationMessageDown.addRoomId(builder, roomIdOffset);
		if (translationOffset > 0) {
			TranslationMessageDown.addTranslation(builder, translationOffset);
		}
		TranslationMessageDown.addRoomType(builder, roomType);
		TranslationMessageDown.addIsActive(builder, isActive);
		int john = TranslationMessageDown.endTranslationMessageDown(builder);
		builder.finish(john);

		return builder;
	}

	private int createChatUserInfo(ChatUser entity, FlatBufferBuilder builder) {
		int uidOffset = builder.createString(entity.getUid());

		int languageTypeOffset = builder.createString(entity.getLanguageType());

		ChatUserInfo.startChatUserInfo(builder);
		ChatUserInfo.addUid(builder, uidOffset);
		ChatUserInfo.addUserType(builder, entity.getUserType());
		ChatUserInfo.addLanguageType(builder, languageTypeOffset);
		return ChatUserInfo.endChatUserInfo(builder);
	}

	// 聊天模块
	private int createChatRoomMemberInfo(ChatRoomMember entity, FlatBufferBuilder builder) {
		int uidOffset = builder.createString(entity.getUid());
		int roomIdOffset = builder.createString(entity.getRoomId());

		int atOrdersOffset = 0;
		if (entity.getAtOrders() != null) {
			long[] data = new long[entity.getAtOrders().size()];
			int i = 0;
			for (long d : entity.getAtOrders()) {
				data[i] = d;
			}
			atOrdersOffset = ChatRoomMemberInfo.createAtOrdersVector(builder, data);
		}

		ChatRoomMemberInfo.startChatRoomMemberInfo(builder);
		if (atOrdersOffset > 0) {
			ChatRoomMemberInfo.addAtOrders(builder, atOrdersOffset);
		}
		ChatRoomMemberInfo.addLastOrder(builder, entity.getLastOrder());
		ChatRoomMemberInfo.addRoomId(builder, roomIdOffset);
		ChatRoomMemberInfo.addState(builder, entity.getState());
		ChatRoomMemberInfo.addUid(builder, uidOffset);
		return ChatRoomMemberInfo.endChatRoomMemberInfo(builder);
	}

	private int createChatRoomInfo(ChatRoom entity, FlatBufferBuilder builder) {
		int roomIdOffset = builder.createString(entity.getRoomId());
		int adminOffset = builder.createString(entity.getAdmin());
		int nameOffset = builder.createString(entity.getName());

		ChatRoomInfo.startChatRoomInfo(builder);
		ChatRoomInfo.addRoomId(builder, roomIdOffset);
		ChatRoomInfo.addRoomType(builder, entity.getRoomType());
		ChatRoomInfo.addAdmin(builder, adminOffset);
		ChatRoomInfo.addCreateTime(builder, entity.getCreateTime());
		ChatRoomInfo.addName(builder, nameOffset);
		return ChatRoomInfo.endChatRoomInfo(builder);
	}

	private int createChatMessageInfo(ChatMessage entity, Map<String, String> translate, FlatBufferBuilder builder) {
		int uidOffset = builder.createString(entity.getUid());
		int roomIdOffset = builder.createString(entity.getRoomId());
		int contentOffset = builder.createString(entity.getContent());
		int clientExtOffset = builder.createString(entity.getClientExt());
		int serverExtOffset = builder.createString(entity.getServerExt());
		int atUidsOffset, translationOffset = 0;
		{
			int[] data = new int[entity.getAtUidsSet().size()];
			int i = 0;
			for (String atUid : entity.getAtUidsSet()) {
				data[i] = builder.createString(atUid);
				i++;
			}
			atUidsOffset = ChatMessageInfo.createAtUidsVector(builder, data);
		}
		{
			if (translate != null) {
				int[] data = new int[translate.size()];
				int i = 0;
				for (Entry<String, String> entry : translate.entrySet()) {
					int languageTypeOffset = builder.createString(entry.getKey());
					int translationContentOffset = builder.createString(entry.getValue());

					MessageContent.startMessageContent(builder);
					MessageContent.addContent(builder, translationContentOffset);
					MessageContent.addLanguageType(builder, languageTypeOffset);
					data[i] = MessageContent.endMessageContent(builder);
					i++;
				}
				translationOffset = ChatMessageInfo.createTranslationVector(builder, data);
			}
		}

		ChatMessageInfo.startChatMessageInfo(builder);
		if (translationOffset > 0) {
			ChatMessageInfo.addTranslation(builder, translationOffset);
		}
		if (atUidsOffset > 0) {
			ChatMessageInfo.addAtUids(builder, atUidsOffset);
		}
		ChatMessageInfo.addRoomId(builder, roomIdOffset);
		ChatMessageInfo.addOrder(builder, entity.getOrderId());
		ChatMessageInfo.addContentType(builder, entity.getContentType());
		ChatMessageInfo.addContent(builder, contentOffset);
		ChatMessageInfo.addUid(builder, uidOffset);
		ChatMessageInfo.addReceivedTime(builder, entity.getReceivedTime());
		ChatMessageInfo.addSendedTime(builder, entity.getSendedTime());
		ChatMessageInfo.addClientExt(builder, clientExtOffset);
		ChatMessageInfo.addServerExt(builder, serverExtOffset);
		return ChatMessageInfo.endChatMessageInfo(builder);
	}

	private FlatBufferBuilder createManagerChatRoomMemberDown(String roomId, MemberModifyType memberModifyType,
			List<String> members, int roomType) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int[] data = new int[members.size()];
		int i = 0;
		for (String member : members) {
			data[i] = builder.createString(member);
			i++;
		}
		int uidOffset = ManagerChatRoomMemberDown.createUidVector(builder, data);
		int roomIdOffset = builder.createString(roomId);

		ManagerChatRoomMemberDown.startManagerChatRoomMemberDown(builder);
		ManagerChatRoomMemberDown.addModifyType(builder, memberModifyType.ordinal());
		ManagerChatRoomMemberDown.addRoomId(builder, roomIdOffset);
		ManagerChatRoomMemberDown.addUid(builder, uidOffset);
		ManagerChatRoomMemberDown.addRoomType(builder, roomType);
		int john = ManagerChatRoomMemberDown.endManagerChatRoomMemberDown(builder);
		builder.finish(john);

		return builder;
	}

	private FlatBufferBuilder createSendChatMessageDownBuilder(ChatRoom chatRoom, ChatMessage chatMessage,
			Map<String, String> translate, RoomType roomType) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int chatRoomOffset = 0;
		if (chatRoom != null) {
			chatRoomOffset = createChatRoomInfo(chatRoom, builder);
		}
		int chatMessageOffset = 0;
		if (chatMessage != null) {
			chatMessageOffset = createChatMessageInfo(chatMessage, translate, builder);
		}

		SendChatMessageDown.startSendChatMessageDown(builder);
		if (chatRoomOffset > 0) {
			SendChatMessageDown.addChatRoom(builder, chatRoomOffset);
		}
		if (chatMessageOffset > 0) {
			SendChatMessageDown.addChatMessage(builder, chatMessageOffset);
		}
		SendChatMessageDown.addRoomType(builder, roomType.getValue());

		int john = SendChatMessageDown.endSendChatMessageDown(builder);
		builder.finish(john);

		return builder;
	}

	@Override
	public IMessage createGainAllNewestChatMessageUp(long userId, long clienSendedTime, String regionRoomId,
			String unionRoomId) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int uidOffset = builder.createString(String.valueOf(userId));

		GainChatMessageUp.startGainChatMessageUp(builder);
		GainChatMessageUp.addUid(builder, uidOffset);
		GainChatMessageUp.addGainType(builder, GainType.AllNewest.getValue());
		// TODO 没弄完
		int john = CreateChatRoomUp.endCreateChatRoomUp(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), GainChatMessageUp.class, userId);
		return msg;
	}

	@Override
	public IMessage createGainRoomMultiChatMessageUp(long userId, long clienSendedTime, String roomId,
			RoomType roomType, List<Long> orders) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMessage createGainRoomPageChatMessageUp(long userId, long clienSendedTime, String roomId, RoomType roomType,
			long order, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMessage createGainRoomNewestChatMessageUp(long userId, long clienSendedTime, String roomId,
			RoomType roomType) {
		// TODO Auto-generated method stub
		return null;
	}
}
