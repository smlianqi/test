package com.elex.common.net.session;

import java.util.Arrays;

import com.elex.common.component.net.client.INetClientService;
import com.elex.common.net.service.netty.session.ForwardSubSessionBox;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.type.ConnectType;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.service.IService;
import com.elex.common.util.hash.DefaultHashFunc;
import com.elex.common.util.hash.IHashFunc;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.common.util.random.RandomUtil;

/**
 * session池
 * 
 * @author Administrator
 *
 */
public class SessionPool implements ISessionPool {
	protected static final ILogger logger = XLogUtil.logger();

	private volatile ISession[] sessions;

	// 默认hash函数
	private IHashFunc hashFunc;

	public SessionPool(int count) {
		this.sessions = new ISession[count];
		this.hashFunc = DefaultHashFunc.defaultHashFunc;
	}

	public SessionPool(int count, IHashFunc hashFunc) {
		this.sessions = new ISession[count];
		this.hashFunc = hashFunc;
	}

	@Override
	public void release() {
		for (ISession session : sessions) {
			SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);

			IService service = sessionBox.getService();
			if (service != null) {
				if (service instanceof INetClientService) {
					INetClientService clientService = (INetClientService) service;
					clientService.removeNetClient(session);
				}
			}
		}
	}

	@Override
	public void bindingServer(int index, ISession session) {
		ForwardSubSessionBox forwardSessionBox = new ForwardSubSessionBox();
		forwardSessionBox.setIndex(index);
		forwardSessionBox.setClient(false);
		forwardSessionBox.setSessionPool(this);
		
		SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
		sessionBox.setSubSessionBox(forwardSessionBox);

		ISession older = null;
		synchronized (this) {
			older = sessions[index];
			sessions[index] = session;
		}
		if (older != null) {
			older.close();
		}
	}

	@Override
	public void bindingClient(int index, String sid, String token, ISession session) {
		ForwardSubSessionBox forwardSessionBox = new ForwardSubSessionBox();
		forwardSessionBox.setClient(true);
		forwardSessionBox.setIndex(index);
		forwardSessionBox.setToken(token);
		forwardSessionBox.setSid(sid);
		forwardSessionBox.setSessionPool(this);

		SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
		sessionBox.setSubSessionBox(forwardSessionBox);

		ISession older = null;
		synchronized (this) {
			older = sessions[index];
			sessions[index] = session;
		}
		if (older != null) {
			older.close();
		}
	}

	@Override
	public void close() {
		for (int i = 0; i < sessions.length; i++) {
			ISession session = sessions[i];
			synchronized (this) {
				sessions[i] = null;
			}
			session.close();
		}
	}

	@Override
	public ISession getSession(int index) {
		return sessions[index];
	}

	@Override
	public ISession getRandomSession() {
		int i = RandomUtil.generalRrandom(size());
		ISession session = sessions[i];
		if (session != null) {
			return session;
		}
		// 获取可用的session
		session = getAvailableSession();
		if (session != null) {
			return session;
		}
		if (logger.isErrorEnabled()) {
			logger.error("All session is unavailable!!!");
		}
		return session;
	}

	/**
	 * 指定散列发送
	 * 
	 * @param message
	 */
	public void send(String key, Object msg) {
		int i = getIndex(key);
		ISession session = sessions[i];
		if (session != null) {
			session.send(msg);
		} else {
			send(msg);
		}
	}

	@Override
	public int getIndex(String key) {
		long h = hashFunc.hash(key);
		h = Math.abs(h);
		int i = (int) (h % size());
		return i;
	}

	@Override
	public void send(int index, Object msg) {
		ISession session = sessions[index];
		if (session != null) {
			session.send(msg);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("Index session is unavailable!!! index=" + index);
			}
		}
	}

	/**
	 * 随机发送
	 * 
	 * @param msg
	 */
	@Override
	public void sendRandom(Object msg) {
		int i = RandomUtil.generalRrandom(size());
		ISession session = sessions[i];
		if (session != null) {
			session.send(msg);
			return;
		}
		session = getAvailableSession();
		if (session != null) {
			session.send(msg);
			return;
		}
		if (logger.isErrorEnabled()) {
			logger.error("All session is unavailable!");
		}
	}

	/**
	 * 指定发送固定
	 * 
	 * @param msg
	 */
	@Override
	public void send(Object msg) {
		ISession session = sessions[0];
		if (session != null) {
			if (logger.isWarnEnabled()) {
				logger.warn("Fixed session send! i=" + 0);
			}
			session.send(msg);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("Fixed session is unavailable! i=" + 0);
			}
		}
	}

	/**
	 * 获取可用的session
	 * 
	 * @return
	 */
	private ISession getAvailableSession() {
		ISession session = null;
		for (ISession s : sessions) {
			if (s == null) {
				// TODO 判断session的其他可用条件
				continue;
			}
			session = s;
			break;
		}
		return session;
	}

	public static void main(String[] args) {
	}

	@Override
	public int size() {
		return sessions.length;
	}

	@Override
	public ConnectType getConnectType() {
		return ConnectType.SessionPool;
	}

	@Override
	public String toString() {
		return "SessionPool [sessions=" + Arrays.toString(sessions) + "]";
	}
}
