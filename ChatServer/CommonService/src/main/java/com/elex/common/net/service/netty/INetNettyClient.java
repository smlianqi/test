package com.elex.common.net.service.netty;

import com.elex.common.net.INetClient;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;

public interface INetNettyClient extends INetClient {
	/**
	 * 发送
	 * 
	 * @param message
	 */
	void send(Object message);

	void setSessionAttachment(SessionAttachType key, Object attachment);

	ISession getSession();
}
