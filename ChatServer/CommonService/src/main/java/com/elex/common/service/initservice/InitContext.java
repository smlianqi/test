package com.elex.common.service.initservice;

import java.util.HashMap;
import java.util.Map;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.factory.IServiceFactory;

/**
 * 初始化上下文
 * 
 * @author admin
 *
 */
public class InitContext {
	private IGlobalContext globalContext;

	private IInitCallback initCallback;
	// 服务工厂
	private IServiceFactory serviceFactory;
	// 附件
	private Map<Object, Object> storage;

	private FunctionType functionType;

	public InitContext() {
		this.storage = new HashMap<Object, Object>();
	}

	public <T> T getAttach(Object key) {
		return (T) storage.get(key);
	}

	public <T> T getAttachByCls(Class<T> key) {
		return (T) storage.get(key);
	}

	public void putAttach(Object key, Object attach) {
		storage.put(key, attach);
	}

	public void reset() {
		storage.clear();
	}

	public IServiceFactory getServiceFactory() {
		return serviceFactory;
	}

	public FunctionType getFunctionType() {
		return functionType;
	}

	public void setFunctionType(FunctionType functionType) {
		this.functionType = functionType;
	}

	public IGlobalContext getGlobalContext() {
		return globalContext;
	}

	public void setGlobalContext(IGlobalContext globalContext) {
		this.globalContext = globalContext;
	}

	public IInitCallback getInitCallback() {
		return initCallback;
	}

	public void setInitCallback(IInitCallback initCallback) {
		this.initCallback = initCallback;
	}

	public void setServiceFactory(IServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}
}
