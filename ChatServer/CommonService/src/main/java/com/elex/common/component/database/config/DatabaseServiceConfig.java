package com.elex.common.component.database.config;

import java.util.List;
import java.util.Map;

import com.elex.common.service.config.AServiceConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.configloader.ServiceFileConfig;
import com.elex.common.service.type.ServiceType;

public class DatabaseServiceConfig extends AServiceConfig {
	private ScDatabase sc = new ScDatabase();

	public DatabaseServiceConfig(ServiceFileConfig serviceFileConfig, IFunctionServiceConfig functionServiceConfig) {
		super(serviceFileConfig, functionServiceConfig);
		init();
	}

	private void init() {
		sc.setId(getProperty("id"));
		sc.setDependIds(getProperty("depend_ids"));
		sc.setExtraParams(getProperty("extra_params"));
		// sc.setReadme(getProperty("readme"));

		sc.setDriverClass(getProperty("driver_class"));
		sc.setJdbcUrl(getProperty("jdbc_url"));
		sc.setUser(getProperty("user"));
		sc.setPassword(getProperty("password"));
		sc.setMaxPoolSize(Integer.parseInt(getProperty("max_pool_size")));
		sc.setMinPoolSize(Integer.parseInt(getProperty("min_pool_size")));
		sc.setInitialPoolSize(Integer.parseInt(getProperty("initial_pool_size")));
		sc.setMaxIdleTime(Integer.parseInt(getProperty("max_idle_time")));
		sc.setAcquireIncrement(Integer.parseInt(getProperty("acquire_increment")));
		sc.setMaxStateMents(Integer.parseInt(getProperty("max_state_ments")));
		sc.setBreakAfterAcquireFailure(Boolean.parseBoolean(getProperty("break_after_acquire_failure")));
		sc.setAutoCommitOnClose(Boolean.parseBoolean(getProperty("auto_commit_on_close")));

		sc.setIdleConnectionTestPeriod(Integer.parseInt(getProperty("idle_connection_test_period")));
		sc.setPreferredTestQuery(getProperty("preferred_test_query"));

		sc.setAcquireRetryAttempts(Integer.parseInt(getProperty("acquire_retry_attempts")));
		sc.setAcquireRetryDelay(Integer.parseInt(getProperty("acquire_retry_delay")));

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
		return ServiceType.database;
	}

	@Override
	public Map<String, List<String>> getDependIdsMap() {
		return sc.getDependIdsMap();
	}
}
