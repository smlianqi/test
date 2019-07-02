package com.elex.common.net.session;

public interface ISessionFactory {
	ISession createSession(Object channel);

	ISessionManager getSessionManager();
}
