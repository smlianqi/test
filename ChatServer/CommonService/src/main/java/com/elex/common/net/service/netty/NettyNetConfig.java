package com.elex.common.net.service.netty;

import com.elex.common.net.message.IMessageConfigMgr;
import com.elex.common.net.message.protocol.ICommandMessageFactory;
import com.elex.common.net.session.ISessionFactory;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.net.type.NetProtocolType;

import io.netty.channel.ChannelHandler;

/**
 * 网络客户端配置
 * 
 * @author mausmars
 *
 */
public class NettyNetConfig {
	protected String host; // host
	protected int port; // 端口
	protected NetProtocolType netProtocolType;// 网络协议类型

	protected int mainReactorThreadNum = Runtime.getRuntime().availableProcessors() * 2; // 默认（内核）
	protected int subReactorThreadNum = 4; // 子反应器线程数量
	protected int handlerThreadNum = 5; // 业务线程数量

	protected int readerIdleTimeSeconds; // 读超时(s)
	protected int writerIdleTimeSeconds; // 写超时(s)
	protected int allIdleTimeSeconds;// 全部超时(s)

	protected MegProtocolType megProtocolType;// 消息协议类型

	protected IMessageConfigMgr messageConfigMgr;
	protected ChannelHandler handler;

	// channel优化配置
	protected ChannelOptionConfig channelOptionConfig;

	protected ICommandMessageFactory commandMessageFactory;

	protected ISessionFactory sessionFactory;

	protected Object listener;
	// -------------------------

	public NettyNetConfig clone() {
		NettyNetConfig netConfig = new NettyNetConfig();
		netConfig.host = host;
		netConfig.port = port;
		netConfig.netProtocolType = netProtocolType;

		netConfig.mainReactorThreadNum = mainReactorThreadNum;
		netConfig.subReactorThreadNum = subReactorThreadNum;
		netConfig.handlerThreadNum = handlerThreadNum;

		netConfig.readerIdleTimeSeconds = readerIdleTimeSeconds;
		netConfig.writerIdleTimeSeconds = writerIdleTimeSeconds;
		netConfig.allIdleTimeSeconds = allIdleTimeSeconds;

		netConfig.messageConfigMgr = messageConfigMgr;
		netConfig.handler = handler;

		netConfig.channelOptionConfig = channelOptionConfig;
		netConfig.commandMessageFactory = commandMessageFactory;
		netConfig.sessionFactory = sessionFactory;
		netConfig.listener = listener;
		
		netConfig.megProtocolType = megProtocolType;
		
		return netConfig;
	}

	public int getMainReactorThreadNum() {
		return mainReactorThreadNum;
	}

	public void setMainReactorThreadNum(int mainReactorThreadNum) {
		if (mainReactorThreadNum > 0 && mainReactorThreadNum < this.mainReactorThreadNum) {
			this.mainReactorThreadNum = mainReactorThreadNum;
		}
	}

	public int getSubReactorThreadNum() {
		return subReactorThreadNum;
	}

	public void setSubReactorThreadNum(int subReactorThreadNum) {
		this.subReactorThreadNum = subReactorThreadNum;
	}

	public int getHandlerThreadNum() {
		return handlerThreadNum;
	}

	public void setHandlerThreadNum(int handlerThreadNum) {
		this.handlerThreadNum = handlerThreadNum;
	}

	public int getReaderIdleTimeSeconds() {
		return readerIdleTimeSeconds;
	}

	public void setReaderIdleTimeSeconds(int readerIdleTimeSeconds) {
		this.readerIdleTimeSeconds = readerIdleTimeSeconds;
	}

	public int getWriterIdleTimeSeconds() {
		return writerIdleTimeSeconds;
	}

	public void setWriterIdleTimeSeconds(int writerIdleTimeSeconds) {
		this.writerIdleTimeSeconds = writerIdleTimeSeconds;
	}

	public int getAllIdleTimeSeconds() {
		return allIdleTimeSeconds;
	}

	public void setAllIdleTimeSeconds(int allIdleTimeSeconds) {
		this.allIdleTimeSeconds = allIdleTimeSeconds;
	}

	public IMessageConfigMgr getMessageConfigMgr() {
		return messageConfigMgr;
	}

	public void setMessageConfigMgr(IMessageConfigMgr messageConfigMgr) {
		this.messageConfigMgr = messageConfigMgr;
	}

	public ChannelHandler getHandler() {
		return handler;
	}

	public void setHandler(ChannelHandler handler) {
		this.handler = handler;
	}

	public ChannelOptionConfig getChannelOptionConfig() {
		return channelOptionConfig;
	}

	public void setChannelOptionConfig(ChannelOptionConfig channelOptionConfig) {
		this.channelOptionConfig = channelOptionConfig;
	}

	public NetProtocolType getNetProtocolType() {
		return netProtocolType;
	}

	public void setNetProtocolType(NetProtocolType netProtocolType) {
		this.netProtocolType = netProtocolType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ICommandMessageFactory getCommandMessageFactory() {
		return commandMessageFactory;
	}

	public void setCommandMessageFactory(ICommandMessageFactory commandMessageFactory) {
		this.commandMessageFactory = commandMessageFactory;
	}

	public void setMegProtocolType(MegProtocolType megProtocolType) {
		this.megProtocolType = megProtocolType;
	}

	public MegProtocolType getMegProtocolType() {
		return megProtocolType;
	}

	public ISessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(ISessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setListener(Object listener) {
		this.listener = listener;
	}

	public Object getListener() {
		return listener;
	}
}
