package com.elex.common.net.event;

import com.elex.common.net.session.ISession;

public class ReconnectEvent {
	private ISession session;

	public ISession getSession() {
		return session;
	}

	public void setSession(ISession session) {
		this.session = session;
	}
}
