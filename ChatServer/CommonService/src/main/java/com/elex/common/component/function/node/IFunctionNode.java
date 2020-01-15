package com.elex.common.component.function.node;

public interface IFunctionNode {
	/**
	 * 得到和创建一个本地子节点（有了本地节点才可以操作zk）
	 * 
	 * @param key
	 * @return
	 */
	IFunctionNode createAndGetLocalChild(String key);

	/**
	 * 创建zk节点（只有当前服，才会调用这个方法）
	 * 
	 * @param paths
	 */
	void local2zkCreate() throws Exception;

	/**
	 * 移除zk节点（只有当前服，才会调用这个方法）
	 * 
	 * @param paths
	 */
	void local2zkRemove() throws Exception;

	/**
	 * 更新数据（本地数据更新到zk）
	 * 
	 * @throws Exception
	 */
	void local2zkData() throws Exception;

	/**
	 * 根据zk的节点，映射创建节点
	 */
	void zk2localCreate();
}
