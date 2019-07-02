package com.elex.common.component.player.service;

import java.util.concurrent.Executors;

import com.elex.common.component.player.type.PlayerMServiceType;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

/**
 * 事件中心管理
 * 
 * @author mausmars
 *
 */
public class DefaultEventCentreService implements IEventCentreService {
	// 异步事件
	protected AsyncEventBus asyncEventBus;
	// 同步事件
	protected EventBus syncEventBus;

	public DefaultEventCentreService() {
		this(1);
	}

	public DefaultEventCentreService(int threadCount) {
		asyncEventBus = new AsyncEventBus("AsyncEventBus", Executors.newFixedThreadPool(threadCount));
		syncEventBus = new EventBus("SyncEventBus");
	}

	@Override
	public PlayerMServiceType getType() {
		return PlayerMServiceType.Event;
	}

	public void registerAsync(Object listener) {
		asyncEventBus.register(listener);
	}

	public void unregisterAsync(Object listener) {
		asyncEventBus.unregister(listener);
	}

	public void registerSync(Object listener) {
		syncEventBus.register(listener);
	}

	public void unregisterSync(Object listener) {
		syncEventBus.register(listener);
	}

	public void notify(Object event) {
		// 先处理异步
		asyncEventBus.post(event);
		// 先处理同步
		syncEventBus.post(event);

	}
}
