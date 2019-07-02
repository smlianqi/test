package com.elex.im.message;

import com.elex.common.message.IModuleMessageCreater;
import com.elex.common.net.message.IMessage;
import com.elex.im.data.chatmessage.ChatMessage;
import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.module.serverchat.module.chat.room.result.CreateRoomResult;
import com.elex.im.module.serverchat.module.chat.room.result.GainMessageResult;
import com.elex.im.module.serverchat.module.chat.room.result.ModifyMemberResult;
import com.elex.im.module.serverchat.module.chat.type.MemberModifyType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;
import com.elex.im.module.serverclient.request.SendMultiChatReq;
import com.elex.im.module.serverclient.request.SendSingleChatReq;
import com.elex.im.module.serverclient.request.SendWorldChatReq;

import java.util.List;
import java.util.Map;

public interface AModuleMessageCreater extends IModuleMessageCreater {
	IMessage createUserLoginUp(long userId, int userType, String languageType);

	IMessage createUserLogoutUp(long userId);

	IMessage createModifyLanguageUp(long userId, String languageType);

	IMessage createUserLoginDown(long userId, int userType, String functionType, String sid);

	// =============================================
	IMessage createErrorMessageDown(Long userId, int commandId, int errorDetail, int errorType);

	IMessage createSuccessMessageDown(Long userId, int commandId);

	// =============================================
	IMessage createGainAllNewestChatMessageUp(long userId, long clienSendedTime, String regionRoomId,
			String unionRoomId);

	IMessage createGainRoomMultiChatMessageUp(long userId, long clienSendedTime, String roomId, RoomType roomType,
			List<Long> orders);

	IMessage createGainRoomPageChatMessageUp(long userId, long clienSendedTime, String roomId, RoomType roomType,
			long order, int count);

	IMessage createGainRoomNewestChatMessageUp(long userId, long clienSendedTime, String roomId, RoomType roomType);

	// ----------------------------------------------
	IMessage createSendSingleChatMessageUp(long userId, SendSingleChatReq req);

	IMessage createSendMultiChatMessageUp(long userId, SendMultiChatReq req);

	IMessage createSendWorldChatMessageUp(long userId, SendWorldChatReq req);

	// ----------------------------------------------
	IMessage createManagerChatRoomMemberUp(long userId, String roomId, MemberModifyType memberModifyType,
			List<Long> members);

	IMessage createCreateChatRoomUp(long userId, int roomType, String roomId, String admin, List<Long> members);

	IMessage createTranslationMessageUp(long userId, String roomId, int roomType, long order);

	// ===============================
	byte[] createSendChatMessageDownBytes(ChatRoom chatRoom, ChatMessage chatMessage, Map<String, String> translate,
			RoomType roomType);

	IMessage createSendChatMessageDown(long userId, ChatRoom chatRoom, ChatMessage chatMessage,
			Map<String, String> translate, RoomType roomType);

	IMessage createGainChatMessageDown(long userId, long clientSendedTime, List<GainMessageResult> resultChatRoomInfos);

	IMessage createLastMessageOrderInChatDown(long userId, Map<String,String> data);

	IMessage createGainChatMessageDown(long userId);

	IMessage createManagerChatRoomMemberDown(long userId, ModifyMemberResult result);

	byte[] createManagerChatRoomMemberDownBytes(ModifyMemberResult result);

	IMessage createCreateChatRoomDown(long userId, CreateRoomResult result);

	byte[] createCreateChatRoomDownBytes(long userId, CreateRoomResult result);

	byte[] createTranslationMessageDownBytes(ChatMessage chatMessage, Map<String, String> translate, int roomType,
			boolean isActive);

	IMessage createTranslationMessageDown(long userId, ChatMessage chatMessage, Map<String, String> translate,
			int roomType, boolean isActive);
}
