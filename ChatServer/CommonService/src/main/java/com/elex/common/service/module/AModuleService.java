package com.elex.common.service.module;

import com.elex.common.service.IGlobalContext;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public abstract class AModuleService implements IModuleService {
	protected static final ILogger logger = XLogUtil.logger();

	protected IGlobalContext context;

	public AModuleService(IGlobalContext context) {
		this.context = context;
	}

	@Override
	public void init() {
	}

	public <T extends IModuleService> T getModuleService(ModuleServiceType moduleServiceType) {
		return context.getModuleServiceMgr().getService(moduleServiceType);
	}
}
