package com.elex.im.module.serverchat.module.chat.room;

import com.elex.im.data.chatroom.ChatRoom;
import com.elex.im.data.chatroommember.ChatRoomMember;
import com.elex.im.module.serverchat.module.chat.room.request.CreateChatRoomReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.GainChatMessageReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.ManagerChatRoomMemberReqContext;
import com.elex.im.module.serverchat.module.chat.room.request.SendMessageReqContext;
import com.elex.im.module.serverchat.module.chat.room.result.GainMessageResult;
import com.elex.im.module.serverchat.module.chat.room.result.RoomResult;

/**
 * 聊天室接口
 * 
 * @author mausmars
 *
 */
public interface IChatRoomHandler {
	/**
	 * 创建房间
	 */
	RoomResult createRoom(CreateChatRoomReqContext context);

	/**
	 * 修改房间成员
	 */
	RoomResult modifyMember(ManagerChatRoomMemberReqContext context, ChatRoom chatRoom);

	/**
	 * 获取聊天信息
	 * 
	 * @param uid
	 * @return
	 */
	GainMessageResult gainChatMessage(GainChatMessageReqContext context, ChatRoom chatRoom, ChatRoomMember self);

	/**
	 * 发送消息
	 */
	RoomResult sendMessage(SendMessageReqContext context);
}
