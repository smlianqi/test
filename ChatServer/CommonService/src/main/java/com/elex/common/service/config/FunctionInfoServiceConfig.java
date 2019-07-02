package com.elex.common.service.config;

import java.util.Map;
import java.util.Properties;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.service.configloader.ServiceFileConfig;

public class FunctionInfoServiceConfig implements IFunctionServiceConfig {
	private ProvidingFunction providingFunction;

	private ConfigFunctioninfo cf = new ConfigFunctioninfo();

	public FunctionInfoServiceConfig(ServiceFileConfig serviceFileConfig, ProvidingFunction providingFunction) {
		this.providingFunction = providingFunction;
		init(serviceFileConfig);
	}

	private void init(ServiceFileConfig serviceFileConfig) {
		Properties p = serviceFileConfig.getProperties();

		cf = new ConfigFunctioninfo();

		if (providingFunction.getGroupId() == null) {
			String groupId = p.getProperty("group_id");
			providingFunction.setGroupId(groupId);
			cf.setGroupId(groupId);
		} else {
			cf.setGroupId(providingFunction.getGroupId());
		}
		if (providingFunction.getRegionId() == null) {
			String regionId = p.getProperty("region_id");
			providingFunction.setRegionId(regionId);
			cf.setRegionId(regionId);
		} else {
			cf.setRegionId(providingFunction.getRegionId());
		}
		cf.setFunctionType(providingFunction.getFunctionType().name());

		cf.setDependIds(p.getProperty("depend_ids"));
		cf.setExtraParams(p.getProperty("extra_params"));
		// cf.setReadme(p.getProperty("readme"));
		cf.obtainAfter();
	}

	@Override
	public String getGroupId() {
		return providingFunction.getGroupId();
	}

	@Override
	public String getRegionId() {
		return providingFunction.getRegionId();
	}

	@Override
	public FunctionType getFunctionType() {
		return providingFunction.getFunctionType();
	}

	@Override
	public <T> T getConfig() {
		return (T) cf;
	}

	@Override
	public Map<String, Map<String, String>> getPropertiesMap() {
		return providingFunction.getPropertiesMap();
	}
}
