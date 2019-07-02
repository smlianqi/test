package com.elex.common.component.function.router;

/**
 * 节点路由
 * 
 * @author mausmars
 *
 */
public interface INodeRouter {
	/**
	 * 插入节点
	 * 
	 * @param nodeId
	 */
	void insertNode(String nodeId);

	/**
	 * 删除节点
	 * 
	 * @param nodeId
	 */
	void removeNode(String nodeId);

	/**
	 * 随机节点
	 * 
	 * @return
	 */
	String randomNode();
}
