package com.elex.common.service.exp;

import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceType;

/**
 * 服务异常
 * 
 * @author mausmars
 *
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	protected ServiceType serviceType;
	protected String serviceId;
	protected ServiceErrorType serviceErrorType;

	public ServiceException(ServiceType serviceType, String serviceId, ServiceErrorType serviceErrorType,
			Throwable cause) {
		super(cause);
		this.serviceType = serviceType;
		this.serviceId = serviceId;
		this.serviceErrorType = serviceErrorType;
	}

	public static ServiceException createException(IServiceConfig serviceConfig, ServiceErrorType serviceErrorType,
			Throwable cause) {
		return new ServiceException(serviceConfig.getServiceType(), serviceConfig.getServiceId(), serviceErrorType,
				cause);
	}

	public static ServiceException createException(ServiceType serviceType, String serviceId,
			ServiceErrorType serviceErrorType, Throwable cause) {
		return new ServiceException(serviceType, serviceId, serviceErrorType, cause);
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public ServiceErrorType getServiceErrorType() {
		return serviceErrorType;
	}

}
