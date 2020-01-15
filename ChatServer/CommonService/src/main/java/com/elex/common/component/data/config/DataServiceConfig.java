package com.elex.common.component.data.config;

import java.util.List;
import java.util.Map;

import com.elex.common.service.config.AServiceConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.configloader.ServiceFileConfig;
import com.elex.common.service.type.ServiceType;

public class DataServiceConfig extends AServiceConfig {
	private ScData sc = new ScData();

	public DataServiceConfig(ServiceFileConfig serviceFileConfig, IFunctionServiceConfig functionServiceConfig) {
		super(serviceFileConfig, functionServiceConfig);
		init();
	}

	private void init() {
		sc.setId(getProperty("id"));
		sc.setDependIds(getProperty("depend_ids"));
		sc.setExtraParams(getProperty("extra_params"));
		// sc.setReadme(getProperty("readme"));
		sc.setTableConfigs(getProperty("table_configs"));
		sc.obtainAfter();
	}

	@Override
	public <T> T getConfig() {
		return (T) sc;
	}

	@Override
	public String getServiceId() {
		return sc.getId();
	}

	@Override
	public ServiceType getServiceType() {
		return ServiceType.data;
	}

	@Override
	public Map<String, List<String>> getDependIdsMap() {
		return sc.getDependIdsMap();
	}
}
