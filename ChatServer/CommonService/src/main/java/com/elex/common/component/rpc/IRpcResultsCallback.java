package com.elex.common.component.rpc;

/**
 * 结果回调
 * 
 * @author mausmars
 *
 */
public interface IRpcResultsCallback<T> {
	/**
	 * 回复
	 * 
	 * @param arg
	 */
	void response(T arg);

	/**
	 * 异常
	 * 
	 * @param ex
	 */
	void exception(Throwable ex);
}