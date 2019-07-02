package com.elex.common.net.service.netty;

import com.elex.common.net.event.ReconnectEvent;
import com.elex.common.net.service.netty.filter.tcp.MessageTcpDecoder;
import com.elex.common.net.service.netty.filter.tcp.MessageTcpEncoder;
import com.elex.common.net.service.netty.heartbeat.DefaultClientHeartBeatTimeoutHandler;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.NetProtocolType;
import com.elex.common.net.type.NetServiceType;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.google.common.eventbus.EventBus;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

/**
 * netty tcp 客户端
 * 
 * @author mausmars
 *
 */
public class NettyTcpClient implements INetNettyClient {
	protected static final ILogger logger = XLogUtil.logger();

	private NettyNetConfig config;
	// --------------------------------------------
	private volatile ISession session;
	private EventLoopGroup workerGroup;
	private EventExecutorGroup handlerGroup;

	private Bootstrap sb;

	private int id;

	private EventBus syncEventBus = new EventBus("ClientEvent");

	private ChannelFuture cf;

	public NettyTcpClient(int id) {
		this.id = id;
	}

	@Override
	public ISession getSession() {
		return session;
	}

	public  void registerListener() throws InterruptedException {
		if(cf != null) {
			//确保不重复添加listener
			cf.removeListener(connectListener);
			cf.addListener(connectListener);
			cf.channel().closeFuture().removeListener(closeListener);
			cf.channel().closeFuture().addListener(closeListener);
			cf.sync();
		}
	}

	@Override
	public int getId() {
		return id;
	}

	private ChannelFutureListener connectListener = new ChannelFutureListener() {
		@Override
		public void operationComplete(ChannelFuture cf) throws Exception {
			if (!cf.isSuccess()) {
				if (logger.isDebugEnabled()) {
					logger.debug("与服务器 连接建立失败 " + config.getHost() + ":" + config.getPort() + " " + id);
				}
//				 createReconnectTask(cf);
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("与服务器 连接建立成功 " + config.getHost() + ":" + config.getPort() + " " + id);
				}

				// 创建session
				session.replaceChannel(cf.channel());

				// 发送事件
				ReconnectEvent event = new ReconnectEvent();
				event.setSession(session);
				syncEventBus.post(event);
			}
		}
	};
	private ChannelFutureListener closeListener = new ChannelFutureListener() {
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			if (logger.isDebugEnabled()) {
				logger.debug("Close Reconnect !!!");
			}
			createReconnectTask(future);
		}
	};

	private void createReconnectTask(ChannelFuture future) {
		final EventLoop loop = future.channel().eventLoop();
		loop.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					connect();
				} catch (InterruptedException e) {
					logger.error("", e);
				}
			}
		}, 5L, TimeUnit.SECONDS);
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
			handlerGroup = new DefaultEventExecutorGroup(config.getHandlerThreadNum(),
					new NettyThreadFactory("TcpClient" + config.getPort()));

			sb = new Bootstrap();
			sb.group(workerGroup);
			sb.channel(NioSocketChannel.class);
			sb.handler(new ChannelInitializer<SocketChannel>() {
				// 4.0不允许加入加入一个ChannelHandler超过一次，除非它由@Sharable注解。
				// Be aware that sub-classes of ByteToMessageDecoder MUST NOT
				// annotated with @Sharable
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new LoggingHandler());
					ch.pipeline().addLast(new IdleStateHandler(config.getReaderIdleTimeSeconds(),
							config.getWriterIdleTimeSeconds(), config.getAllIdleTimeSeconds()));
					ch.pipeline().addLast(new DefaultClientHeartBeatTimeoutHandler());

					ch.pipeline().addLast(new MessageTcpDecoder());
					ch.pipeline().addLast(new MessageTcpEncoder());
					ch.pipeline().addLast(handlerGroup, config.getHandler());
				}
			});

			// 优化参数
			setOption(sb);

			// 注册监听器
			if (config.getListener() != null) {
				syncEventBus.register(config.getListener());
			}
			// 连接
			connect();
			if (logger.isInfoEnabled()) {
				logger.info(
						"Start NettyTcpClient Success! [host=" + config.getHost() + ",port=" + config.getPort() + "]");
			}
		} catch (Exception e) {
			logger.error("Start NettyTcpClient ERROR!", e);
			// release();
		}
	}

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
		sb.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);
		sb.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
	}

	private void connect() throws InterruptedException {
		cf = sb.connect(config.getHost(), config.getPort());
		if (session == null) {
			ISession newSession = config.getSessionFactory().createSession(cf.channel());
			session = newSession;

			// 设置服务id
			newSession.setAttach(SessionAttachType.ClientServiceId, id);
		}
		registerListener();
	}

	private void release() {
		if (logger.isInfoEnabled()) {
			logger.info("Release NettyTcpClient start!");
		}
		workerGroup.shutdownGracefully();
		handlerGroup.shutdownGracefully();
		if (logger.isInfoEnabled()) {
			logger.info("Release NettyTcpClient finished!");
		}
		cf = null;
		session = null;
	}

	@Override
	public void shutdown() {
		if (logger.isInfoEnabled()) {
			logger.info("NettyTcpClient shutdown!");
		}
		// client可以shutdown也可以session关闭
		session.close();
		release();
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
		sb.append("NettyTcpClient [ mainReactorThreadNum=");
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
