package com.elex.common.component.function;

import com.elex.common.component.event.IEventService;
import com.elex.common.component.function.filter.NodeFilter;
import com.elex.common.component.member.IMemberService;
import com.elex.common.component.net.client.INetClientService;
import com.elex.common.component.rpc.IRpcServicePrx;
import com.elex.common.component.zookeeper.IZookeeperService;
import com.elex.common.service.IGlobalContext;

/**
 * 功能上下文
 * 
 * @author mausmars
 * 
 */
public interface IFunctionContext {
	String getCurrentServerFid();

	/**
	 * 节点过滤工具
	 * 
	 * @return
	 */
	NodeFilter getNodeFilter();

	/**
	 * 获取zk服务
	 * 
	 * @return
	 */
	IZookeeperService getZookeeperService();

	/**
	 * 成员管理
	 * 
	 * @return
	 */
	IMemberService getMemberService();

	/**
	 * rpc代理服务
	 * 
	 * @return
	 */
	IRpcServicePrx getRpcServicePrx();

	/**
	 * 网络客户端服务
	 * 
	 * @return
	 */
	INetClientService getNetClientService();

	/**
	 * 全局
	 * 
	 * @return
	 */
	IGlobalContext getGlobalContext();

	IEventService getEventService();
}
