package com.elex.common.net.message.protocol;

public interface ICommandMessage {
	byte[] toByteArray();

	// --------------------------------
	int getCommandId();

	byte[] getBodyByte();
}
