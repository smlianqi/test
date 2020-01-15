package com.elex.common.service.configloader;

import java.util.Properties;

public class ServiceFileConfig {
	private String fileName;// 文件名，不带后缀
	private Properties properties;// 文件里的属性

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
