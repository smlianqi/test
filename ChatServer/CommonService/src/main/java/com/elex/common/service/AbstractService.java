package com.elex.common.service;

import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.exp.ServiceErrorType;
import com.elex.common.service.exp.ServiceException;
import com.elex.common.service.exp.ServiceStateErrorType;
import com.elex.common.service.exp.ServiceStateException;
import com.elex.common.service.type.ServiceStateType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 抽象服务
 * 
 * @author mausmars
 *
 */
public abstract class AbstractService<T> implements IService {
	protected static final ILogger logger = XLogUtil.logger();

	protected IServiceConfig serviceConfig;
	protected IGlobalContext globalContext;
	protected ICustomConfig customConfig;

	protected volatile ServiceStateType state = ServiceStateType.Stopped;

	public AbstractService(IServiceConfig serviceConfig, IGlobalContext globalContext) {
		this.serviceConfig = serviceConfig;
		this.globalContext = globalContext;
	}

	@Override
	public <K extends IServiceConfig> K getConfig() {
		return (K) serviceConfig;
	}

	@Override
	public ServiceStateType getServiceStateType() {
		return state;
	}

	/**
	 * 获得数据层的配置对象
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T getSConfig() {
		return (T) serviceConfig.getConfig();
	}

	@Override
	public IFunctionServiceConfig getFunctionServiceConfig() {
		return serviceConfig.getFunctionServiceConfig();
	}

	public Object getAttach(Object key) {
		return serviceConfig.getAttach(key);
	}

	@Override
	public void init() {
		synchronized (this) {
			if (state != ServiceStateType.Stopped) {
				return;
			}
			state = ServiceStateType.Initing;
		}
		// 初始化服务
		try {
			initService();
		} catch (Exception e) {
			logger.error("", e);
			throw ServiceException.createException(serviceConfig, ServiceErrorType.ServiceInitError, e);
		}

		synchronized (this) {
			if (state != ServiceStateType.Initing) {
				return;
			}
			state = ServiceStateType.Inited;
		}

		if (logger.isInfoEnabled()) {
			logger.info("LocalService init! [type=" + serviceConfig.getServiceType() + ",id="
					+ serviceConfig.getServiceId() + "] state=" + state);
		}
	}

	@Override
	public void startup() {
		synchronized (this) {
			if (state != ServiceStateType.Inited) {
				return;
			}
			state = ServiceStateType.Starting;
		}
		// 启动服务
		try {
			startupService();
		} catch (Exception e) {
			logger.error("", e);
			throw ServiceException.createException(serviceConfig, ServiceErrorType.ServiceStartError, e);
		}

		synchronized (this) {
			if (state != ServiceStateType.Starting) {
				return;
			}
			state = ServiceStateType.Started;
		}
		if (logger.isInfoEnabled()) {
			logger.info("LocalService startup! [type=" + serviceConfig.getServiceType() + ",id="
					+ serviceConfig.getServiceId() + "] state=" + state);
		}
	}

	@Override
	public void shutdown() {
		synchronized (this) {
			if (state != ServiceStateType.Started) {
				return;
			}
			state = ServiceStateType.Stopping;
		}
		if (logger.isInfoEnabled()) {
			logger.info("LocalService shutdown! [type=" + serviceConfig.getServiceType() + ",id="
					+ serviceConfig.getServiceId() + "] state=" + state);
		}

		// 停止服务
		try {
			shutdownService();
		} catch (Exception e) {
			logger.error("", e);
			throw ServiceException.createException(serviceConfig, ServiceErrorType.ServiceShutdownError, e);
		}

		synchronized (this) {
			if (state != ServiceStateType.Stopping) {
				return;
			}
			state = ServiceStateType.Started;
		}
		if (logger.isInfoEnabled()) {
			logger.info("LocalService shutdown! [type=" + serviceConfig.getServiceType() + ",id="
					+ serviceConfig.getServiceId() + "] state=" + state);
		}
	}

	public IGlobalContext getGlobalContext() {
		return globalContext;
	}

	public IServiceManager getServiceManager() {
		return globalContext.getServiceManager();
	}

	// 初始化服务
	public abstract void initService() throws Exception;

	// 启动服务
	public abstract void startupService() throws Exception;

	// 停止服务
	public abstract void shutdownService() throws Exception;

	/**
	 * 检查服务使用状态
	 */
	protected void checkServiceUseState() {
		if (state != ServiceStateType.Started) {
			throw new ServiceStateException(serviceConfig.getServiceType(), serviceConfig.getServiceId(),
					ServiceStateErrorType.Disabled);
		}
	}
}
