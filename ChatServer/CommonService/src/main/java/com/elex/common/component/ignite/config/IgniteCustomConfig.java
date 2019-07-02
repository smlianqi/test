package com.elex.common.component.ignite.config;

import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.type.ServiceType;

public class IgniteCustomConfig implements ICustomConfig {
	private IDataRegionConfig dataRegionConfig;

	@Override
	public ServiceType[] getServiceTypes() {
		return new ServiceType[] { ServiceType.ignite };
	}

	public IDataRegionConfig getDataRegionConfig() {
		return dataRegionConfig;
	}

	// ------------------------------
	public void setDataRegionConfig(IDataRegionConfig dataRegionConfig) {
		this.dataRegionConfig = dataRegionConfig;
	}

}
