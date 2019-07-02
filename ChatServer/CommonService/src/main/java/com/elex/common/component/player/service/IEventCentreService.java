package com.elex.common.component.player.service;

public interface IEventCentreService extends IPlayerMService {
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

	void registerSync(Object listener);

	void unregisterSync(Object listener);
}
