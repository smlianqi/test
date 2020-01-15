package com.elex.common.net;

import com.elex.common.net.session.ISession;

/**
 * 连接回调接口
 * 
 * @author mausmars
 *
 */
public interface IConnectCallBack {
	void connectExcute(ISession session);
}
