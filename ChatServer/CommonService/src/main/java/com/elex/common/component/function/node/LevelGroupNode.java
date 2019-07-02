package com.elex.common.component.function.node;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 组节点。如： /sms/组id
 * 
 * @author mausmars
 * 
 */
public class LevelGroupNode extends AFunctionNode {
	public LevelGroupNode(String key) {
		super(key);
	}

	public AFunctionNode createAndGetLocalChild(String key) {
		AFunctionNode node = (AFunctionNode) selectChild(key);
		if (node != null) {
			node.setNewCreate2Old();
			return node;
		}
		node = new LevelRegionNode(key);
		node = insertChild(node);
		node.context = this.context;
		if (logger.isDebugEnabled()) {
			logger.debug("Local node is created! level=" + node.getLevel() + ",key=" + key + ",path=["
					+ node.getCurrentpath() + "]");
		}
		return node;
	}

	@Override
	public void zk2localCreate() {
		try {
			Stat stat = context.getZookeeperService().exists(getCurrentpath(), false);
			if (stat == null) {
				return;
			}
			// 监听sms子节点变化
			List<String> childNodeStrs = context.getZookeeperService().getChildren(getCurrentpath(), true);
			for (String childNodeStr : childNodeStrs) {
				if (!context.getNodeFilter().isRegionFilterNode(childNodeStr)) {
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
		String currentpath = getCurrentpath();

		Stat stat = context.getZookeeperService().exists(currentpath, true);
		if (stat != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("ZK server is exist node,no create node:[" + currentpath + "]");
			}
		} else {
			// 创建永久节点
			String node = createZKNode(currentpath, null, CreateMode.PERSISTENT);
			if (logger.isDebugEnabled()) {
				logger.debug("Create ZK node=[" + node + "]");
			}
		}
		// 监听sms子节点变化
		context.getZookeeperService().getChildren(currentpath, true);
	}

	@Override
	public void local2zkRemove() throws Exception {

	}

	@Override
	public void nodeChildrenChanged() {
		try {
			// 监听sms子节点变化
			List<String> childNodeStrs = context.getZookeeperService().getChildren(getCurrentpath(), true);
			for (String childNodeStr : childNodeStrs) {
				if (!context.getNodeFilter().isRegionFilterNode(childNodeStr)) {
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
