package com.elex.common.component.zookeeper.config;

import java.util.List;
import java.util.Map;

import com.elex.common.service.config.AServiceConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.configloader.ServiceFileConfig;
import com.elex.common.service.type.ServiceType;

public class ZookeeperServiceConfig extends AServiceConfig {
	private ScZookeeper sc = new ScZookeeper();

	public ZookeeperServiceConfig(ServiceFileConfig serviceFileConfig, IFunctionServiceConfig functionServiceConfig) {
		super(serviceFileConfig, functionServiceConfig);
		init();
	}

	private void init() {
		sc.setId(getProperty("id"));
		sc.setDependIds(getProperty("depend_ids"));
		sc.setExtraParams(getProperty("extra_params"));
		// sc.setReadme(getProperty("readme"));

		sc.setHosts(getProperty("hosts"));
		sc.setTimeout(Integer.parseInt(getProperty("timeout")));
		sc.setPermissions(getProperty("permissions"));
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
		return ServiceType.zookeeper;
	}

	@Override
	public Map<String, List<String>> getDependIdsMap() {
		return sc.getDependIdsMap();
	}
}
