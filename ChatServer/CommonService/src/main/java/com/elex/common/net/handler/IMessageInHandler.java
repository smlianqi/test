package com.elex.common.net.handler;

import com.elex.common.net.session.ISession;

/**
 * 处理消息接口
 * 
 * @author mausmars
 *
 * @param <T>
 */

public interface IMessageInHandler<T> {
	/**
	 * 逻辑处理
	 * 
	 * @param message
	 * @param session
	 */
	void inhandle(T message, ISession session, Object attr);
}
