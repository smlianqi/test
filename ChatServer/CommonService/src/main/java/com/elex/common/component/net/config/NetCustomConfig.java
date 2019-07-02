package com.elex.common.component.net.config;

import java.util.HashMap;
import java.util.Map;

import com.elex.common.component.net.handlerconfig.INetHandlerConfig;
import com.elex.common.message.IMessageCreater;
import com.elex.common.message.IModuleMessageCreater;
import com.elex.common.net.message.protocol.ICommandMessageFactory;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.type.ServiceType;

public class NetCustomConfig implements ICustomConfig {
	// {服务id:网络配置}
	private Map<String, INetHandlerConfig> map = new HashMap<>();
	private ICommandMessageFactory commandMessageFactory;

	private Map<MegProtocolType, IMessageCreater> messageCreaterMap = new HashMap<>();
	private Map<MegProtocolType, IModuleMessageCreater> moduleMessageCreaterMap = new HashMap<>();

	public NetCustomConfig() {
	}

	public void insertMessageCreater(IMessageCreater messageCreater) {
		messageCreaterMap.put(messageCreater.getMegProtocolType(), messageCreater);
	}

	public void insertModuleMessageCreater(IModuleMessageCreater messageCreater) {
		moduleMessageCreaterMap.put(messageCreater.getMegProtocolType(), messageCreater);
	}

	@Override
	public ServiceType[] getServiceTypes() {
		return new ServiceType[] { ServiceType.netserver, ServiceType.netclient };
	}

	public INetHandlerConfig getNetHandlerConfig(String sid) {
		return map.get(sid);
	}

	public void putNetHandlerConfig(String sid, INetHandlerConfig c) {
		map.put(sid, c);
	}

	public ICommandMessageFactory getCommandMessageFactory() {
		return commandMessageFactory;
	}

	public void setCommandMessageFactory(ICommandMessageFactory commandMessageFactory) {
		this.commandMessageFactory = commandMessageFactory;
	}

	public Map<MegProtocolType, IMessageCreater> getMessageCreaterMap() {
		return messageCreaterMap;
	}

	public Map<MegProtocolType, IModuleMessageCreater> getModuleMessageCreaterMap() {
		return moduleMessageCreaterMap;
	}
}
