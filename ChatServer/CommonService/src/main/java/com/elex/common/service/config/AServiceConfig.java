package com.elex.common.service.config;

import java.util.Map;
import java.util.Properties;

import com.elex.common.service.configloader.ServiceFileConfig;

public abstract class AServiceConfig implements IServiceConfig {
	protected IFunctionServiceConfig functionServiceConfig;
	protected ServiceFileConfig serviceFileConfig;

	public AServiceConfig(ServiceFileConfig serviceFileConfig, IFunctionServiceConfig functionServiceConfig) {
		this.functionServiceConfig = functionServiceConfig;
		this.serviceFileConfig = serviceFileConfig;
	}

	@Override
	public Object getAttach(Object key) {
		return null;
	}

	@Override
	public IFunctionServiceConfig getFunctionServiceConfig() {
		return functionServiceConfig;
	}

	protected String getProperty(String key) {
		Properties p = serviceFileConfig.getProperties();

		Map<String, Map<String, String>> propertiesMap = functionServiceConfig.getPropertiesMap();
		if (propertiesMap == null) {
			return p.getProperty(key);
		}
		Map<String, String> pMap = propertiesMap.get(serviceFileConfig.getFileName());
		if (pMap == null) {
			return p.getProperty(key);
		}
		String v = pMap.get(key);
		if (v == null) {
			return p.getProperty(key);
		}
		return v;
	}
}
