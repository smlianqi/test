package com.elex.common.net.service.netty;

import java.net.InetSocketAddress;

import com.elex.common.net.service.netty.filter.udp.MessageUdpDownDecoder;
import com.elex.common.net.service.netty.filter.udp.MessageUdpUpEncoder;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.NetProtocolType;
import com.elex.common.net.type.NetServiceType;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class NettyUdpClient implements INetNettyClient {
	protected static final ILogger logger = XLogUtil.logger();

	private NettyNetConfig config;
	// --------------------------------------------
	private ISession session;
	private EventLoopGroup workerGroup;
	private EventExecutorGroup handlerGroup;

	private int id;

	public NettyUdpClient(int id) {
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
			// 第二个被称为'childGroup',当有新的连接进来时将会被注册到worker中
			workerGroup = new NioEventLoopGroup(config.getSubReactorThreadNum()); // 用于处理parentGroup接收并注册给child的连接中的信息
			// 业务线程池
			handlerGroup = new DefaultEventExecutorGroup(config.getHandlerThreadNum());

			Bootstrap sb = new Bootstrap();
			sb.group(workerGroup);
			sb.channel(NioDatagramChannel.class);
			sb.handler(new ChannelInitializer<DatagramChannel>() {
				@Override
				public void initChannel(DatagramChannel ch) throws Exception {
					// ch.pipeline().addLast(new
					// LoggingHandler(LogLevel.ERROR));
					ch.pipeline().addLast(new LoggingHandler());

					ch.pipeline().addLast(new MessageUdpDownDecoder(config));
					ch.pipeline().addLast(new MessageUdpUpEncoder());
					ch.pipeline().addLast(handlerGroup, config.getHandler());
				}
			});

			ChannelFuture cf = sb.bind(0).sync();
			InetSocketAddress address = new InetSocketAddress(config.getHost(), config.getPort());

			// 创建session
			session = config.getSessionFactory().createSession(cf.channel());
			// 设置地址
			session.setAttach(SessionAttachType.TargetSocketAddress, address);

			cf.channel().closeFuture().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					release();
				}
			});
			if (logger.isInfoEnabled()) {
				logger.info(
						"Start NettyUdpClient Success! [host=" + config.getHost() + ",port=" + config.getPort() + "]");
			}
		} catch (InterruptedException e) {
			logger.error("Start NettyUdpClient ERROR!", e);
			release();
		}
	}

	private void release() {
		if (logger.isInfoEnabled()) {
			logger.info("Release NettyUdpClient start!");
		}
		workerGroup.shutdownGracefully();
		handlerGroup.shutdownGracefully();
		if (logger.isInfoEnabled()) {
			logger.info("Release NettyUdpClient finished!");
		}
	}

	@Override
	public void shutdown() {
		if (logger.isInfoEnabled()) {
			logger.info("NettyUdpClient shutdown!");
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
		sb.append("NettyUdpClient [ mainReactorThreadNum=");
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
