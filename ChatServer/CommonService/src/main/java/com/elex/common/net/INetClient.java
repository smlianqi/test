package com.elex.common.net;

import com.elex.common.net.type.NetProtocolType;
import com.elex.common.net.type.NetServiceType;

/**
 * 连接
 * 
 * @author mausmars
 *
 */
public interface INetClient {
	/**
	 * netclient的id
	 * 
	 * @return
	 */
	int getId();

	void startup();

	void shutdown();

	/**
	 * 网络服务类型
	 * 
	 * @return
	 */
	NetServiceType getNetServiceType();

	/**
	 * 网络协议类型
	 * 
	 * @return
	 */
	NetProtocolType getNetProtocolType();
}
