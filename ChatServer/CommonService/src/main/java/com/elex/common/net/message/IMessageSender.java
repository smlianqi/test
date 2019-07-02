package com.elex.common.net.message;

import java.util.List;

/**
 * 消息发送接口
 * 
 * @author mausmars
 * 
 */
public interface IMessageSender {
	/**
	 * 发送消息到所有人
	 * 
	 * @param sessionId
	 * @param message
	 */
	void sendBroadcast2Players(Object message);

	// -------------------------------------
	/**
	 * 发送消息到sessionId
	 * 
	 * @param sessionId
	 * @param message
	 */
	void sendMessageBySessionId(Object message, String sessionId);

	/**
	 * 发送给指定人
	 * 
	 * @param sessionIds
	 * @param message
	 */
	void sendMessageBySessionIds(Object message, List<String> sessionIds);

	// -------------------------------------
	/**
	 * 发送消息到playerId
	 * 
	 * @param playerId
	 * @param message
	 */
	void sendMessageByPlayerId(Object message, Long playerId);

	/**
	 * 发送给指定人
	 * 
	 * @param playerIds
	 * @param message
	 */
	void sendMessageByPlayerIds(Object message, List<Long> playerIds);
	
}
