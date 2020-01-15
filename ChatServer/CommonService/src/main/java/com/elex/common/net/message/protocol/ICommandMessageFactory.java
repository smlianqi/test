package com.elex.common.net.message.protocol;

public interface ICommandMessageFactory {
	ICommandMessage createMessage(byte[] datas);

	ICommandMessage createMessage(int commandId, byte[] content);
}
