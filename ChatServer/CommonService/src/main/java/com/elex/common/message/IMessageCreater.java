package com.elex.common.message;

import java.util.List;

import com.elex.common.net.message.IMessage;
import com.elex.common.net.type.MegProtocolType;

public interface IMessageCreater {
	MegProtocolType getMegProtocolType();

	IMessage createBindingSocketUpMessage(String serverId, String token, int index);

	IMessage createPingCheckUpMessage();

	IMessage createBroadcastUpMessage(BroadcastType type, int commandId, byte[] content, List<String> uids);
}
