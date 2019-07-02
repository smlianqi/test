package com.elex.common.component.rpc;

/**
 * 功能服务接口（提供具体的业务接口，基类） （具体服务）
 * 
 * @author mausmars
 *
 */
public interface IRpcFService {
	IRpcFServicePrx getRpcServicePrx();
}
