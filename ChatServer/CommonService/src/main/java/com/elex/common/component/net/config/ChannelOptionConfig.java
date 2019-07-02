package com.elex.common.component.net.config;

import java.util.Properties;

import com.elex.common.service.configloader.ServiceFileConfig;

public class ChannelOptionConfig {
	private ChannelOption channelOption = new ChannelOption();

	public ChannelOptionConfig(ServiceFileConfig serviceFileConfig) {
		init(serviceFileConfig);
	}

	private void init(ServiceFileConfig serviceFileConfig) {
		Properties pp = serviceFileConfig.getProperties();

		channelOption.setId(pp.getProperty("id"));
		channelOption.setExtraParams(pp.getProperty("extra_params"));
		// sc.setReadme(p.getProperty("readme"));

		channelOption.setBacklog(Integer.parseInt(pp.getProperty("backlog")));
		channelOption.setKeepalive(Boolean.parseBoolean(pp.getProperty("keepalive")));
		channelOption.setNodelay(Boolean.parseBoolean(pp.getProperty("nodelay")));
		channelOption.setReuseaddr(Boolean.parseBoolean(pp.getProperty("reuseaddr")));
		channelOption.setLinger(Integer.parseInt(pp.getProperty("linger")));
		channelOption.setRcvbuf(Integer.parseInt(pp.getProperty("rcvbuf")));
		channelOption.setSndbuf(Integer.parseInt(pp.getProperty("sndbuf")));

		String timeout = pp.getProperty("timeout");
		if (timeout != null) {
			channelOption.setTimeout(Integer.parseInt(timeout));
		}
		channelOption.setConnectTimeout(Integer.parseInt(pp.getProperty("connect_timeout")));
		channelOption.obtainAfter();
	}

	public ChannelOption getChannelOption() {
		return channelOption;
	}
}
