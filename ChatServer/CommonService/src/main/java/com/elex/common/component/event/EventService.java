package com.elex.common.component.event;

import java.util.concurrent.Executors;

import com.elex.common.component.event.config.ScEvent;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

/**
 * 事件服务
 * 
 * @author mausmars
 *
 */
public class EventService extends AbstractService<ScEvent> implements IEventService {
	// 异步事件
	protected AsyncEventBus asyncEventBus;
	// 同步事件
	protected EventBus syncEventBus;

	public EventService(IServiceConfig serviceConfig, IGlobalContext globalContext) {
		super(serviceConfig, globalContext);
	}

	@Override
	public void initService() throws Exception {
		ScEvent c = getSConfig();

		asyncEventBus = new AsyncEventBus("AsyncEventBus", Executors.newFixedThreadPool(c.getThreadCount()));
		syncEventBus = new EventBus("SyncEventBus");
	}

	@Override
	public void startupService() throws Exception {

	}

	@Override
	public void shutdownService() throws Exception {

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
