package com.elex.common.component.net.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elex.common.service.GeneralConstant;
import com.elex.common.service.config.AServiceConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.configloader.ServiceFileConfig;
import com.elex.common.service.type.ServiceType;

public class NetServerServiceConfig extends AServiceConfig {
	private ScNetserver sc = new ScNetserver();
	private Map<String, ChannelOption> channelOptionMap = new HashMap<String, ChannelOption>();

	public NetServerServiceConfig(ServiceFileConfig serviceFileConfig, IFunctionServiceConfig functionServiceConfig) {
		super(serviceFileConfig, functionServiceConfig);
		init();
	}

	private void init() {
		sc.setId(getProperty("id"));
		sc.setDependIds(getProperty("depend_ids"));
		sc.setExtraParams(getProperty("extra_params"));
		// sc.setReadme(getProperty("readme"));
		sc.setNetServiceType(getProperty("net_service_type"));
		sc.setNetProtocolType(getProperty("net_protocol_type"));
		sc.setHost(getProperty("host"));
		sc.setPort(Integer.parseInt(getProperty("port")));
		sc.setHandlerThread(Integer.parseInt(getProperty("handler_thread")));
		sc.setSubReactorThread(Integer.parseInt(getProperty("sub_reactor_thread")));
		sc.setAllIdleTimeSeconds(Integer.parseInt(getProperty("all_idle_time_seconds")));
		sc.setWriterIdleTimeSeconds(Integer.parseInt(getProperty("reader_idle_time_seconds")));
		sc.setReaderIdleTimeSeconds(Integer.parseInt(getProperty("writer_idle_time_seconds")));
		sc.setOutsideHost(getProperty("outside_host"));
		sc.setMegProtocolType(getProperty("meg_protocol_type"));

		sc.setIsRegister(Integer.parseInt(getProperty("is_register")) == 1);
		sc.setConnectCount(Integer.parseInt(getProperty("connect_count")));
		sc.setCheckToken(getProperty("check_token"));
		sc.obtainAfter();
	}

	public String getExtraParamsMap() {
		return sc.getExtraParamsMap().get(GeneralConstant.ChannelOptionKey);
	}

	public void putChannelOption(ChannelOption channelOption) {
		channelOptionMap.put(GeneralConstant.ChannelOptionKey, channelOption);
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
		return ServiceType.netserver;
	}

	@Override
	public Object getAttach(Object key) {
		if (GeneralConstant.ChannelOptionKey.equals(key)) {
			return channelOptionMap.get(key);
		}
		return null;
	}

	@Override
	public Map<String, List<String>> getDependIdsMap() {
		return sc.getDependIdsMap();
	}
}
