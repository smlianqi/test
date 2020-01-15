package com.elex.common.net.handler;

import com.elex.common.net.session.ISession;

/**
 * 断开连接处理
 * 
 * @author mausmars
 *
 */
public interface IClosedSessionHandler {
	/**
	 * 断开连接处理
	 * 
	 * @param session
	 */
	void execute(ISession session);
}
