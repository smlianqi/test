package com.elex.im.data.chatmessage;

import java.util.List;

import com.elex.common.component.data.IDao;

public interface IChatMessageDao extends IDao<ChatMessage> {
	ChatMessage selectByRoomIdAndOrder(String roomId, long order);

	List<ChatMessage> selectByRoomIdAndCount(String roomId, int newMaxCount);

	List<ChatMessage> selectByRoomIdAndOrders(String roomId, List<Long> order);

	List<ChatMessage> selectByRoomIdAndOrderPage(String roomId, long order, int pageCount);

	long getMaxOrder(String roomId);

	long getNextOrder(String roomId);
}
