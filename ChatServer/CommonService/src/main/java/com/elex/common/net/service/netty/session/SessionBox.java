package com.elex.common.net.service.netty.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.IPlayerFactory;
import com.elex.common.message.IMessageCreater;
import com.elex.common.message.IModuleMessageCreater;
import com.elex.common.net.handler.IMessageOutHandler;
import com.elex.common.net.session.ISession;
import com.elex.common.net.session.ISessionManager;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.net.type.SessionType;
import com.elex.common.service.IService;

/**
 * 工具箱
 * 
 * @author mausmars
 *
 */
public class SessionBox {
	private MegProtocolType megProtocolType;// 协议类型
	private ISessionManager sessionManager;

	private IMessageOutHandler<Object> messageOutHandler;

	private ISubSessionBox subSessionBox;

	// session映射多个player
	private ConcurrentMap<Long, IPlayer> playerMap = new ConcurrentHashMap<>();

	private Map<MegProtocolType, IMessageCreater> messageCreaterMap;
	private Map<MegProtocolType, IModuleMessageCreater> moduleMessageCreaterMap;

	// 归属的服务
	private IService service;

	public SessionBox() {
	}

	public void replaceSession(IPlayer player, ISession newSession) {
		ISession oldSession = player.getSession();

		if (oldSession != newSession) {
			SessionBox oldSessionBox = oldSession.getAttach(SessionAttachType.SessionBox);
			if (oldSessionBox.getSubSessionBox() != null) {
				// 添加映射关系
				switch (oldSessionBox.getSessionType()) {
				case Client:
					player.replaceSessionAndCloseOld(newSession);
					break;
				case Forward:
					SessionBox sessionBox = oldSession.getAttach(SessionAttachType.SessionBox);
					sessionBox.playerMap.remove(player.getUserId());
					// player用到session pool的session
					player.replaceSession(newSession);
					break;
				default:
					break;
				}
			} else {
				// 如果为空，是client session
				bindingPlayer(player);
				player.replaceSession(newSession);
			}
		}
	}

	public void bindingPlayer(IPlayer player) {
		if (subSessionBox == null) {
			subSessionBox = new ClientSubSessionBox();
			((ClientSubSessionBox) subSessionBox).setUserId(player.getUserId());
		}
		// 设置映射player
		playerMap.put(player.getUserId(), player);
	}

	public <T extends IModuleMessageCreater> T getModuleMessageCreater() {
		return (T) moduleMessageCreaterMap.get(megProtocolType);
	}

	public <T extends IModuleMessageCreater> T getModuleMessageCreater(MegProtocolType mpt) {
		return (T) moduleMessageCreaterMap.get(mpt);
	}

	public IMessageCreater getMessageCreater() {
		return messageCreaterMap.get(megProtocolType);
	}

	public IMessageCreater getMessageCreater(MegProtocolType mpt) {
		return messageCreaterMap.get(mpt);
	}

	public String getHashKey() {
		if (subSessionBox == null) {
			return null;
		}
		String key = null;
		switch (subSessionBox.getSessionType()) {
		case Forward: {
			ForwardSubSessionBox box = (ForwardSubSessionBox) subSessionBox;
			key = String.valueOf(box.getIndex());
			break;
		}
		case Client: {
			ClientSubSessionBox box = (ClientSubSessionBox) subSessionBox;
			key = String.valueOf(box.getUserId());
			break;
		}
		default:
			break;
		}
		return key;
	}

	public Long getUserId() {
		if (subSessionBox == null || subSessionBox.getSessionType() != SessionType.Client) {
			return null;
		}
		ClientSubSessionBox box = (ClientSubSessionBox) subSessionBox;
		return box.getUserId();
	}

	public Integer getSessionIndex() {
		if (subSessionBox == null || subSessionBox.getSessionType() != SessionType.Forward) {
			return null;
		}
		ForwardSubSessionBox box = (ForwardSubSessionBox) subSessionBox;
		return box.getIndex();
	}

	public <T extends ISubSessionBox> T getSubSessionBox() {
		return (T) subSessionBox;
	}

	public SessionType getSessionType() {
		return subSessionBox.getSessionType();
	}

	public void setSubSessionBox(ISubSessionBox subSessionBox) {
		this.subSessionBox = subSessionBox;
	}

	public void outhandle(Object message, ISession session) {
		messageOutHandler.outhandle(message, session);
	}

	public IPlayerFactory getPlayerFactory() {
		return sessionManager.getPlayerFactory();
	}

	public MegProtocolType getMegProtocolType() {
		return megProtocolType;
	}

	public void setMegProtocolType(MegProtocolType megProtocolType) {
		this.megProtocolType = megProtocolType;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public void setMessageOutHandler(IMessageOutHandler<Object> messageOutHandler) {
		this.messageOutHandler = messageOutHandler;
	}

	public IMessageOutHandler<Object> getMessageOutHandler() {
		return messageOutHandler;
	}

	public void setMessageCreaterMap(Map<MegProtocolType, IMessageCreater> messageCreaterMap) {
		this.messageCreaterMap = messageCreaterMap;
	}

	public ConcurrentMap<Long, IPlayer> getPlayerMap() {
		return playerMap;
	}

	public void setPlayerMap(ConcurrentMap<Long, IPlayer> playerMap) {
		this.playerMap = playerMap;
	}

	public void setModuleMessageCreaterMap(Map<MegProtocolType, IModuleMessageCreater> moduleMessageCreaterMap) {
		this.moduleMessageCreaterMap = moduleMessageCreaterMap;
	}

	public IService getService() {
		return service;
	}

	public void setService(IService service) {
		this.service = service;
	}
}
