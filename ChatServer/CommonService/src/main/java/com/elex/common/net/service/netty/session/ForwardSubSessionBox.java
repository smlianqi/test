package com.elex.common.net.service.netty.session;

import com.elex.common.net.session.ISessionPool;
import com.elex.common.net.type.SessionType;

/**
 * 转发session信息
 * 
 * @author mausmars
 *
 */
public class ForwardSubSessionBox implements ISubSessionBox {
	private String sid;
	private String token;
	private int index;

	private boolean isClient;
	private ISessionPool sessionPool;

	public ForwardSubSessionBox() {
	}

	@Override
	public SessionType getSessionType() {
		return SessionType.Forward;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isClient() {
		return isClient;
	}

	public void setClient(boolean isClient) {
		this.isClient = isClient;
	}

	public ISessionPool getSessionPool() {
		return sessionPool;
	}

	public void setSessionPool(ISessionPool sessionPool) {
		this.sessionPool = sessionPool;
	}
}
