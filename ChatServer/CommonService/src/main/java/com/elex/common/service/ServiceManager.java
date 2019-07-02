package com.elex.common.service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceType;

/**
 * 服务工厂
 * 
 * @author mausmars
 *
 */
public class ServiceManager implements IServiceManager {
	// {服务类型,{sid，service对象}}
	private ConcurrentMap<ServiceType, ConcurrentMap<String, IService>> serviceMap;

	public ServiceManager() {
		serviceMap = new ConcurrentHashMap<ServiceType, ConcurrentMap<String, IService>>();
	}

	public <T extends IService> T getService(ServiceType serviceType, String serviceId) {
		ConcurrentMap<String, IService> sm = serviceMap.get(serviceType);
		if (sm == null) {
			return null;
		}
		return (T) sm.get(serviceId);
	}

	@Override
	public <T extends IService> T getService(String serviceType, String serviceId) {
		return getService(ServiceType.valueOf(serviceType), serviceId);
	}

	public <T extends IService> List<T> getTypeService(ServiceType serviceType) {
		ConcurrentMap<String, IService> sm = serviceMap.get(serviceType);

		List<T> services = new LinkedList<T>();
		if (sm != null) {
			for (IService service : sm.values()) {
				services.add((T) service);
			}
		}
		return services;
	}

	@Override
	public <T extends IService> List<T> getTypeService(String serviceType) {
		return getTypeService(ServiceType.valueOf(serviceType));
	}

	@Override
	public boolean isContain(ServiceType serviceType, String serviceId) {
		ConcurrentMap<String, IService> sm = serviceMap.get(serviceType);
		if (sm == null) {
			return false;
		}
		return sm.containsKey(serviceId);
	}

	@Override
	public boolean isContain(String serviceType, String serviceId) {
		return isContain(ServiceType.valueOf(serviceType), serviceId);
	}

	@Override
	public void insertService(IService service) {
		IServiceConfig config = service.getConfig();

		ConcurrentMap<String, IService> sm = serviceMap.get(config.getServiceType());
		if (sm == null) {
			ConcurrentMap<String, IService> shm = new ConcurrentHashMap<String, IService>();
			sm = serviceMap.putIfAbsent(config.getServiceType(), shm);
			if (sm == null) {
				sm = shm;
			}
		}
		sm.putIfAbsent(config.getServiceId(), service);
	}
}
