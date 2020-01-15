package com.elex.common.net.service.netty.session;

import com.elex.common.net.session.ISession;
import com.elex.common.net.type.ConnectType;
import com.elex.common.net.type.SessionAttachType;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * netty session 封装
 * 
 * @author mausmars
 * 
 * 
 */
public class NettySession implements ISession {
	private Channel channel;

	private ConcurrentMap<Object, Object> attach = new ConcurrentHashMap<>();

	public NettySession(Channel channel) {
		this.channel = channel;
		bind();
	}

	private void bind() {
		// 绑定session
		AttributeKey<Object> k = AttributeKey.valueOf(SessionAttachType.ISession_Key.toString());
		this.channel.attr(k).set(this);
	}

	@Override
	public void replaceChannel(Object channel) {
		if (channel instanceof Channel) {
			Channel c = (Channel) channel;

			AttributeKey<Object> k = AttributeKey.valueOf(SessionAttachType.ISession_Key.toString());
			c.attr(k).set(this);

			this.channel = c;
		}
	}

	@Override
	public void write(Object msg) {
		channel.writeAndFlush(msg);
	}

	@Override
	public void send(Object msg) {
		SessionBox sessionBox = getAttach(SessionAttachType.SessionBox);
		sessionBox.outhandle(msg, this);
	}

	@Override
	public ConnectType getConnectType() {
		return ConnectType.Session;
	}

	@Override
	public String getSessionId() {
		return channel.id().asLongText();
	}

	@Override
	public void close() {
		channel.close();
	}

	@Override
	public <T> T getAttach(SessionAttachType key) {
		return (T) attach.get(key);
	}

	@Override
	public void setAttach(SessionAttachType key, Object value) {
		attach.put(key, value);
	}

	@Override
	public boolean isOpen() {
		return channel.isOpen();
	}
}
