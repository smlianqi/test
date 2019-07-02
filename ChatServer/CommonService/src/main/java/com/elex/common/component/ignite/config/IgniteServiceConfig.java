package com.elex.common.component.ignite.config;

import java.util.List;
import java.util.Map;

import com.elex.common.service.config.AServiceConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.configloader.ServiceFileConfig;
import com.elex.common.service.type.ServiceType;

public class IgniteServiceConfig extends AServiceConfig {
	private ScIgnite sc = new ScIgnite();

	public IgniteServiceConfig(ServiceFileConfig serviceFileConfig, IFunctionServiceConfig functionServiceConfig) {
		super(serviceFileConfig, functionServiceConfig);
		init();
	}

	private void init() {
		sc.setId(getProperty("id"));
		sc.setDependIds(getProperty("depend_ids"));
		sc.setExtraParams(getProperty("extra_params"));
		// sc.setReadme(getProperty("readme"));

		sc.setHost(getProperty("host"));
		sc.setPort(Integer.parseInt(getProperty("port")));
		sc.setDiscoveryType(getProperty("discovery_type"));
		sc.setWorkDirectory(getProperty("work_directory"));
		sc.setSwapDirectory(getProperty("swap_directory"));
		sc.setIsClient(Boolean.parseBoolean(getProperty("is_clien")));
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
		return ServiceType.ignite;
	}

	@Override
	public Map<String, List<String>> getDependIdsMap() {
		return sc.getDependIdsMap();
	}
}
