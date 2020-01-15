package com.elex.common.component.function.router;

import com.elex.common.util.hash.ConsistentHash;

public class ConsistentHashRouter implements INodeRouter {
	// 一致哈希算法
	private ConsistentHash<String> consistentHash;

	public ConsistentHashRouter() {
		this.consistentHash = new ConsistentHash<String>();
	}

	@Override
	public void insertNode(String nodeId) {
		consistentHash.add(nodeId);
	}

	@Override
	public void removeNode(String nodeId) {
		consistentHash.remove(nodeId);
	}

	@Override
	public String randomNode() {
		// TODO
		return consistentHash.get("sdfsdf");
	}
}
