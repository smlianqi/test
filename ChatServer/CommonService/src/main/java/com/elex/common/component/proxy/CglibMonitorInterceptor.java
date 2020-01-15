package com.elex.common.component.proxy;

import java.lang.reflect.Method;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 监控 拦截器
 * 
 * @author mausmars
 *
 */
public class CglibMonitorInterceptor implements MethodInterceptor {
	protected static final ILogger logger = XLogUtil.logger();

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		long startTime = System.currentTimeMillis();

		Object object = proxy.invokeSuper(obj, args);
		// Object object = proxy.invoke(obj, args);
		// Object object = method.invoke(obj, args);

		if (logger.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("时间监控 ");
			sb.append(System.currentTimeMillis() - startTime);
			sb.append("ms ");
			sb.append(obj.getClass().getSuperclass().getSimpleName());
			sb.append(" ");
			sb.append(method.getName());
			sb.append(" arg=");
			for (Object arg : args) {
				sb.append(arg);
				sb.append(" ");
			}
			logger.debug(sb.toString());
		}
		return object;
	}
}
