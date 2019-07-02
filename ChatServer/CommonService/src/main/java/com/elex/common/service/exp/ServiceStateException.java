package com.elex.common.service.exp;

import com.elex.common.service.type.ServiceType;

/**
 * 服务状态异常
 * 
 * @author mausmars
 *
 */
public class ServiceStateException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	protected ServiceType serviceType;
	protected String serviceId;
	protected ServiceStateErrorType serviceStateErrorType;

	public ServiceStateException(ServiceType serviceType, String serviceId,
			ServiceStateErrorType serviceStateErrorType) {
		this.serviceType = serviceType;
		this.serviceId = serviceId;
		this.serviceStateErrorType = serviceStateErrorType;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public ServiceStateErrorType getServiceStateErrorType() {
		return serviceStateErrorType;
	}

}
