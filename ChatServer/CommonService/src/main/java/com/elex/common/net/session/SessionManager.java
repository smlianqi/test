package com.elex.common.net.session;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.IPlayerFactory;
import com.elex.common.net.IConnectCallBack;
import com.elex.common.net.message.IMessageConfigMgr;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 发送客户端
 * 
 * @author mausmars
 * 
 */

public class SessionManager implements ISessionManager {
	protected static final ILogger logger = XLogUtil.logger();

	// sessionID:session
	private ConcurrentMap<String, ISession> sessionMap = new ConcurrentHashMap<>();

	private IPlayerFactory playerFactory;

	protected IConnectCallBack callBack;

	protected IMessageConfigMgr messageConfigMgr;

	@Override
	public IPlayerFactory getPlayerFactory() {
		return playerFactory;
	}

	@Override
	public ISession getSessionByUserId(long userId) {
		IPlayer player = playerFactory.getPlayerMgrService().select(userId);
		return player == null ? null : player.getSession();
	}

	@Override
	public IPlayer getPlayer(long userId) {
		return playerFactory.getPlayerMgrService().select(userId);
	}

	@Override
	public void sendBroadcast2Players(Object message) {
		// 发送给所有在线用户
		List<IPlayer> players = playerFactory.getPlayerMgrService().getAllPlayer();
		for (IPlayer player : players) {
			player.send(message);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("sendBroadcastMessage message=" + message.getClass());
		}
	}

	@Override
	public void sendMessageByPlayerId(Object message, Long playerId) {
		IPlayer player = playerFactory.getPlayerMgrService().select(playerId);
		if (player != null) {
			player.send(message);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("sendMessageByPlayerId to playerId=" + playerId + "  message=" + message.getClass());
		}
	}

	@Override
	public void sendMessageByPlayerIds(Object message, List<Long> playerIds) {
		for (Long playerId : playerIds) {
			IPlayer player = playerFactory.getPlayerMgrService().select(playerId);
			if (player == null) {
				continue;
			}
			player.send(message);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("sendMessageByPlayerIds  message=" + message.getClass());
		}
	}

	// -----------------------------------
	@Override
	public void sendMessageBySessionId(Object message, String sessionId) {
		ISession session = sessionMap.get(sessionId);
		if (session != null) {
			session.send(message);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("sendMessageBySessionId to sessionId=" + sessionId + "  message=" + message.getClass());
		}
	}

	@Override
	public void sendMessageBySessionIds(Object message, List<String> sessionIds) {
		for (String sessionId : sessionIds) {
			ISession session = sessionMap.get(sessionId);
			if (session == null) {
				continue;
			}
			session.send(message);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("sendMessageBySessionIds  message=" + message.getClass());
		}
	}

	@Override
	public void insertSession(ISession session) {
		sessionMap.put(session.getSessionId(), session);
		// 回调处理
		if (callBack != null) {
			callBack.connectExcute(session);
		}

	}

	@Override
	public void removeSession(String sessionId) {
		ISession session = sessionMap.remove(sessionId);
		if (session == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("remove session session is null sessionId= " + sessionId);
			}
			return;
		}
	}

	private Collection<ISession> getAllSession() {
		return sessionMap.values();
	}

	@Override
	public List<ISession> removeAllSession() {
		List<ISession> targets = new LinkedList<>();
		Collection<ISession> sessions = getAllSession();
		for (ISession session : sessions) {
			// 关闭session
			session.close();
			targets.add(session);
		}
		return targets;
	}

	@Override
	public List<IPlayer> getPlayers() {
		return playerFactory.getPlayerMgrService().getAllPlayer();
	}

	// ------------------------------------------------------------------------------
	public void setCallBack(IConnectCallBack callBack) {
		this.callBack = callBack;
	}

	public void setMessageConfigMgr(IMessageConfigMgr messageConfigMgr) {
		this.messageConfigMgr = messageConfigMgr;
	}

	public void setPlayerFactory(IPlayerFactory playerFactory) {
		this.playerFactory = playerFactory;
	}
}
