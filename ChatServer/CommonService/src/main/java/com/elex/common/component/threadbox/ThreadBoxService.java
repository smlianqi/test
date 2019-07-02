package com.elex.common.component.threadbox;

import java.util.HashMap;
import java.util.Map;

import com.elex.common.component.threadbox.config.ScThreadbox;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;

/**
 * 线程工具箱服务
 * 
 * @author mausmars
 *
 */
public class ThreadBoxService extends AbstractService<ScThreadbox> implements IThreadBoxService {
	private ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<Map<Object, Object>>();

	public ThreadBoxService(IServiceConfig serviceConfig, IGlobalContext context) {
		super(serviceConfig, context);
	}

	@Override
	public void initService() throws Exception {
	}

	@Override
	public void startupService() throws Exception {
	}

	@Override
	public void shutdownService() throws Exception {
		threadLocal = null;
	}

	public void remove() {
		threadLocal.remove();
	}

	public <T> T fetch(Object key) {
		Map<Object, Object> box = threadLocal.get();
		if (box == null) {
			return null;
		}
		return (T) box.get(key);
	}

	public void store(Object key, Object value) {
		Map<Object, Object> box = threadLocal.get();
		if (box == null) {
			box = new HashMap<Object, Object>();
			threadLocal.set(box);
		}
		box.put(key, value);
	}
}
