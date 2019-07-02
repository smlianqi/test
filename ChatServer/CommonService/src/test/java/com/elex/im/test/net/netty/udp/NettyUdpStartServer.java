package com.elex.im.test.net.netty.udp;

import java.util.List;

import com.elex.common.net.INetServer;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.message.MessageConfigMgr;
import com.elex.common.net.message.protocol.ICommandMessageFactory;
import com.elex.common.net.message.protocol.flat.FlatCommandMessageFactory;
import com.elex.common.net.message.protocol.proto.ProtoCommandMessageFactory;
import com.elex.common.net.service.netty.ChannelOptionConfig;
import com.elex.common.net.service.netty.NettyNetConfig;
import com.elex.common.net.service.netty.NettyUdpServer;
import com.elex.common.net.service.netty.filter.udp.MessageUdpHandler;
import com.elex.common.net.service.netty.handler.NettyUdpHandler;
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
public class NettyUdpStartServer {
	protected static final ILogger logger = XLogUtil.logger();

	private static boolean isFlatMessage = true;

	public static void main(String[] args) throws Exception {
		NettyUdpStartServer start = new NettyUdpStartServer();
		// 代码注入
		INetServer netServer = start.startServer();

		// Thread.sleep(10000);
		// netServer.shutdown();
	}

	private int port = 8799;

	private NettyUdpServer startServer() {
		NettyUdpServer nettyServer = new NettyUdpServer();
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

		NettyNetConfig config = new NettyNetConfig();
		config.setPort(port);
		config.setSubReactorThreadNum(5);
		config.setHandlerThreadNum(10);
		config.setAllIdleTimeSeconds(120);
		config.setReaderIdleTimeSeconds(60);
		config.setWriterIdleTimeSeconds(60);
		config.setMessageConfigMgr(messageConfigMgr);
		config.setCommandMessageFactory(commandMessageFactory);

		MessageUdpHandler messageHandler = new MessageUdpHandler(config);
		// messageHandler.setPoolExecutor(poolExecutor);
		messageHandler.setMessageConfigMgr(messageConfigMgr);

		// netty udp处理器
		NettyUdpHandler gameHandler = new NettyUdpHandler();
		gameHandler.setMessageHandler(messageHandler);

		config.setHandler(gameHandler);
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
