package com.elex.common.net.service.netty;

import java.net.InetSocketAddress;

import com.elex.common.net.INetServer;
import com.elex.common.net.service.netty.filter.tcp.MessageTcpDecoder;
import com.elex.common.net.service.netty.filter.tcp.MessageTcpEncoder;
import com.elex.common.net.service.netty.heartbeat.DefaultServerHeartBeatTimeoutHandler;
import com.elex.common.net.type.NetServiceType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * netty tcp 服务
 * 
 * @author mausmars
 *
 */
public class NettyTcpServer implements INetServer {
	protected static final ILogger logger = XLogUtil.logger();

	private NettyNetConfig config;
	// --------------------------------------------
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private EventExecutorGroup handlerGroup;
	private Channel channel;

	@Override
	public void startup() {
		try {
			// 初始化ServerBootstrap启动类
			// 第一个NioEventLoopGroup通常被称为'parentGroup'，用于接收所有连接到服务器端的客户端连接
			bossGroup = new NioEventLoopGroup(config.getMainReactorThreadNum());// 用于接收发来的连接请求
			// 第二个被称为'childGroup',当有新的连接进来时将会被注册到worker中
			workerGroup = new NioEventLoopGroup(config.getSubReactorThreadNum()); // 用于处理parentGroup接收并注册给child的连接中的信息
			// 业务线程池
			handlerGroup = new DefaultEventExecutorGroup(config.getHandlerThreadNum(),
					new NettyThreadFactory("TcpServer" + config.getPort()));

			ServerBootstrap sb = new ServerBootstrap();
			sb.group(bossGroup, workerGroup);
			sb.channel(NioServerSocketChannel.class);
			if (config.getHost() != null) {
				sb.localAddress(new InetSocketAddress(config.getHost(), config.getPort()));
			} else {
				sb.localAddress(new InetSocketAddress(config.getPort()));
			}
			sb.childHandler(new ChannelInitializer<SocketChannel>() {
				// 4.0不允许加入加入一个ChannelHandler超过一次，除非它由@Sharable注解。
				// Be aware that sub-classes of ByteToMessageDecoder MUST NOT
				// annotated with @Sharable
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new LoggingHandler());
					ch.pipeline().addLast(new IdleStateHandler(config.getReaderIdleTimeSeconds(),
							config.getWriterIdleTimeSeconds(), config.getAllIdleTimeSeconds()));
					ch.pipeline().addLast(new DefaultServerHeartBeatTimeoutHandler());

					ch.pipeline().addLast(new MessageTcpDecoder());
					ch.pipeline().addLast(new MessageTcpEncoder());
					ch.pipeline().addLast(handlerGroup, config.getHandler());
				}
			});

			// 优化参数
			setOption(sb);

			ChannelFuture cf = sb.bind().sync();
			channel = cf.channel();
			channel.closeFuture().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					release();
				}
			});
			if (logger.isInfoEnabled()) {
				String ip = config.getHost() == null ? "0.0.0.0" : config.getHost();
				logger.info("Start NettyTcpServer Success! [port=" + config.getPort() + ",ip=" + ip + "]");
			}
		} catch (Exception e) {
			logger.error("Start NettyTcpServer ERROR!", e);
			release();
		}
	}

	private void setOption(ServerBootstrap sb) {
		ChannelOptionConfig coc = config.getChannelOptionConfig();
		if (coc == null) {
			return;
		}
		// option() 方法用于设置监听套接字
		if (coc.getBacklog() != null) {
			// ServerSocket的最大客户端等待队列
			sb.option(ChannelOption.SO_BACKLOG, coc.getBacklog());
		}
		if (coc.getKeepalive() != null) {
			// childOption()则用于设置连接到服务器的客户端套接字
			sb.childOption(ChannelOption.SO_KEEPALIVE, coc.getKeepalive());
		}
		if (coc.getNodelay() != null) {
			// 禁用negal算法
			sb.childOption(ChannelOption.TCP_NODELAY, coc.getNodelay());
		}
		if (coc.getReuseaddr() != null) {
			// 重用地址
			sb.childOption(ChannelOption.SO_REUSEADDR, coc.getReuseaddr());
		}
		if (coc.getLinger() != null) {
			// 那么close会等到发送的数据已经确认了才返回。但是如果对方宕机，超时，那么会根据linger设定的时间返回。
			sb.childOption(ChannelOption.SO_LINGER, coc.getLinger());
		}
		if (coc.getTimeout() != null) {
			// 设置socket调用InputStream读数据的超时时间，以毫秒为单位，如果超过这个时候，会抛出java.net.SocketTimeoutException。
			// sb.childOption(ChannelOption.SO_TIMEOUT, coc.getTimeout());
			// 阻塞io才有用 SO_TIMEOUT has effect only for OIO socket transport
		}
		if (coc.getRcvbuf() != null) {
			sb.childOption(ChannelOption.SO_RCVBUF, coc.getRcvbuf());
		}
		if (coc.getSndbuf() != null) {
			sb.childOption(ChannelOption.SO_SNDBUF, coc.getSndbuf());
		}
		if (coc.getConnectTimeoutMillis() != null) {
			sb.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, coc.getConnectTimeoutMillis());
		}
		// heap buf 's better
		// ByteBufoutBuf=UnpooledByteBufAllocator.DEFAULT.heapBuffer();
		// ByteBufoutBuf=PooledByteBufAllocator.DEFAULT.heapBuffer();
		// ByteBufoutBuf=PooledByteBufAllocator.DEFAULT.buffer();
		// ByteBufoutBuf=UnpooledByteBufAllocator.DEFAULT.buffer();
		sb.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		sb.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
	}

	private void release() {
		if (logger.isInfoEnabled()) {
			logger.info("Release NettyTcpServer start!");
		}
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		handlerGroup.shutdownGracefully();

		if (logger.isInfoEnabled()) {
			String ip = config.getHost() == null ? "0.0.0.0" : config.getHost();
			logger.info("Release NettyTcpServer finished! [port=" + config.getPort() + ",ip=" + ip + "]");
		}
	}

	@Override
	public void shutdown() {
		if (logger.isInfoEnabled()) {
			logger.info("NettyTcpServer shutdown!");
		}
		channel.close();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NettyTcpServer [ mainReactorThreadNum=");
		sb.append(config.getMainReactorThreadNum());
		sb.append(" subReactorThreadNum=");
		sb.append(config.getSubReactorThreadNum());
		sb.append(" handlerThreadNum=");
		sb.append(config.getHandlerThreadNum());
		sb.append("]");
		return sb.toString();
	}

	@Override
	public String getIp() {
		return config.getHost();
	}

	@Override
	public int getPort() {
		return config.getPort();
	}

	@Override
	public NetServiceType getNetServiceType() {
		return NetServiceType.netty;
	}

	@Override
	public void setConfig(Object config) {
		this.config = (NettyNetConfig) config;
	}
}
