package com.elex.im.test.net.netty.http;

import java.util.ArrayList;
import java.util.List;

import com.elex.common.net.INetServer;
import com.elex.common.net.handler.IClosedSessionHandler;
import com.elex.common.net.handler.ICreateSessionHandler;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.message.MessageConfigMgr;
import com.elex.common.net.message.protocol.ICommandMessageFactory;
import com.elex.common.net.message.protocol.flat.FlatCommandMessageFactory;
import com.elex.common.net.message.protocol.proto.ProtoCommandMessageFactory;
import com.elex.common.net.service.netty.ChannelOptionConfig;
import com.elex.common.net.service.netty.NettyHttpServer;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.net.service.netty.filter.http.MessageHttpHandler;
import com.elex.common.net.service.netty.handler.NettyTcpHandler;
import com.elex.common.net.service.netty.session.NettySessionFactory;
import com.elex.common.net.session.SessionManager;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.net.type.NetProtocolType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.test.net.handler.flat.FlatHandlerConfig;
import com.elex.im.test.net.handler.proto.ProtoHandlerConfig;

/**
 * netty http服务端测试
 * 
 * @author mausmars
 *
 */
public class NettyHttpStartServer {
	protected static final ILogger logger = XLogUtil.logger();

	private static boolean isFlatMessage = true;
	
	public static void main(String[] args) throws Exception {
		NettyHttpStartServer start = new NettyHttpStartServer();

		INetServer netServer = start.startServer();

		// Thread.sleep(5000);
		// netServer.shutdown();
	}

	private int port = 8799;

	public INetServer startServer() {
		NettyHttpServer nettyServer = new NettyHttpServer();
		nettyServer.setConfig(creatNetServerConfig());
		nettyServer.startup();
		return nettyServer;
	}

	private NettyNetConfig creatNetServerConfig() {
		List<MessageConfig> messageConfigs = null;
		ICommandMessageFactory commandMessageFactory = null;
		if (isFlatMessage) {
			commandMessageFactory = new FlatCommandMessageFactory();
			FlatHandlerConfig handlerConfig = new FlatHandlerConfig();
			messageConfigs = handlerConfig.createHandler();
		} else {
			commandMessageFactory = new ProtoCommandMessageFactory();
			ProtoHandlerConfig handlerConfig = new ProtoHandlerConfig();
			messageConfigs = handlerConfig.createHandler();
		}

		MessageConfigMgr messageConfigMgr = new MessageConfigMgr(messageConfigs);

		// 网络配置
		NettyNetConfig config = new NettyNetConfig();
		config.setNetProtocolType(NetProtocolType.http);
		config.setPort(port);
		config.setSubReactorThreadNum(5);
		config.setHandlerThreadNum(10);
		config.setAllIdleTimeSeconds(120);
		config.setReaderIdleTimeSeconds(60);
		config.setWriterIdleTimeSeconds(60);
		config.setMessageConfigMgr(messageConfigMgr);
		config.setCommandMessageFactory(commandMessageFactory);
		if (isFlatMessage) {
			config.setMegProtocolType(MegProtocolType.flat);
		} else {
			config.setMegProtocolType(MegProtocolType.proto);
		}
		
		MessageHttpHandler messageHandler = new MessageHttpHandler(config);
		// messageHandler.setPoolExecutor(poolExecutor);
		messageHandler.setMessageConfigMgr(messageConfigMgr);

		// ------
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

		config.setHandler(gameHandler);
		config.setSessionFactory(sessionFactory);
		// 优化配置
		ChannelOptionConfig coc = new ChannelOptionConfig();
		coc.setBacklog(128);
		coc.setKeepalive(true);
		coc.setNodelay(true);
		coc.setReuseaddr(true);

		coc.setTimeout(3000);
		coc.setLinger(1000);
		coc.setRcvbuf(1048576);
		coc.setSndbuf(1048576);
		coc.setConnectTimeoutMillis(10000);

		return config;
	}
}
