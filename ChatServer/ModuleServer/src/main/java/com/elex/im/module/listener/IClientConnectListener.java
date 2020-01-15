package com.elex.im.module.listener;

import com.elex.common.net.event.ReconnectEvent;

public interface IClientConnectListener {
	void listen(ReconnectEvent event);
}
