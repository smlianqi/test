package com.elex.common.component.net;

import com.elex.common.component.timeout.ITimeoutManager;
import com.elex.common.net.handler.ICreateSessionHandler;

/**
 * session 创建处理器
 * 
 * @author mausmars
 *
 */
public interface INetCreateSessionHandler extends ICreateSessionHandler {
	/**
	 * 初始化
	 * 
	 * @param timeoutManager
	 */
	void init(ITimeoutManager timeoutManager);
}
