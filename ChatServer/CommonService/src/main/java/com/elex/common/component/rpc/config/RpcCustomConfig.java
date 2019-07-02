package com.elex.common.component.rpc.config;

import java.util.HashMap;
import java.util.Map;

import com.elex.common.component.rpc.IRpcFServicePrx;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.type.ServiceType;

import Ice.ObjectFactory;

public class RpcCustomConfig implements ICustomConfig {
	// ice对象工厂
	private Map<String, ObjectFactory> objectFactoryMap;
	// {对象id:Ice服务}
	private Map<String, IRpcFServicePrx> map = new HashMap<String, IRpcFServicePrx>();

	@Override
	public ServiceType[] getServiceTypes() {
		return new ServiceType[] { ServiceType.rpc };
	}

	public Map<String, ObjectFactory> getObjectFactoryMap() {
		return objectFactoryMap;
	}

	public IRpcFServicePrx getLocalService(String objectId) {
		return map.get(objectId);
	}

	/** {对象id:本地服务}，这个要和配置对应上 */
	public void putLocalService(String objId, IRpcFServicePrx localService) {
		map.put(objId, localService);
	}

	// ------------------------------
	public void setObjectFactoryMap(Map<String, ObjectFactory> objectFactoryMap) {
		this.objectFactoryMap = objectFactoryMap;
	}
}
