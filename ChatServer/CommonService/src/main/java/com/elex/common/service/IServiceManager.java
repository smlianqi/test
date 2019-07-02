package com.elex.common.service;

import java.util.List;

import com.elex.common.service.type.ServiceType;

/**
 * 服务管理
 * 
 * @author mausmars
 *
 */
public interface IServiceManager {
	/**
	 * 获取指定服务
	 * 
	 * @param serviceType
	 * @param serviceId
	 * @return
	 */
	<T extends IService> T getService(ServiceType serviceType, String serviceId);

	<T extends IService> T getService(String serviceType, String serviceId);

	/**
	 * 获取类型服务
	 * 
	 * @param serviceType
	 * @return
	 */
	<T extends IService> List<T> getTypeService(ServiceType serviceType);

	<T extends IService> List<T> getTypeService(String serviceType);

	/**
	 * 是否包含服务
	 * 
	 * @param serviceType
	 * @param serviceId
	 * @return
	 */
	boolean isContain(ServiceType serviceType, String serviceId);

	boolean isContain(String serviceType, String serviceId);

	/**
	 * 插入服务
	 * 
	 * @param service
	 */
	void insertService(IService service);
}
