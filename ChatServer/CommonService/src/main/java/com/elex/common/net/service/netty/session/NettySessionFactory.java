package com.elex.common.net.service.netty.session;

import com.elex.common.component.net.config.NetCustomConfig;
import com.elex.common.net.handler.IMessageHandler;
import com.elex.common.net.session.ISession;
import com.elex.common.net.session.ISessionFactory;
import com.elex.common.net.session.ISessionManager;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.service.IService;

import io.netty.channel.Channel;

public class NettySessionFactory implements ISessionFactory {
	private ISessionManager sessionManager;

	private IMessageHandler<Object, Object> messageHandler;

	private NetCustomConfig netCustomConfig;

	private IService service;

	public NettySessionFactory() {
	}

	public ISession createSession(Object channel) {
		Channel c = (Channel) channel;
		NettySession session = new NettySession(c);

		// 工具箱
		SessionBox sessionBox = new SessionBox();
		// TODO 暂时都是proto协议
		sessionBox.setMegProtocolType(MegProtocolType.proto);
		sessionBox.setSessionManager(sessionManager);
		sessionBox.setMessageOutHandler(messageHandler);
		sessionBox.setMessageCreaterMap(netCustomConfig.getMessageCreaterMap());
		sessionBox.setModuleMessageCreaterMap(netCustomConfig.getModuleMessageCreaterMap());
		sessionBox.setService(service);

		session.setAttach(SessionAttachType.SessionBox, sessionBox);

		return session;
	}

	@Override
	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	// ---------------------------------------------------
	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public void setMessageHandler(IMessageHandler<Object, Object> messageHandler) {
		this.messageHandler = messageHandler;
	}

	public void setNetCustomConfig(NetCustomConfig netCustomConfig) {
		this.netCustomConfig = netCustomConfig;
	}

	public void setService(IService service) {
		this.service = service;
	}
}
