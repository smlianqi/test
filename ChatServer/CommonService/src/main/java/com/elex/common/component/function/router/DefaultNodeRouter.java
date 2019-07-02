package com.elex.common.component.function.router;

import java.util.Random;

import io.netty.util.internal.ConcurrentSet;

/**
 * 默认节点路由
 * 
 * @author mausmars
 *
 */
public class DefaultNodeRouter implements INodeRouter {
	private ConcurrentSet<String> nodeSet = new ConcurrentSet<String>();
	private Random random = new Random();

	@Override
	public void insertNode(String nodeId) {
		nodeSet.add(nodeId);
	}

	@Override
	public void removeNode(String nodeId) {
		nodeSet.remove(nodeId);
	}

	@Override
	public String randomNode() {
		String[] keys = nodeSet.toArray(new String[0]);
		if (keys.length == 0) {
			return null;
		}
		return keys[random.nextInt(keys.length)];
	}
}
