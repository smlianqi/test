package com.elex.common.component.net.server;

import com.elex.common.net.session.ISessionManager;

public interface INettyNetServerService extends INetServerService {
	/**
	 * 获取session管理器
	 * 
	 * @return
	 */
	ISessionManager getSessionManager();
}
