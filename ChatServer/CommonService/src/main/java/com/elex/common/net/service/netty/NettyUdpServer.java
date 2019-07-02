package com.elex.common.net.service.netty;

import java.net.InetSocketAddress;

import com.elex.common.net.INetServer;
import com.elex.common.net.service.netty.filter.udp.MessageUdpDownEncoder;
import com.elex.common.net.service.netty.filter.udp.MessageUdpUpDecoder;
import com.elex.common.net.type.NetServiceType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
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

public class NettyUdpServer implements INetServer {
	protected static final ILogger logger = XLogUtil.logger();

	private NettyNetConfig config;
	// --------------------------------------------
	private EventLoopGroup workerGroup;
	private EventExecutorGroup handlerGroup;

	private Channel channel;

	@Override
	public void startup() {
		try {
			workerGroup = new NioEventLoopGroup(config.getSubReactorThreadNum());
			// 业务线程池
			handlerGroup = new DefaultEventExecutorGroup(config.getHandlerThreadNum());

			Bootstrap sb = new Bootstrap();
			sb.group(workerGroup);
			sb.channel(NioDatagramChannel.class);
			if (config.getHost() != null) {
				sb.localAddress(new InetSocketAddress(config.getHost(), config.getPort()));
			} else {
				sb.localAddress(new InetSocketAddress(config.getPort()));
			}
			sb.handler(new ChannelInitializer<DatagramChannel>() {
				@Override
				protected void initChannel(DatagramChannel ch) throws Exception {
					// ch.pipeline().addLast(new
					// LoggingHandler(LogLevel.ERROR));
					ch.pipeline().addLast(new LoggingHandler());

					ch.pipeline().addLast(new MessageUdpUpDecoder(config));
					ch.pipeline().addLast(new MessageUdpDownEncoder());
					ch.pipeline().addLast(handlerGroup, config.getHandler());
				}
			});

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
				logger.info("Start NettyUdpServer Success! [port=" + config.getPort() + ",ip=" + ip + "]");
			}
		} catch (Exception e) {
			logger.error("Start NettyUdpServer ERROR!", e);
			release();
		}
	}

	private void release() {
		if (logger.isInfoEnabled()) {
			logger.info("Release NettyUdpServer start!");
		}
		workerGroup.shutdownGracefully();
		handlerGroup.shutdownGracefully();
		if (logger.isInfoEnabled()) {
			String ip = config.getHost() == null ? "0.0.0.0" : config.getHost();
			logger.info("Release NettyUdpServer finished! [port=" + config.getPort() + ",ip=" + ip + "]");
		}
	}

	@Override
	public void shutdown() {
		if (logger.isInfoEnabled()) {
			logger.info("NettyUdpServer shutdown!");
		}
		channel.close();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NettyUdpServer [ mainReactorThreadNum=");
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
