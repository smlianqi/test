package com.elex.common.net.session;

import java.util.List;

import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.IPlayerFactory;
import com.elex.common.net.message.IMessageSender;

/**
 * session管理类
 * 
 * @author mausmars
 * 
 */
public interface ISessionManager extends IMessageSender {
	/**
	 * 新建session
	 * 
	 * @param session
	 */
	void insertSession(ISession session);

	/**
	 * 移除session
	 * 
	 * @param sessionId
	 */
	void removeSession(String sessionId);

	/**
	 * 移除所有连接
	 * 
	 * @return
	 */
	List<ISession> removeAllSession();

	ISession getSessionByUserId(long userId);

	IPlayer getPlayer(long userId);

	List<IPlayer> getPlayers();

	// ----------------------------------------------
	IPlayerFactory getPlayerFactory();
}
