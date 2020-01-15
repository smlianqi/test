package com.elex.common.component.function.node;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 区节点。如： /sms/组id/区id
 * 
 * @author mausmars
 * 
 */
public class LevelRegionNode extends AFunctionNode {
	public LevelRegionNode(String key) {
		super(key);
	}

	// 获得并创建子节点
	@Override
	public AFunctionNode createAndGetLocalChild(String key) {
		AFunctionNode node = (AFunctionNode) selectChild(key);
		if (node != null) {
			node.setNewCreate2Old();
			return node;
		}
		LevelLeafMgrNode n = new LevelLeafMgrNode(key);
		node = insertChild(n);
		n.context = this.context;

		if (logger.isDebugEnabled()) {
			logger.debug("Local node is created! level=" + n.getLevel() + ",key=" + key + ",path=[" + n.getCurrentpath()
					+ "]");
		}
		// 这里就把领导节点创建出来
		n.createLeaderNode();
		return n;
	}

	@Override
	public void zk2localCreate() {
		// 创建zk节点映射；设置监听。监听区
		try {
			Stat stat = context.getZookeeperService().exists(getCurrentpath(), false);
			if (stat == null) {
				return;
			}
			// 监听 /sms/组id下全部子节点
			List<String> childNodeStrs = context.getZookeeperService().getChildren(getCurrentpath(), true);
			for (String childNodeStr : childNodeStrs) {
				if (!context.getNodeFilter().isFunctionFilterNode(childNodeStr)) {
					continue;
				}
				AFunctionNode cnode = createAndGetLocalChild(childNodeStr);
				cnode.zk2localCreate();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void local2zkCreate() throws Exception {
		String path = getCurrentpath();
		if (context.getZookeeperService().exists(path, true) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("ZK server is exist node,no create node:[" + path + "]");
			}
		} else {
			// 创建永久节点
			String node = createZKNode(path, null, CreateMode.PERSISTENT);
			if (logger.isDebugEnabled()) {
				logger.debug("Create ZK node=[" + node + "]");
			}
		}
		// 监听 /sms/组id下全部子节点
		context.getZookeeperService().getChildren(path, true);
	}

	@Override
	public void local2zkRemove() throws Exception {

	}

	@Override
	public void nodeChildrenChanged() {
		try {
			// 监听 /sms/组id下全部子节点
			List<String> childNodeStrs = context.getZookeeperService().getChildren(getCurrentpath(), true);
			for (String childNodeStr : childNodeStrs) {
				if (!context.getNodeFilter().isFunctionFilterNode(childNodeStr)) {
					continue;
				}
				AFunctionNode cnode = createAndGetLocalChild(childNodeStr);
				if (cnode.isNewCreate()) {
					cnode.zk2localCreate();
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void none() {

	}

	@Override
	public void nodeCreated() {

	}

	@Override
	public void nodeDeleted() {

	}

	@Override
	public void nodeDataChanged() {

	}
}
