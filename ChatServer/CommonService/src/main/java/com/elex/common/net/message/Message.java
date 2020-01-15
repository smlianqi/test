package com.elex.common.net.message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.elex.common.net.type.MegProtocolType;
import com.elex.common.net.type.MessageAttachType;

public class Message implements IMessage {
	private Object message;// 消息flat->ByteBuffer；proto->MessageLite消息对象
	private MegProtocolType megProtocolType;
	private ConcurrentMap<Object, Object> attach = new ConcurrentHashMap<>();

	public Message() {
	}

	public static IMessage createProtoMessage(Object message) {
		Message msg = new Message();
		msg.setMegProtocolType(MegProtocolType.proto);
		msg.setMessage(message);
		return msg;
	}

	public static IMessage createProtoMessage(Object message, long userId) {
		Message msg = new Message();
		msg.setMegProtocolType(MegProtocolType.proto);
		msg.setMessage(message);
		msg.setAttach(MessageAttachType.UserId, userId);
		return msg;
	}

	public static IMessage createFlatMessage(Object message, Class<?> messageClass) {
		Message msg = new Message();
		msg.setMegProtocolType(MegProtocolType.flat);
		msg.setMessage(message);
		msg.setAttach(MessageAttachType.MessageClass, messageClass);
		return msg;
	}

	public static IMessage createFlatMessage(Object message, Class<?> messageClass, long userId) {
		Message msg = new Message();
		msg.setMegProtocolType(MegProtocolType.flat);
		msg.setMessage(message);
		msg.setAttach(MessageAttachType.MessageClass, messageClass);
		msg.setAttach(MessageAttachType.UserId, userId);
		return msg;
	}

	public static IMessage createBytesMessage(Object message, int commandId) {
		Message msg = new Message();
		msg.setMegProtocolType(MegProtocolType.bytes);
		msg.setMessage(message);
		msg.setAttach(MessageAttachType.CommandId, commandId);
		return msg;
	}

	public static IMessage createBytesMessage(Object message, long userId, int commandId) {
		Message msg = new Message();
		msg.setMegProtocolType(MegProtocolType.bytes);
		msg.setMessage(message);
		msg.setAttach(MessageAttachType.UserId, userId);
		msg.setAttach(MessageAttachType.CommandId, commandId);
		return msg;
	}

	public <T1> T1 getMessage() {
		return (T1) message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	@Override
	public MegProtocolType getMegProtocolType() {
		return megProtocolType;
	}

	public void setMegProtocolType(MegProtocolType megProtocolType) {
		this.megProtocolType = megProtocolType;
	}

	@Override
	public <T2> T2 getAttach(MessageAttachType key) {
		return (T2) attach.get(key);
	}

	@Override
	public void setUserId(long userId) {
		this.setAttach(MessageAttachType.UserId, userId);
	}

	public void setAttach(MessageAttachType key, Object value) {
		attach.put(key, value);
	}
}
