package com.elex.common.net.handler;

import com.elex.common.net.session.ISession;

/**
 * 处理消息接口
 * 
 * @author mausmars
 *
 * @param <T>
 */

public interface IMessageOutHandler<T> {
	/**
	 * 逻辑处理
	 * 
	 * @param message
	 * @param session
	 */
	void outhandle(T message, ISession session);
}
