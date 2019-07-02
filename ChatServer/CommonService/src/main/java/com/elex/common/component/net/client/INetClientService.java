package com.elex.common.component.net.client;

import com.elex.common.net.INetClient;
import com.elex.common.net.session.ISession;
import com.elex.common.service.IService;

/**
 * 网络客户端服务
 * 
 * @author mausmars
 *
 */
public interface INetClientService extends IService {
	/**
	 * 获取网络客户端
	 * 
	 * @return
	 */
	<T extends INetClient> T createNetClient();

	<T extends INetClient> T createNetClient(String host, int port);

	void removeNetClient(int clientId);

	void removeNetClient(ISession session);

}
