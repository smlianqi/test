package com.elex.common.net.message;

import com.elex.common.net.type.MegProtocolType;
import com.elex.common.net.type.MessageAttachType;

public interface IMessage {
	MegProtocolType getMegProtocolType();

	<T1> T1 getMessage();

	void setUserId(long userId);

	<T2> T2 getAttach(MessageAttachType key);
}
