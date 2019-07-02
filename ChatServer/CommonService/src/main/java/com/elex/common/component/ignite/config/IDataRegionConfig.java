package com.elex.common.component.ignite.config;

import org.apache.ignite.configuration.DataRegionConfiguration;

public interface IDataRegionConfig {
	DataRegionConfiguration[] createDataRegionConfigurations(ScIgnite c);
}
