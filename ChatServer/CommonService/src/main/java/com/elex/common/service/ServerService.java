package com.elex.common.service;

import java.util.List;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.service.configloader.IServiceConfigLoader;
import com.elex.common.service.factory.ServiceFactory;
import com.elex.common.service.filter.DefaultFilterChain;
import com.elex.common.service.filter.IFilterChain;
import com.elex.common.service.initservice.IInitCallback;
import com.elex.common.service.initservice.InitContext;
import com.elex.common.service.module.IModuleServiceConfig;
import com.elex.common.service.module.IModuleServiceMgr;
import com.elex.common.service.module.ModuleServiceMgr;
import com.elex.common.service.type.ServiceType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.common.util.uuid.UniqueIdGenerator;

/**
 * 服务器服务
 * 
 * @author mausmars
 *
 */
public class ServerService implements IGlobalContext {
	protected static final ILogger logger = XLogUtil.logger();

	protected long startTime;
	protected long serverId;
	// 初始化链
	protected IFilterChain initFilterChain;

	// TODO 可以启动多个服，这里工厂，管理器需要单独包装
	// 服务管理器
	protected ServiceManager sm;
	// 模块服务管理
	protected ModuleServiceMgr msm;
	// 服务工厂
	protected ServiceFactory sf;

	protected Object lock = new Object();
	// ------------------------
	// 配置加载
	protected IServiceConfigLoader serviceConfigLoader;
	// ------------------------
	protected IModuleServiceConfig moduleServiceConfig;

	// 功能类型
	protected FunctionType functionType;
	// 初始化回调
	private IInitCallback initCallback;

	private UniqueIdGenerator uniqueIdGenerator;

	public ServerService(FunctionType functionType) {
		this.functionType = functionType;
		this.msm = new ModuleServiceMgr();
	}

	public ServerService(FunctionType functionType, ServiceManager sm) {
		this.functionType = functionType;
		this.sm = sm;
		this.msm = new ModuleServiceMgr();
	}

	private void init() {
		if (this.sm == null) {
			this.sm = new ServiceManager();
		}
		this.uniqueIdGenerator = new UniqueIdGenerator(GeneralConstant.OnlineTime);
		this.serverId = uniqueIdGenerator.nextID();
		this.sf = new ServiceFactory(this, serviceConfigLoader);
	}

	public void startup(boolean isBlock) {
		// 初始化
		init();
		try {
			startTime = System.currentTimeMillis();

			if (logger.isInfoEnabled()) {
				logger.info("### ServerService start! ### ");
			}
			InitContext initContext = new InitContext();
			initContext.setGlobalContext(this);
			initContext.setServiceFactory(sf);
			initContext.setFunctionType(functionType);
			initContext.setInitCallback(initCallback);
			initContext.putAttach(IModuleServiceConfig.class, moduleServiceConfig);

			// 默认初始化
			defInit();
			initFilterChain.doFilter(null, initContext);

			initAfter();
			if (logger.isInfoEnabled()) {
				logger.info("[ServerId=" + serverId + "] ### ServerService start finished! ###");
			}
			if (isBlock) {
				// 阻塞
				startBlock();
			}
		} catch (Exception e) {
			logger.error("", e);
			// 初始化失败就退出程序
//			System.exit(0);
		}
	}

	// 如果子类有其他的操作在这里复写
	void initAfter() {
	}

	private void startBlock() throws InterruptedException {
		synchronized (lock) {
			lock.wait();
		}
	}

	public void shutdown() {
		try {
			// TODO 关闭服务
			List<IService> services = sm.getTypeService(ServiceType.function);
			// 停止服务
			for (IService service : services) {
				try {
					// 关闭服务
					service.shutdown();
				} catch (Exception e) {
					logger.error("", e);
				}
			}
			synchronized (lock) {
				lock.notifyAll();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public long getStartTime() {
		return startTime;
	}

	@Override
	public ServiceManager getServiceManager() {
		return sm;
	}

	@Override
	public IModuleServiceMgr getModuleServiceMgr() {
		return msm;
	}

	private void defInit() {
		// if (logger.isInfoEnabled()) {
		// logger.info("ServerService defInit!");
		// }
		if (initFilterChain == null) {
			initFilterChain = new DefaultFilterChain("server_initchain");
		}
	}

	@Override
	public long getServerId() {
		return serverId;
	}

	@Override
	public long createUniqueId() {
		return uniqueIdGenerator.nextID();
	}

	// --------------------------------------------
	public void setInitFilterChain(IFilterChain initFilterChain) {
		this.initFilterChain = initFilterChain;
	}

	public void setServiceConfigLoader(IServiceConfigLoader serviceConfigLoader) {
		this.serviceConfigLoader = serviceConfigLoader;
	}

	public void setModuleServiceConfig(IModuleServiceConfig moduleServiceConfig) {
		this.moduleServiceConfig = moduleServiceConfig;
	}

	public void setInitCallback(IInitCallback initCallback) {
		this.initCallback = initCallback;
	}
}
