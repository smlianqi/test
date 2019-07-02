package com.elex.common.net.message.protocol.proto;

import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.net.message.protocol.ICommandMessageFactory;

public class ProtoCommandMessageFactory implements ICommandMessageFactory {
	public ICommandMessage createMessage(byte[] datas) {
		ProtoCommandMessage commandMessage = new ProtoCommandMessage(datas);
		return commandMessage;
	}

	public ICommandMessage createMessage(int commandId, byte[] content) {
		ProtoCommandMessage commandMessage = new ProtoCommandMessage();

		commandMessage.setCommandId(commandId);
		commandMessage.setBodyByte(content);

		return commandMessage;
	}
}
