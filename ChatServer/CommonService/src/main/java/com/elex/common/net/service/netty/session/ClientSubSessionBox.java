package com.elex.common.net.service.netty.session;

import com.elex.common.net.type.SessionType;

public class ClientSubSessionBox implements ISubSessionBox {
	private Long userId;

	@Override
	public SessionType getSessionType() {
		return SessionType.Client;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
