package com.elex.common.component.function.node;

import com.elex.common.component.function.IFunctionContext;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 节点管理（监听节点变化）
 * 
 * @author mausmars
 * 
 */
public class NodeManager {
	protected static final ILogger logger = XLogUtil.logger();

	// 模块节点
	private AFunctionNode root;

	public void init(String moduleName, IFunctionContext context) throws Exception {
		root = new LevelModuleNode(moduleName);
		root.setContext(context);
		root.local2zkCreate();
	}

	public AFunctionNode selectNode(String groupId, String regionId, FunctionType functionType) {
		AFunctionNode gnode = root.selectChild(groupId);
		if (gnode == null) {
			return null;
		}
		AFunctionNode rnode = gnode.selectChild(regionId);
		if (rnode == null) {
			return null;
		}
		return rnode.selectChild(functionType.name());
	}

	public AFunctionNode selectNode(NodePath path) {
		return selectNode(root, path);
	}

	/**
	 * 查找Node
	 * 
	 * @param node
	 * @param path
	 * @return
	 */
	AFunctionNode selectNode(AFunctionNode node, NodePath path) {
		if (node.getRoot().getKey().equals(path.getLastKey())) {
			// 如果是根路径
			return (AFunctionNode) node.getRoot();
		}
		String ckey = path.getChildKey(node.getLevel());
		AFunctionNode cnode = (AFunctionNode) node.selectChild(ckey);
		if (cnode == null) {
			return null;
		}
		if (cnode.getLevel() == path.getLevel()) {
			return cnode;
		} else if (cnode.getLevel() < path.getLevel()) {
			return selectNode(cnode, path);
		} else {
			return null;
		}
	}

	public AFunctionNode getRootNode() {
		return root;
	}
}
