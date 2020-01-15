package com.elex.common.net.rounter;

import com.elex.common.net.service.netty.filter.tcp.NettyMessage;
import com.elex.common.net.session.ISession;

public interface IForwardService {
	void forwardMsg(NettyMessage message, ISession session);
}
