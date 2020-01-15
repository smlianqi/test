package com.elex.common.net.rounter;

import com.elex.common.component.function.type.FunctionType;

/**
 * 消息路由
 * 
 * @author mausmars
 *
 */
public interface IMessageRounter {
	/**
	 * 对应的转发功能类型
	 * 
	 * @param commandId
	 * @return
	 */
	FunctionType forward(String commandId);
}
