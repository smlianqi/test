package com.elex.common.component.rpc;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.service.IService;

/**
 * rpc代理服务器接口 （封装底层客户端连接）
 * 
 * @author mausmars
 *
 */
public interface IRpcServicePrx extends IService {
	/**
	 * 初始化rpc服务代理
	 * 
	 * @param functionInfo
	 */
	IRpcFServicePrx createServicePrx(String objectId, String netProtocol, String host, int port, String packageName,
			String className, FunctionType functionType);
}
