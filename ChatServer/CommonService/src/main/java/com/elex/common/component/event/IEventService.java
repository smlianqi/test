package com.elex.common.component.event;

import com.elex.common.service.IService;
import com.google.common.eventbus.Subscribe;

/**
 * 事件服务
 * 
 * @author mausmars
 *
 */
public interface IEventService extends IService {
	/**
	 * 通知
	 * 
	 * @param event
	 */
	void notify(Object event);

	/**
	 * 注册，异步处理
	 * 
	 * @param listener
	 */
	void registerAsync(Object listener);

	void unregisterAsync(Object listener);

	/**
	 * 注册监听器，监听器需要监听的方法加上 @Subscribe标签
	 * 
	 * @param listener
	 */
	void registerSync(Object listener);

	void unregisterSync(Object listener);
}
