package com.elex.im.message;

import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.util.json.JsonUtil;
import com.elex.im.data.chatmessage.ChatMessage;
import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.data.chatroommember.ChatRoomMember;
import com.elex.im.data.chatuser.ChatUser;
import com.elex.im.message.proto.ChatMessage.*;
import com.elex.im.message.proto.ErrorMessage.ErrorMessageDown;
import com.elex.im.message.proto.ErrorMessage.SuccessMessageDown;
import com.elex.im.message.proto.UserMessage.ModifyLanguageUp;
import com.elex.im.message.proto.UserMessage.UserLoginDown;
import com.elex.im.message.proto.UserMessage.UserLoginUp;
import com.elex.im.message.proto.UserMessage.UserLogoutUp;
import com.elex.im.module.serverchat.module.chat.room.result.CreateRoomResult;
import com.elex.im.module.serverchat.module.chat.room.result.GainMessageResult;
import com.elex.im.module.serverchat.module.chat.room.result.ModifyMemberResult;
import com.elex.im.module.serverchat.module.chat.type.GainType;
import com.elex.im.module.serverchat.module.chat.type.MemberModifyType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;
import com.elex.im.module.serverclient.request.SendMultiChatReq;
import com.elex.im.module.serverclient.request.SendSingleChatReq;
import com.elex.im.module.serverclient.request.SendWorldChatReq;
import com.elex.im.module.translation.type.LanguageType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PrototModuleMessageCreater implements AModuleMessageCreater {
	@Override
	public MegProtocolType getMegProtocolType() {
		return MegProtocolType.proto;
	}

	// =============================================
	// 用户模块
	@Override
	public IMessage createUserLoginUp(long userId, int userType, String languageType) {
		UserLoginUp.Builder builder = UserLoginUp.newBuilder();
		builder.setUserId(userId);
		builder.setUserType(userType);
		builder.setLanguageType(languageType);
		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	@Override
	public IMessage createUserLogoutUp(long userId) {
		UserLogoutUp.Builder builder = UserLogoutUp.newBuilder();
		builder.setUserId(userId);

		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	@Override
	public IMessage createModifyLanguageUp(long userId, String languageType) {
		ModifyLanguageUp.Builder builder = ModifyLanguageUp.newBuilder();
		builder.setLanguageType(languageType);

		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	@Override
	public IMessage createUserLoginDown(long userId, int userType, String functionType, String sid) {
		UserLoginDown.Builder builder = UserLoginDown.newBuilder();
		builder.setUserType(userType);
		builder.setFunctionType(functionType);
		builder.setSid(sid);
		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	// =============================================
	@Override
	public IMessage createErrorMessageDown(Long userId, int commandId, int errorDetail, int errorType) {
		ErrorMessageDown.Builder builder = createErrorMessageDown(commandId, errorDetail, errorType);
		IMessage msg = null;
		if (userId == null) {
			msg = Message.createProtoMessage(builder.build());
		} else {
			msg = Message.createProtoMessage(builder.build(), userId);
		}
		return msg;
	}

	@Override
	public IMessage createSuccessMessageDown(Long userId, int commandId) {
		SuccessMessageDown.Builder builder = createSuccessMessageDown(commandId);
		IMessage msg = null;
		if (userId == null) {
			msg = Message.createProtoMessage(builder.build());
		} else {
			msg = Message.createProtoMessage(builder.build(), userId);
		}

		return msg;
	}

	// =============================================
	// 修改聊天室成员
	@Override
	public IMessage createManagerChatRoomMemberUp(long userId, String roomId, MemberModifyType memberModifyType,
			List<Long> members) {
		ManagerChatRoomMemberUp.Builder builder = ManagerChatRoomMemberUp.newBuilder();
		builder.setModifyType(memberModifyType.ordinal());
		builder.setRoomId(roomId);

		for (Long member : members) {
			builder.addUid(String.valueOf(member));
		}
		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	// 创建房间
	@Override
	public IMessage createCreateChatRoomUp(long userId, int roomType, String roomId, String admin, List<Long> members) {
		CreateChatRoomUp.Builder builder = CreateChatRoomUp.newBuilder();
		builder.setRoomType(roomType);
		builder.setAdmin(admin);
		builder.setRoomId(roomId);
		for (Long member : members) {
			builder.addUid(String.valueOf(member));
		}
		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	// ------------------------
	@Override
	public IMessage createGainAllNewestChatMessageUp(long userId, long clienSendedTime, String regionRoomId,
			String unionRoomId) {
		GainChatMessageUp.Builder builder = GainChatMessageUp.newBuilder();
		builder.setUid(String.valueOf(userId));
		builder.setSendedTime(clienSendedTime);
		builder.setGainType(GainType.AllNewest.getValue());

		AllNewestCondition.Builder b = AllNewestCondition.newBuilder();
		b.setRegionRoomId(regionRoomId);
		b.setUnionRoomId(unionRoomId);
		builder.setAllNewest(b);

		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	@Override
	public IMessage createGainRoomMultiChatMessageUp(long userId, long clienSendedTime, String roomId,
			RoomType roomType, List<Long> orders) {
		GainChatMessageUp.Builder builder = GainChatMessageUp.newBuilder();
		builder.setUid(String.valueOf(userId));
		builder.setSendedTime(clienSendedTime);
		builder.setGainType(GainType.RoomMulti.getValue());

		RoomMultiCondition.Builder b = RoomMultiCondition.newBuilder();
		b.setRoomId(roomId);
		b.setRoomType(roomType.getValue());
		b.addAllOrder(orders);
		builder.setRoomMulti(b);

		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	@Override
	public IMessage createGainRoomPageChatMessageUp(long userId, long clienSendedTime, String roomId, RoomType roomType,
			long order, int count) {
		GainChatMessageUp.Builder builder = GainChatMessageUp.newBuilder();
		builder.setUid(String.valueOf(userId));
		builder.setSendedTime(clienSendedTime);
		builder.setGainType(GainType.RoomPage.getValue());

		RoomPageCondition.Builder b = RoomPageCondition.newBuilder();
		b.setCount(count);
		b.setOrder(order);
		b.setRoomId(roomId);
		b.setRoomType(roomType.getValue());
		builder.setRoomPage(b);

		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	@Override
	public IMessage createGainRoomNewestChatMessageUp(long userId, long clienSendedTime, String roomId,
			RoomType roomType) {
		GainChatMessageUp.Builder builder = GainChatMessageUp.newBuilder();
		builder.setUid(String.valueOf(userId));
		builder.setSendedTime(clienSendedTime);
		builder.setGainType(GainType.RoomNewest.getValue());

		RoomNewestCondition.Builder b = RoomNewestCondition.newBuilder();
		b.setRoomId(roomId);
		b.setRoomType(roomType.getValue());
		builder.setRoomNewest(b);

		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	// ------------------------
	// 发送单聊
	@Override
	public IMessage createSendSingleChatMessageUp(long userId, SendSingleChatReq req) {
		SendChatMessageUp.Builder builder = SendChatMessageUp.newBuilder();
		builder.setUid(String.valueOf(req.getUid()));
		builder.setContentType(req.getContentType().ordinal());
		builder.setContent(req.getContent());
		builder.setSendedTime(req.getSendedTime());
		builder.setClientExt(req.getClientExt());
		builder.setServerExt(req.getServerExt());
		builder.setRoomType(req.getRoomType().getValue());

		builder.setTargetUid(String.valueOf(req.getTargetUid()));

		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	// 发送群聊
	@Override
	public IMessage createSendMultiChatMessageUp(long userId, SendMultiChatReq req) {
		SendChatMessageUp.Builder builder = SendChatMessageUp.newBuilder();
		builder.setUid(String.valueOf(req.getUid()));
		builder.setContentType(req.getContentType().ordinal());
		builder.setContent(req.getContent());
		builder.setSendedTime(req.getSendedTime());
		builder.setClientExt(req.getClientExt());
		builder.setServerExt(req.getServerExt());
		builder.setRoomType(req.getRoomType().getValue());

		builder.setRoomId(req.getRoomId());
		if (req.getAtUids() != null) {
			for (Long atUid : req.getAtUids()) {
				builder.addAtUids(String.valueOf(atUid));
			}
		}
		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	// 发送世界聊
	@Override
	public IMessage createSendWorldChatMessageUp(long userId, SendWorldChatReq req) {
		SendChatMessageUp.Builder builder = SendChatMessageUp.newBuilder();
		builder.setUid(String.valueOf(req.getUid()));
		builder.setContentType(req.getContentType().ordinal());
		builder.setContent(req.getContent());
		builder.setSendedTime(req.getSendedTime());
		builder.setClientExt(req.getClientExt());
		builder.setServerExt(req.getServerExt());
		builder.setRoomType(req.getRoomType().getValue());
		builder.setRoomId(req.getRoomId());
		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	// -------------------------------
	@Override
	public byte[] createSendChatMessageDownBytes(ChatRoom chatRoom, ChatMessage chatMessage,
			Map<String, String> translate, RoomType roomType) {
		SendChatMessageDown.Builder builder = createSendChatMessageDownBuilder(chatRoom, chatMessage, translate,
				roomType);
		return builder.build().toByteArray();
	}

	@Override
	public IMessage createSendChatMessageDown(long userId, ChatRoom chatRoom, ChatMessage chatMessage,
			Map<String, String> translate, RoomType roomType) {
		SendChatMessageDown.Builder builder = createSendChatMessageDownBuilder(chatRoom, chatMessage, translate,
				roomType);
		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	@Override
	public IMessage createGainChatMessageDown(long userId, long clientSendedTime,
			List<GainMessageResult> resultChatRoomInfos) {
		GainChatMessageDown.Builder builder = GainChatMessageDown.newBuilder();

		for (GainMessageResult resultChatRoomInfo : resultChatRoomInfos) {
			ChatRoomMessageInfo.Builder chatRoomMessageInfoBuilder = ChatRoomMessageInfo.newBuilder();
			if (resultChatRoomInfo.getChatRoom() != null) {
				chatRoomMessageInfoBuilder.setChatRoom(createChatRoomInfo(resultChatRoomInfo.getChatRoom()));
			}
			if (resultChatRoomInfo.getSelf() != null) {
				chatRoomMessageInfoBuilder.setSelf(createChatRoomMemberInfo(resultChatRoomInfo.getSelf()));
			}
			if (resultChatRoomInfo.getChatUsers() != null) {
				for (ChatUser chatUser : resultChatRoomInfo.getChatUsers()) {
					chatRoomMessageInfoBuilder.addUsers(createChatUserInfo(chatUser));
				}
			}
			if (resultChatRoomInfo.getChatMessages() != null) {
				for (ChatMessage chatMessage : resultChatRoomInfo.getChatMessages()) {
					// TODO 暂时没有翻译
					chatRoomMessageInfoBuilder.addChatMessage(createChatMessageInfo(chatMessage, null));
				}
				chatRoomMessageInfoBuilder.setLastOrder(resultChatRoomInfo.getLastOrder());
			}
			chatRoomMessageInfoBuilder.setRoomType(resultChatRoomInfo.getRoomType());
			builder.addChatRoomMessage(chatRoomMessageInfoBuilder);
		}
		builder.setSendedTime(clientSendedTime);
		return Message.createProtoMessage(builder.build(), userId);
	}

	@Override
	public IMessage createLastMessageOrderInChatDown(long userId, Map<String, String> data) {
		LastMessageOrderInChatDown.Builder builder = LastMessageOrderInChatDown.newBuilder();
		builder.setUserId(String.valueOf(userId));
		builder.putAllRoomToMsgOrder(data);
		return Message.createProtoMessage(builder.build(), userId);
	}

	@Override
	public IMessage createGainChatMessageDown(long userId) {
		GainChatMessageDown.Builder builder = GainChatMessageDown.newBuilder();
		return Message.createProtoMessage(builder.build(), userId);
	}

	@Override
	public IMessage createManagerChatRoomMemberDown(long userId, ModifyMemberResult result) {
		ManagerChatRoomMemberDown.Builder builder = createManagerChatRoomMemberDown(result.getChatRoom().getRoomId(),
				result.getModifyType(), result.getUids(), result.getChatRoom().getRoomType());
		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	@Override
	public byte[] createManagerChatRoomMemberDownBytes(ModifyMemberResult result) {
		ManagerChatRoomMemberDown.Builder builder = createManagerChatRoomMemberDown(result.getChatRoom().getRoomId(),
				result.getModifyType(), result.getUids(), result.getChatRoom().getRoomType());
		return builder.build().toByteArray();
	}

	@Override
	public IMessage createCreateChatRoomDown(long userId, CreateRoomResult result) {
		CreateChatRoomDown.Builder builder = createCreateChatRoomDown(result.getChatRoom().getRoomType(),
				result.getChatRoom().getRoomId(), result.getChatRoom().getAdmin(), result.getUids());
		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	@Override
	public byte[] createCreateChatRoomDownBytes(long userId, CreateRoomResult result) {
		CreateChatRoomDown.Builder builder = createCreateChatRoomDown(result.getChatRoom().getRoomType(),
				result.getChatRoom().getRoomId(), result.getChatRoom().getAdmin(), result.getUids());
		return builder.build().toByteArray();
	}

	@Override
	public IMessage createTranslationMessageUp(long userId, String roomId, int roomType, long order) {
		TranslationMessageUp.Builder builder = TranslationMessageUp.newBuilder();
		builder.setOrder(order);
		builder.setRoomId(roomId);
		builder.setRoomType(roomType);
		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	@Override
	public IMessage createTranslationMessageDown(long userId, ChatMessage chatMessage, Map<String, String> translate,
			int roomType, boolean isActive) {
		TranslationMessageDown.Builder builder = createTranslationMessageDownBuilder(chatMessage, translate, roomType,
				isActive);
		IMessage msg = Message.createProtoMessage(builder.build(), userId);
		return msg;
	}

	@Override
	public byte[] createTranslationMessageDownBytes(ChatMessage chatMessage, Map<String, String> translate,
			int roomType, boolean isActive) {
		TranslationMessageDown.Builder builder = createTranslationMessageDownBuilder(chatMessage, translate, roomType,
				isActive);
		return builder.build().toByteArray();
	}

	// -------------------------------
	private ErrorMessageDown.Builder createErrorMessageDown(int commandId, int errorDetail, int errorType) {
		ErrorMessageDown.Builder builder = ErrorMessageDown.newBuilder();
		builder.setCommandId(commandId);
		builder.setErrorDetail(errorDetail);
		builder.setErrorType(errorType);
		return builder;
	}

	private SuccessMessageDown.Builder createSuccessMessageDown(int commandId) {
		SuccessMessageDown.Builder builder = SuccessMessageDown.newBuilder();
		builder.setCommandId(commandId);
		return builder;
	}

	private TranslationMessageDown.Builder createTranslationMessageDownBuilder(ChatMessage chatMessage,
			Map<String, String> translate, int roomType, boolean isActive) {
		TranslationMessageDown.Builder builder = TranslationMessageDown.newBuilder();
		builder.setOrder(chatMessage.getOrderId());
		builder.setRoomId(chatMessage.getRoomId());
		if (translate != null) {
			builder.putAllTranslation(translate);
		}
		builder.setRoomType(roomType);
		builder.setIsActive(isActive);

		return builder;
	}

	private ChatUserInfo.Builder createChatUserInfo(ChatUser entity) {
		ChatUserInfo.Builder builder = ChatUserInfo.newBuilder();
		builder.setUid(entity.getUid());
		builder.setUserType(entity.getUserType());
		builder.setLanguageType(entity.getLanguageType());
		return builder;
	}

	// 聊天模块
	private ChatRoomMemberInfo.Builder createChatRoomMemberInfo(ChatRoomMember entity) {
		ChatRoomMemberInfo.Builder builder = ChatRoomMemberInfo.newBuilder();
		builder.setRoomId(entity.getRoomId());
		builder.setUid(entity.getUid());
		builder.setState(entity.getState());
		builder.setLastOrder(entity.getLastOrder());
		return builder;
	}

	private ChatRoomInfo.Builder createChatRoomInfo(ChatRoom entity) {
		ChatRoomInfo.Builder builder = ChatRoomInfo.newBuilder();
		builder.setAdmin(entity.getAdmin());
		builder.setCreateTime(entity.getCreateTime());
		builder.setName(entity.getName());
		builder.setRoomId(entity.getRoomId());
		builder.setRoomType(entity.getRoomType());
		return builder;
	}
	private Map<String, String> translateStr2Map(String translateResult) {
		Map<String, String> translate = null;
		if (translateResult != null) {
			translate = JsonUtil.gsonString2Obj(translateResult, Map.class);
			// 现在翻译返回的语言的iso是谷歌的，需要转成标准的
			List<LanguageType> changeLT = new LinkedList<>();
			for (Entry<String, String> entry : translate.entrySet()) {
				LanguageType languageType = LanguageType.getLanguageTypeByGoogle(entry.getKey());
				if (languageType != null && !entry.getKey().equals(languageType.getIsoCode())) {
					changeLT.add(languageType);
				}
			}
			for (LanguageType lt : changeLT) {
				// 替换翻译的key
				String v = translate.remove(lt.getGoogleCode());
				translate.put(lt.getIsoCode(), v);
			}
		}
		return translate;
	}
	
	private ChatMessageInfo.Builder createChatMessageInfo(ChatMessage entity, Map<String, String> translate) {
		ChatMessageInfo.Builder builder = ChatMessageInfo.newBuilder();
		builder.setUid(entity.getUid());
		builder.setRoomId(entity.getRoomId());
		builder.setContentType(entity.getContentType());
		builder.setContent(entity.getContent());
		builder.setOrder(entity.getOrderId());
		for (String atUid : entity.getAtUidsSet()) {
			builder.addAtUids(atUid);
		}
		builder.setSendedTime(entity.getSendedTime());
		builder.setReceivedTime(entity.getReceivedTime());
		builder.setClientExt(entity.getClientExt());
		builder.setServerExt(entity.getServerExt());
		if (translate != null) {
			builder.putAllTranslation(translate);
		}else if(entity.getTranslationStr() != null && !entity.getTranslationStr().equals("")) {
			builder.putAllTranslation(translateStr2Map(entity.getTranslationStr()));
		}
		return builder;
	}

	private ManagerChatRoomMemberDown.Builder createManagerChatRoomMemberDown(String roomId,
			MemberModifyType memberModifyType, List<String> members, int roomType) {
		ManagerChatRoomMemberDown.Builder builder = ManagerChatRoomMemberDown.newBuilder();
		builder.setModifyType(memberModifyType.ordinal());
		builder.setRoomId(roomId);
		if (members != null) {
			for (String member : members) {
				builder.addUid(member);
			}
		}
		builder.setRoomType(roomType);
		return builder;
	}

	private CreateChatRoomDown.Builder createCreateChatRoomDown(int roomType, String roomId, String admin,
			List<String> members) {
		CreateChatRoomDown.Builder builder = CreateChatRoomDown.newBuilder();
		builder.setRoomType(roomType);
		builder.setAdmin(admin);
		builder.setRoomId(roomId);
		if (members != null) {
			for (String member : members) {
				builder.addUid(member);
			}
		}
		return builder;
	}

	private SendChatMessageDown.Builder createSendChatMessageDownBuilder(ChatRoom chatRoom, ChatMessage chatMessage,
			Map<String, String> translate, RoomType roomType) {
		SendChatMessageDown.Builder builder = SendChatMessageDown.newBuilder();
		if (chatRoom != null) {
			builder.setChatRoom(createChatRoomInfo(chatRoom));
		}
		if (chatMessage != null) {
			builder.setChatMessage(createChatMessageInfo(chatMessage, translate));
		}
		builder.setRoomType(roomType.getValue());
		return builder;
	}
}
