package com.elex.common.net.service.netty;

import java.net.URI;

import com.elex.common.net.service.netty.filter.websocket.MessageWebSocketDownDecoder;
import com.elex.common.net.service.netty.filter.websocket.MessageWebSocketUpEncoder;
import com.elex.common.net.service.netty.filter.websocket.WebSocketHandshakerHandler;
import com.elex.common.net.service.netty.heartbeat.DefaultClientHeartBeatTimeoutHandler;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.NetProtocolType;
import com.elex.common.net.type.NetServiceType;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * netty http 客户端
 * 
 * @author mausmars
 *
 */
public class NettyWebSocketClient implements INetNettyClient {
	protected static final ILogger logger = XLogUtil.logger();

	private NettyNetConfig config;
	// --------------------------------------------
	private ISession session;
	private EventLoopGroup workerGroup;
	private EventExecutorGroup handlerGroup;

	// protocol://ip:prot
	private static final String Url_Template = "%s://%s:%d/websocket";

	private int id;

	public NettyWebSocketClient(int id) {
		this.id = id;
	}

	@Override
	public ISession getSession() {
		return session;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void startup() {
		try {
			// 初始化ServerBootstrap启动类
			// 第一个NioEventLoopGroup通常被称为'parentGroup'，用于接收所有连接到服务器端的客户端连接
			// EventLoopGroup bossGroup = new
			// NioEventLoopGroup(mainReactorThreadNum);// 用于接收发来的连接请求
			// 第二个被称为'childGroup',当有新的连接进来时将会被注册到worker中
			workerGroup = new NioEventLoopGroup(config.getSubReactorThreadNum()); // 用于处理parentGroup接收并注册给child的连接中的信息
			// 业务线程池
			handlerGroup = new DefaultEventExecutorGroup(config.getHandlerThreadNum());

			NetProtocolType netProtocolType = config.getNetProtocolType();

			final SslContext sslCtx;
			if (netProtocolType == NetProtocolType.wss) {
				sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
			} else if (netProtocolType == NetProtocolType.ws) {
				sslCtx = null;
			} else {
				throw new IllegalArgumentException("NetProtocolType is Fail!");
			}
			String url = String.format(Url_Template, netProtocolType.name(), config.getHost(), config.getPort());
			final URI uri = new URI(url);

			final WebSocketHandshakerHandler handler = new WebSocketHandshakerHandler(WebSocketClientHandshakerFactory
					.newHandshaker(uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders()));
			// handler.setConfig(config);
			Bootstrap sb = new Bootstrap();
			sb.group(workerGroup);
			sb.channel(NioSocketChannel.class);

			sb.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new LoggingHandler());// 日志
					if (sslCtx != null) {
						// 加密
						ch.pipeline().addLast(sslCtx.newHandler(ch.alloc(), uri.getHost(), uri.getPort()));
					}
					p.addLast(new HttpClientCodec());
					p.addLast(new HttpObjectAggregator(8192));
					p.addLast(WebSocketClientCompressionHandler.INSTANCE);
					p.addLast(handler);

					ch.pipeline().addLast(new IdleStateHandler(config.getReaderIdleTimeSeconds(),
							config.getWriterIdleTimeSeconds(), config.getAllIdleTimeSeconds()));
					ch.pipeline().addLast(new DefaultClientHeartBeatTimeoutHandler());

					p.addLast(new MessageWebSocketDownDecoder(config));
					p.addLast(new MessageWebSocketUpEncoder());
					p.addLast(handlerGroup, config.getHandler());
				}
			});

			// 优化参数
			setOption(sb);

			Channel ch = sb.connect(config.getHost(), config.getPort()).sync().channel();
			// 这里等待websocket 握手完毕
			handler.handshakeFuture().sync();

			// 创建session
			session = config.getSessionFactory().createSession(ch);

			ch.closeFuture().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					release();
				}
			});
			if (logger.isInfoEnabled()) {
				logger.info("Start NettyWebSocketClient Success!");
			}
		} catch (Exception e) {
			logger.error("Start NettyWebSocketClient ERROR!", e);
			release();
		}
	}

	// public void sendBinaryWebSocketFrame(Channel ch, String msg) {
	// TestUp.Builder builder = TestUp.newBuilder();
	// builder.setName("test_" + msg);
	// builder.setCount(123);
	//
	// NettyMessageType messageType = new NettyMessageType();
	// IMessage m = messageType.createMessage();
	// m.setCommandId(1);
	// // TODO 这里耦合proto了
	// m.setBodyByte(((MessageLite) builder.build()).toByteArray());
	//
	// ByteBuf byteBuf = Unpooled.directBuffer();
	// m.write(byteBuf);
	// // websocket写二进制消息
	// WebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
	// session.write(frame);
	// }

	private void setOption(Bootstrap sb) {
		ChannelOptionConfig coc = config.getChannelOptionConfig();
		if (coc == null) {
			return;
		}
		// option() 方法用于设置监听套接字
		if (coc.getNodelay() != null) {
			// 禁用negal算法
			sb.option(ChannelOption.TCP_NODELAY, coc.getNodelay());
		}
		if (coc.getKeepalive() != null) {
			// childOption()则用于设置连接到服务器的客户端套接字
			sb.option(ChannelOption.SO_KEEPALIVE, coc.getKeepalive());
		}
	}

	private void release() {
		if (logger.isInfoEnabled()) {
			logger.info("Release NettyWebSocketClient start!");
		}
		workerGroup.shutdownGracefully();
		handlerGroup.shutdownGracefully();
		if (logger.isInfoEnabled()) {
			logger.info("Release NettyWebSocketClient finished!");
		}
	}

	@Override
	public void shutdown() {
		if (logger.isInfoEnabled()) {
			logger.info("NettyWebSocketClient shutdown!");
		}
		// client可以shutdown也可以session关闭
		session.close();
	}

	@Override
	public void send(Object message) {
		session.send(message);
	}

	@Override
	public void setSessionAttachment(SessionAttachType key, Object attachment) {
		session.setAttach(key, attachment);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NettyWebSocketClient [ mainReactorThreadNum=");
		sb.append(config.getMainReactorThreadNum());
		sb.append(" subReactorThreadNum=");
		sb.append(config.getSubReactorThreadNum());
		sb.append(" handlerThreadNum=");
		sb.append(config.getHandlerThreadNum());
		sb.append("]");
		return sb.toString();
	}

	@Override
	public NetServiceType getNetServiceType() {
		return NetServiceType.netty;
	}

	@Override
	public NetProtocolType getNetProtocolType() {
		return config.getNetProtocolType();
	}

	// -----------------------------------------
	public void setConfig(NettyNetConfig config) {
		this.config = config;
	}
}
