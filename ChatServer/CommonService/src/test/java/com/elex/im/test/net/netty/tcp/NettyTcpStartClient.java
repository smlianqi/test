package com.elex.im.test.net.netty.tcp;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elex.common.net.handler.IClosedSessionHandler;
import com.elex.common.net.handler.ICreateSessionHandler;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.message.MessageConfigMgr;
import com.elex.common.net.service.netty.ChannelOptionConfig;
import com.elex.common.net.service.netty.INetNettyClient;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.net.service.netty.NettyTcpClient;
import com.elex.common.net.service.netty.filter.tcp.MessageTcpHandler;
import com.elex.common.net.service.netty.handler.NettyTcpHandler;
import com.elex.common.net.service.netty.session.NettySessionFactory;
import com.elex.common.net.session.SessionManager;
import com.elex.common.net.type.MegProtocolType;
import com.elex.im.test.net.ClosedSessionHandler;
import com.elex.im.test.net.handler.flat.FlatHandlerConfig;
import com.elex.im.test.net.handler.proto.ProtoHandlerConfig;
import com.elex.im.test.net.message.flat.Test1Bean;
import com.elex.im.test.net.message.flat.Test1Up;
import com.elex.im.test.net.message.proto.Test1Message;
import com.google.flatbuffers.FlatBufferBuilder;

public class NettyTcpStartClient {
	protected static final Logger logger = LogManager.getLogger(NettyTcpStartClient.class);

	private static boolean isFlatMessage = true;

	public static void main(String[] args) throws Exception {
		int useCount = 2;// 用户数量
		for (int i = 1; i <= useCount; i++) {
			sendtest(i);
		}
	}

	private static void sendtest(int id) throws Exception {
		NettyTcpStartClient start = new NettyTcpStartClient();

		INetNettyClient netClient = start.createNettyClient("localhost", 8799);
		// INetClient netClient = start.createNettyClient("192.168.0.221",
		// 10001);

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
		int sendCount = 100;// 发送次数
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

	public INetNettyClient createNettyClient(String host, int port) {
		NettyTcpClient nettyClient = new NettyTcpClient(1);
		nettyClient.setConfig(creatNetClientConfig(host, port));
		// 启动
		nettyClient.startup();
		return nettyClient;
	}

	private NettyNetConfig creatNetClientConfig(String host, int port) {
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

		SessionManager messageSender = new SessionManager();
		messageSender.setMessageConfigMgr(messageConfigMgr);

		// 建立连接处理类
		List<ICreateSessionHandler> createSessionHandlerList = new ArrayList<ICreateSessionHandler>();

		// 断开连接处理类
		List<IClosedSessionHandler> closeSessionHandlerList = new ArrayList<IClosedSessionHandler>();
		closeSessionHandlerList.add(new ClosedSessionHandler());

		NettySessionFactory sessionFactory=new NettySessionFactory();
		sessionFactory.setMessageHandler(messageHandler);
		sessionFactory.setSessionManager(messageSender);
		
		NettyTcpHandler gameHandler = new NettyTcpHandler();
		gameHandler.setMessageHandler(messageHandler);
		gameHandler.setCreateSessionHandlers(createSessionHandlerList);
		gameHandler.setCloseSessionHandlers(closeSessionHandlerList);
		gameHandler.setSessionFactory(sessionFactory);

		// 网络配置
		NettyNetConfig config = new NettyNetConfig();
		config.setHost(host);
		config.setPort(port);
		config.setSubReactorThreadNum(5);
		config.setHandlerThreadNum(10);
		config.setAllIdleTimeSeconds(300);
		config.setReaderIdleTimeSeconds(150);
		config.setWriterIdleTimeSeconds(150);
		config.setHandler(gameHandler);
		config.setMessageConfigMgr(messageConfigMgr);
		config.setSessionFactory(sessionFactory);
		if (isFlatMessage) {
			config.setMegProtocolType(MegProtocolType.flat);
		} else {
			config.setMegProtocolType(MegProtocolType.proto);
		}

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
