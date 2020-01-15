package com.elex.common.component.net.server;

import com.elex.common.component.function.info.FunctionInfo;
import com.elex.common.service.IService;

/**
 * 网络服务端服务
 * 
 * @author mausmars
 *
 */
public interface INetServerService extends IService {
	/**
	 * 注册服务
	 * 
	 * @param functionInfo
	 */
	void registerService(FunctionInfo functionInfo);
}
