package com.elex.common.component.clsloader;

import java.util.concurrent.ConcurrentHashMap;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 类加载器
 * 
 * @author mausmars
 * 
 */
public class ServerClassLoader {
	protected static final ILogger logger = XLogUtil.logger();

	private static final ConcurrentHashMap<String, Class<?>> CLASSNAME_TO_CLASS = new ConcurrentHashMap<String, Class<?>>();

	private static ClassLoader classLoader;

	static {
		classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = ServerClassLoader.class.getClassLoader();
		}
	}

	public static synchronized Class<?> getClass(String className) {
		Class<?> c = CLASSNAME_TO_CLASS.get(className);
		try {
			if (c == null) {
				c = Class.forName(className, true, classLoader);
			}
			if (c == null) {
				logger.error("className:" + className);
				throw new RuntimeException();
			}
			// 避免下次再次查询
			CLASSNAME_TO_CLASS.putIfAbsent(className, c);
		} catch (ClassNotFoundException e) {
			logger.error("className:" + className, e);
			throw new RuntimeException(e);
		}
		return c;
	}
}
