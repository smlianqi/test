package com.elex.common.net.message.protocol.flat;

import com.elex.common.net.message.protocol.ICommandMessage;
import com.elex.common.net.message.protocol.ICommandMessageFactory;

public class FlatCommandMessageFactory implements ICommandMessageFactory {
	public ICommandMessage createMessage(byte[] datas) {
		FlatCommandMessage commandMessage = new FlatCommandMessage(datas);
		return commandMessage;
	}

	public ICommandMessage createMessage(int commandId, byte[] content) {
		FlatCommandMessage commandMessage = new FlatCommandMessage(commandId, content);
		return commandMessage;
	}
}
