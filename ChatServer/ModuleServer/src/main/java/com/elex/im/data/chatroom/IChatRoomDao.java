package com.elex.im.data.chatroom;

import com.elex.common.component.data.IDao;

public interface IChatRoomDao extends IDao<ChatRoom> {
	ChatRoom selectByRoomId(String roomId);
}
