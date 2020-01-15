package com.elex.im.test.net.netty.http;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.elex.common.net.handler.IClosedSessionHandler;
import com.elex.common.net.handler.ICreateSessionHandler;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.message.MessageConfigMgr;
import com.elex.common.net.message.protocol.ICommandMessageFactory;
import com.elex.common.net.message.protocol.flat.FlatCommandMessageFactory;
import com.elex.common.net.message.protocol.proto.ProtoCommandMessageFactory;
import com.elex.common.net.service.netty.ChannelOptionConfig;
import com.elex.common.net.service.netty.INetNettyClient;
import com.elex.common.net.service.netty.NettyHttpClient;
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
import com.elex.im.test.net.message.flat.Test1Bean;
import com.elex.im.test.net.message.flat.Test1Up;
import com.elex.im.test.net.message.proto.Test1Message;
import com.google.flatbuffers.FlatBufferBuilder;

/**
 * netty http客户端测试
 * 
 * @author mausmars
 *
 */
public class NettyHttpStartClient {
	protected static final ILogger logger = XLogUtil.logger();

	private static boolean isFlatMessage = true;

	public static void main(String[] args) throws Exception {
		int useCount = 1;// 用户数量
		for (int i = 1; i <= useCount; i++) {
			sendtest(i);
		}
	}

	private static void sendtest(int id) throws Exception {
		NettyHttpStartClient start = new NettyHttpStartClient();

		String host = "127.0.0.1";
		int port = 8799;
		INetNettyClient netClient = start.createNettyClient(host, port);

		if (isFlatMessage) {
			sendFlatMessage(netClient, id);
		} else {
			sendProtoMessage(netClient, id);
		}

		// Thread.sleep(10000);
		// netClient.shutdown();
	}

	private static void sendFlatMessage(INetNettyClient netClient, int id) {
		int sendCount = 1;// 发送次数
		for (int i = 1; i <= sendCount; i++) {
			FlatBufferBuilder builder = new FlatBufferBuilder(0);

			int tokenOffset = builder.createString("test_" + id);
			int userIdStrOffset = builder.createString("test_" + id);

			int testBeanOffset = Test1Bean.createTest1Bean(builder, tokenOffset, i, userIdStrOffset, i);

			Test1Up.startTest1Up(builder);
			Test1Up.addTestBean(builder, testBeanOffset);
			int john = Test1Up.endTest1Up(builder);
			builder.finish(john);

			IMessage msg = Message.createFlatMessage(builder.dataBuffer(), Test1Up.class);

			netClient.send(msg);
		}
	}

	// 发送proto消息
	private static void sendProtoMessage(INetNettyClient netClient, int id) {
		int sendCount = 1;// 发送次数
		for (int i = 1; i <= sendCount; i++) {
			// 发送proto message
			Test1Message.Test1Up.Builder builder = Test1Message.Test1Up.newBuilder();
			Test1Message.Test1Bean.Builder beanBuilder = Test1Message.Test1Bean.newBuilder();
			beanBuilder.setUserIdStr("test_" + id);
			beanBuilder.setToken("test_" + id);
			builder.setTestBean(beanBuilder);

			beanBuilder.setRid(i);
			beanBuilder.setAsid(i);

			netClient.send(builder.build());
		}
	}

	public INetNettyClient createNettyClient(String host, int port) throws URISyntaxException {
		NettyHttpClient nettyClient = new NettyHttpClient(1);
		nettyClient.setConfig(creatNetClientConfig(host, port));
		// 启动
		nettyClient.startup();
		return nettyClient;
	}

	private NettyNetConfig creatNetClientConfig(String host, int port) {
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
		config.setHost(host);
		config.setPort(port);
		config.setSubReactorThreadNum(5);
		config.setHandlerThreadNum(10);
		config.setAllIdleTimeSeconds(300);
		config.setReaderIdleTimeSeconds(150);
		config.setWriterIdleTimeSeconds(150);
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

		SessionManager messageSender = new SessionManager();
		messageSender.setMessageConfigMgr(messageConfigMgr);

		// 建立连接处理类
		List<ICreateSessionHandler> createSessionHandlerList = new ArrayList<ICreateSessionHandler>();

		// 断开连接处理类
		List<IClosedSessionHandler> closeSessionHandlerList = new ArrayList<IClosedSessionHandler>();

		NettySessionFactory sessionFactory = new NettySessionFactory();
		sessionFactory.setMessageHandler(messageHandler);
		sessionFactory.setSessionManager(messageSender);

		NettyTcpHandler gameHandler = new NettyTcpHandler();
		gameHandler.setMessageHandler(messageHandler);
		gameHandler.setCreateSessionHandlers(createSessionHandlerList);
		gameHandler.setCloseSessionHandlers(closeSessionHandlerList);
		gameHandler.setSessionFactory(sessionFactory);

		config.setHandler(gameHandler);
		config.setSessionFactory(sessionFactory);
		// 优化配置
		ChannelOptionConfig coc = new ChannelOptionConfig();
		// coc.setBacklog(128);
		coc.setKeepalive(true);
		coc.setNodelay(true);
		// coc.setReuseaddr(true);

		// coc.setLinger(1000);
		// coc.setRcvbuf(1048576);
		// coc.setSndbuf(1048576);
		// coc.setConnectTimeoutMillis(1048576);

		config.setChannelOptionConfig(coc);
		return config;
	}
}
