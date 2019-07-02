package com.elex.common.net.handler;

import com.elex.common.net.session.ISession;

/**
 * 建立连接处理
 * 
 * @author mausmars
 *
 */
public interface ICreateSessionHandler {

	/**
	 * 建立连接处理
	 * 
	 * @param session
	 */
	void execute(ISession session);
}
