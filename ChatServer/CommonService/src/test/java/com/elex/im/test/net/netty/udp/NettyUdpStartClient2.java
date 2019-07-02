package com.elex.im.test.net.netty.udp;

import java.util.List;

import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.message.MessageConfigMgr;
import com.elex.common.net.message.protocol.ICommandMessageFactory;
import com.elex.common.net.message.protocol.flat.FlatCommandMessageFactory;
import com.elex.common.net.message.protocol.proto.ProtoCommandMessageFactory;
import com.elex.common.net.service.netty.INetNettyClient;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.net.service.netty.NettyUdpClient;
import com.elex.common.net.service.netty.filter.udp.MessageUdpHandler;
import com.elex.common.net.service.netty.handler.NettyUdpHandler;
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
public class NettyUdpStartClient2 {
	protected static final ILogger logger = XLogUtil.logger();

	private static boolean isFlatMessage = true;

	public static void main(String[] args) throws Exception {
		int useCount = 1;// 用户数量
		for (int i = 1; i <= useCount; i++) {
			sendtest(i);
		}
	}

	private static void sendtest(int id) throws Exception {
		NettyUdpStartClient2 start = new NettyUdpStartClient2();

		INetNettyClient netClient = start.createNettyClient("10.1.17.242", 8799);

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

	public INetNettyClient createNettyClient(String host, int port) {
		NettyUdpClient nettyClient = new NettyUdpClient(1);
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
		config.setHost(host);
		config.setPort(port);
		config.setSubReactorThreadNum(5);
		config.setHandlerThreadNum(10);
		config.setAllIdleTimeSeconds(300);
		config.setReaderIdleTimeSeconds(150);
		config.setWriterIdleTimeSeconds(150);
		config.setMessageConfigMgr(messageConfigMgr);
		config.setCommandMessageFactory(commandMessageFactory);

		MessageUdpHandler messageHandler = new MessageUdpHandler(config);
		// messageHandler.setPoolExecutor(poolExecutor);
		messageHandler.setMessageConfigMgr(messageConfigMgr);

		NettyUdpHandler gameHandler = new NettyUdpHandler();
		gameHandler.setMessageHandler(messageHandler);

		config.setHandler(gameHandler);
		// 优化配置
		return config;
	}
}
