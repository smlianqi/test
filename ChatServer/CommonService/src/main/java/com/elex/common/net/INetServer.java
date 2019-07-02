package com.elex.common.net;

import com.elex.common.net.type.NetServiceType;

/**
 * 服务接口
 * 
 * @author mausmars
 * 
 *
 */
public interface INetServer {

	/**
	 * 启动服务
	 * 
	 */
	void startup();

	/**
	 * 关闭服务
	 * 
	 */
	void shutdown();

	String getIp();

	int getPort();

	/**
	 * 网络服务类型
	 * 
	 * @return
	 */
	NetServiceType getNetServiceType();

	void setConfig(Object config);
}
