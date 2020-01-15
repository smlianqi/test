package com.elex.common.component.player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.elex.common.component.player.service.IPlayerMService;
import com.elex.common.component.player.type.PlayerAttachType;
import com.elex.common.component.player.type.PlayerMServiceType;
import com.elex.common.component.player.type.UserType;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 角色封装类
 * 
 * @author mausmars
 * 
 */
public class DefaultPlayer implements IPlayer {
	protected static final ILogger logger = XLogUtil.logger();

	protected long userId; // 用户id
	protected UserType userType; // 用户类型
	// private String rid; // 区id
	protected volatile ISession session; // 连接session

	protected IGlobalContext context; // 上下文

	/**
	 * player服务
	 */
	protected ConcurrentMap<PlayerMServiceType, IPlayerMService> serviceMap = new ConcurrentHashMap<>();

	protected ConcurrentMap<Object, Object> attach = new ConcurrentHashMap<>();

	public DefaultPlayer(long userId, UserType userType) {
		this.userId = userId;
		this.userType = userType;
	}

	@Override
	public UserType getUserType() {
		return userType;
	}

	@Override
	public <T extends IPlayerMService> T getPlayerMService(PlayerMServiceType type) {
		return (T) serviceMap.get(type);
	}

	public void insert(IPlayerMService service) {
		serviceMap.put(service.getType(), service);
	}

	@Override
	public IGlobalContext getContext() {
		return context;
	}

	@Override
	public ISession getSession() {
		return session;
	}

	@Override
	public Long getUserId() {
		return userId;
	}

	@Override
	public void replaceSessionAndCloseOld(ISession session) {
		if (this.session != session) {
			// 不是同一个session就关闭老的
			if (this.session != null) {
				this.session.close();
			}
			this.session = session;
		}
	}

	@Override
	public void replaceSession(ISession session) {
		this.session = session;
	}

	@Override
	public void clearSession() {
		this.session = null;
	}

	@Override
	public void send(Object msg) {
		if (session == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(">>> Player send session=null!!!");
			}
			return;
		}
		// if (!session.isOpen()) {
		// if (logger.isDebugEnabled()) {
		// logger.debug(">>> Player send session is closed!!!");
		// }
		// return;
		// }

		if ((msg instanceof IMessage)) {
			IMessage m = (IMessage) msg;
			m.setUserId(userId);
			// if (logger.isDebugEnabled()) {
			// logger.debug("send msg=" + msg);
			// }
			session.send(m);
		} else {
			session.send(msg);
		}
	}

	@Override
	public <T> T getAttach(PlayerAttachType key) {
		return (T) attach.get(key);
	}

	@Override
	public void setAttach(PlayerAttachType key, Object value) {
		attach.put(key, value);
	}

	@Override
	public void setAttachToSession(SessionAttachType key, Object attachment) {
		if (session != null) {
			session.setAttach(key, attachment);
		}
	}

	@Override
	public <T> T getAttachFromSession(SessionAttachType key) {
		if (session != null) {
			return session.getAttach(key);
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultPlayer other = (DefaultPlayer) obj;
		if (userId != other.userId)
			return false;
		return true;
	}

	// --------------------------------------------
	public void setSession(ISession session) {
		this.session = session;
	}

	public void setContext(IGlobalContext context) {
		this.context = context;
	}
}
