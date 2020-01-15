package com.elex.common.component.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class CglibProxyFactory implements IProxyFactory {
	protected MethodInterceptor methodInterceptor;

	public CglibProxyFactory(MethodInterceptor methodInterceptor) {
		this.methodInterceptor = methodInterceptor;
	}

	public <T> T getInterface(Class<T> clazz) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(methodInterceptor);
		return (T) enhancer.create();
	}

	public <T> T getInterface(Class<T> clazz, Class<?>[] argClazz, Object[] args) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(methodInterceptor);
		return (T) enhancer.create(argClazz, args);
	}
}
