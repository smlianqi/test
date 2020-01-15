package com.elex.im.test.net.netty.tcp;

import java.util.ArrayList;
import java.util.List;

import com.elex.common.net.INetServer;
import com.elex.common.net.handler.IClosedSessionHandler;
import com.elex.common.net.handler.ICreateSessionHandler;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.message.MessageConfigMgr;
import com.elex.common.net.service.netty.ChannelOptionConfig;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.net.service.netty.NettyTcpServer;
import com.elex.common.net.service.netty.filter.tcp.MessageTcpHandler;
import com.elex.common.net.service.netty.handler.NettyTcpHandler;
import com.elex.common.net.service.netty.session.NettySessionFactory;
import com.elex.common.net.session.SessionManager;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.test.net.handler.flat.FlatHandlerConfig;
import com.elex.im.test.net.handler.proto.ProtoHandlerConfig;

public class NettyTcpStartServer {
	protected static final ILogger logger = XLogUtil.logger();

	private static boolean isFlatMessage = true;

	public static void main(String[] args) throws Exception {
		NettyTcpStartServer start = new NettyTcpStartServer();
		// 代码注入
		INetServer netServer = start.startServer();

		// Thread.sleep(10000);
		// netServer.shutdown();
	}

	private int port = 8799;

	private NettyTcpServer startServer() {
		NettyTcpServer nettyServer = new NettyTcpServer();
		nettyServer.setConfig(creatNetServerConfig());
		nettyServer.startup();
		return nettyServer;
	}

	private NettyNetConfig creatNetServerConfig() {
		List<MessageConfig> messageConfigs = null;
		if (isFlatMessage) {
			FlatHandlerConfig handlerConfig = new FlatHandlerConfig();
			messageConfigs = handlerConfig.createHandler();
		} else {
			ProtoHandlerConfig handlerConfig = new ProtoHandlerConfig();
			messageConfigs = handlerConfig.createHandler();
		}

		MessageConfigMgr messageConfigMgr = new MessageConfigMgr(messageConfigs);

		MessageTcpHandler messageHandler = new MessageTcpHandler();
		// messageHandler.setPoolExecutor(poolExecutor);
		messageHandler.setMessageConfigMgr(messageConfigMgr);
		// messageHandler.setFunctionService(functionService);
		// messageHandler.setMessageRounter(messageRounter);

		SessionManager sessionManager = new SessionManager();
		sessionManager.setMessageConfigMgr(messageConfigMgr);

		// 建立连接处理类
		List<ICreateSessionHandler> createSessionHandlerList = new ArrayList<ICreateSessionHandler>();

		// 断开连接处理类
		List<IClosedSessionHandler> closeSessionHandlerList = new ArrayList<IClosedSessionHandler>();

		NettySessionFactory sessionFactory=new NettySessionFactory();
		sessionFactory.setMessageHandler(messageHandler);
		sessionFactory.setSessionManager(sessionManager);
		
		NettyTcpHandler gameHandler = new NettyTcpHandler();
		gameHandler.setMessageHandler(messageHandler);
		gameHandler.setCreateSessionHandlers(createSessionHandlerList);
		gameHandler.setCloseSessionHandlers(closeSessionHandlerList);
		gameHandler.setSessionFactory(sessionFactory);

		// 网络配置
		NettyNetConfig config = new NettyNetConfig();
		config.setPort(port);
		config.setSubReactorThreadNum(5);
		config.setHandlerThreadNum(10);
		config.setAllIdleTimeSeconds(120);
		config.setReaderIdleTimeSeconds(60);
		config.setWriterIdleTimeSeconds(60);
		config.setHandler(gameHandler);
		config.setMessageConfigMgr(messageConfigMgr);
		if (isFlatMessage) {
			config.setMegProtocolType(MegProtocolType.flat);
		} else {
			config.setMegProtocolType(MegProtocolType.proto);
		}
		config.setSessionFactory(sessionFactory);
		// 优化配置
		ChannelOptionConfig coc = new ChannelOptionConfig();
		coc.setBacklog(128);
		coc.setKeepalive(true);
		coc.setNodelay(true);
		coc.setReuseaddr(true);

		coc.setLinger(1000);
		coc.setRcvbuf(1048576);
		coc.setSndbuf(1048576);
		coc.setConnectTimeoutMillis(1048576);

		config.setChannelOptionConfig(coc);
		return config;
	}
}
