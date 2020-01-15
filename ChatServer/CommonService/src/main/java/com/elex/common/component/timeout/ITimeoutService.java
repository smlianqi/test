package com.elex.common.component.timeout;

import com.elex.common.service.IService;

/**
 * 超时服务
 * 
 * @author mausmars
 *
 */
public interface ITimeoutService extends IService {
	/**
	 * 创建默认配置任务
	 * 
	 * @param id
	 * @param cornExpression
	 * @return
	 */
	ITimeoutManager createTask(String key);

	/**
	 * 创建自定义任务
	 * 
	 * @param id
	 * @param cornExpression
	 * @return
	 */
	ITimeoutManager createTask(String key, String expression);

	/**
	 * 移除任务
	 * 
	 * @param id
	 * @param cornExpression
	 * @return
	 */
	void removeTask(String key);
}
