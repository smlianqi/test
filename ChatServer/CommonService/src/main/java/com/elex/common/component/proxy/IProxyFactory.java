package com.elex.common.component.proxy;

/**
 * 代理创建工厂接口
 * 
 * @author mausmars
 *
 */
public interface IProxyFactory {
	<T> T getInterface(Class<T> clazz);

	<T> T getInterface(Class<T> clazz, Class<?>[] argClazz, Object[] args);
}
