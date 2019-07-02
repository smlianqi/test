package com.elex.common.component.net.handlerconfig;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.elex.common.component.player.IPlayerFactory;
import com.elex.common.net.handler.IClosedSessionHandler;
import com.elex.common.net.handler.ICreateSessionHandler;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.rounter.IMessageRounter;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.service.IGlobalContext;

public abstract class ANetHandlerConfig implements INetHandlerConfig {
	// 消息映射
	protected Map<MegProtocolType, List<MessageConfig>> messageConfigs = new HashMap<>();

	protected IMessageRounter messageRounter;

	protected IPlayerFactory playerFactory;

	protected Object listener;

	// 建立连接处理类
	protected List<ICreateSessionHandler> createSessionHandlers = new LinkedList<>();
	// 断开连接处理类
	protected List<IClosedSessionHandler> closeSessionHandlers = new LinkedList<>();

	public ANetHandlerConfig() {
		for (MegProtocolType mpt : MegProtocolType.values()) {
			messageConfigs.put(mpt, new LinkedList<MessageConfig>());
		}
	}

	@Override
	public Object getListener() {
		return listener;
	}

	@Override
	public List<MessageConfig> getMessageConfigs(MegProtocolType megProtocolType) {
		return messageConfigs.get(megProtocolType);
	}

	@Override
	public IMessageRounter getMessageRounter() {
		return messageRounter;
	}

	@Override
	public IPlayerFactory getPlayerFactory() {
		return playerFactory;
	}

	@Override
	public List<ICreateSessionHandler> getCreateSessionHandlers() {
		return createSessionHandlers;
	}

	@Override
	public List<IClosedSessionHandler> getCloseSessionHandlers() {
		return closeSessionHandlers;
	}

	/**
	 * 创建消息配置
	 */
	public abstract void createMessageConfigs(IGlobalContext c);

	public void addCloseSessionHandlers(IClosedSessionHandler closeSessionHandler) {
		this.closeSessionHandlers.add(closeSessionHandler);
	}

	public void addCreateSessionHandler(ICreateSessionHandler createSessionHandler) {
		this.createSessionHandlers.add(createSessionHandler);
	}

	public void setMessageRounter(IMessageRounter messageRounter) {
		this.messageRounter = messageRounter;
	}

	public void setPlayerFactory(IPlayerFactory playerFactory) {
		this.playerFactory = playerFactory;
	}

	public void setListener(Object listener) {
		this.listener = listener;
	}
}
