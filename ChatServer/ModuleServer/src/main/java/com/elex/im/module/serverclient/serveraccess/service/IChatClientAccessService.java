package com.elex.im.module.serverclient.serveraccess.service;

import java.util.List;

import com.elex.common.component.player.type.UserType;
import com.elex.im.module.serverchat.module.chat.type.MemberModifyType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;
import com.elex.im.module.serverclient.request.SendMultiChatReq;
import com.elex.im.module.serverclient.request.SendSingleChatReq;
import com.elex.im.module.serverclient.request.SendWorldChatReq;
import com.elex.im.module.translation.type.LanguageType;

/**
 * 聊天接入集群的客户端服务接口
 * 
 * @author mausmars
 *
 */
public interface IChatClientAccessService {
	/**
	 * 开始服务
	 */
	void startup();

	/**
	 * 停止服务
	 */
	void shutdown();

	// ---------------------------------
	/**
	 * 发送登录
	 * 
	 * @param userId
	 *            用户id
	 */
	void sendUserLogin(long userId, UserType userType, LanguageType languageType);

	/**
	 * 发送登出
	 * 
	 * @param userId
	 *            用户id
	 */
	void sendUserLogout(long userId);

	/**
	 * 发送修改语言
	 * 
	 * @param userId
	 *            用户id
	 */
	void sendModifyLanguage(long userId, Object message) throws Exception;

	void sendModifyLanguage(long userId, LanguageType languageType);

	// ---------------------------------
	void sendChatMessage(long userId, Object message) throws Exception;

	/**
	 * 发送全服信息
	 * 
	 * @param userId
	 *            用户id
	 * @param content
	 * @param roomId
	 */

	void sendWorldChatMessage(long userId, SendWorldChatReq req);

	/**
	 * 发送单聊信息
	 * 
	 * @param userId
	 * @param content
	 * @param targetUid
	 */
	void sendSingleChatMessage(long userId, SendSingleChatReq req);

	/**
	 * 发送群聊信息
	 * 
	 * @param userId
	 */
	void sendMultiChatMessage(long userId, SendMultiChatReq req);

	// ---------------------------------
	/**
	 * 发送获取聊天信息
	 * 
	 * @param userId
	 */
	void sendGainChatMessage(long userId, Object message) throws Exception;

	void sendLastMessageOrderInChat(long userId, Object message) throws Exception;

	void sendGainAllNewestChatMessage(long userId, long clienSendedTime, String regionRoomId, String unionRoomId);

	void sendGainRoomMultiChatMessage(long userId, long clienSendedTime, String roomId, RoomType roomType,
			List<Long> orders);

	void sendGainRoomPageChatMessage(long userId, long clienSendedTime, String roomId, RoomType roomType, long order,
			int count);

	void sendGainRoomNewestChatMessage(long userId, long clienSendedTime, String roomId, RoomType roomType);

	// ---------------------------------
	void sendCreateChatRoom(long userId, Object message) throws Exception;

	/**
	 * 发送创建房间
	 * 
	 * @param userId
	 *            用户id
	 * @param roomId
	 *            房间id(全服固定id，用户组合id，工会id)
	 * @param admin
	 *            管理员
	 * @param members
	 *            成员
	 */
	void sendCreateChatRoom(long userId, RoomType roomType, String roomId, String admin, List<Long> members);

	/**
	 * 发送成员管理
	 * 
	 * @param userId
	 *            用户id
	 * @param rid
	 *            房间唯一id
	 * @param memberModifyType
	 *            0添加 1移除
	 * @param members
	 *            成员
	 */
	void sendManagerChatRoomMember(long userId, Object message) throws Exception;

	void sendManagerChatRoomMember(long userId, String roomId, MemberModifyType memberModifyType, List<Long> members);

	/**
	 * 发送翻译请求
	 * 
	 * @param userId
	 * @param roomId
	 * @param order
	 */
	void sendTranslationMessage(long userId, Object message) throws Exception;

	void sendTranslationMessage(long userId, String roomId, int roomType, long order);
}
