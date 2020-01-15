package com.elex.common.component.function.node;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import com.elex.common.component.function.IFunctionContext;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 抽象功能节点
 * 
 * @author mausmars
 * 
 */
public abstract class AFunctionNode implements IFunctionNode {
	protected static final ILogger logger = XLogUtil.logger();

	protected String key; // key

	protected volatile AFunctionNode parent;// 父节点
	protected ConcurrentMap<String, AFunctionNode> children = new ConcurrentHashMap<>();// 子节点

	// ----------------------
	protected IFunctionContext context;

	// 当前路径
	protected String currentpath;
	// 新创建
	protected volatile boolean isNewCreate = true;

	AFunctionNode(String key) {
		this.key = key;
	}

	/** 移除节点本身 */
	public void remove() {
		if (parent == null) {
			return;
		}
		this.parent.getChildren().remove(this.key);
	}

	/** 移除子节点 */
	public AFunctionNode removeChild(String key) {
		return children.remove(key);
	}

	/** 插入子节点 */
	public <T extends AFunctionNode> T insertChild(AFunctionNode child) {
		AFunctionNode n = children.putIfAbsent(child.getKey(), child);
		if (n == null) {
			n = child;
			// 添加父节点
			child.setParent(this);
		}
		return (T) n;
	}

	/** 获取父节点 */
	public AFunctionNode getParent() {
		return this.parent;
	}

	/** 查询子节点 */
	public AFunctionNode selectChild(String key) {
		return children.get(key);
	}

	/** 得到全部子节点 */
	public Collection<AFunctionNode> selectAllChild() {
		return children.values();
	}

	/** 得到根节点 */
	public AFunctionNode getRoot() {
		AFunctionNode n = this;
		for (;;) {
			if (n.getParent() == null) {
				break;
			}
			n = n.getParent();
		}
		return n;
	}

	/** 是否包含子节点 */
	public boolean containsChild(String key) {
		return children.containsKey(key);
	}

	/** 得到节点全路径 */
	public String getFullPath() {
		List<AFunctionNode> nodes = getFullNode();

		StringBuilder sb = new StringBuilder();
		for (AFunctionNode node : nodes) {
			sb.append("/");
			sb.append(node.getKey());
		}
		return sb.toString();
	}

	/** 得到节点全路径 */
	public List<AFunctionNode> getFullNode() {
		LinkedList<AFunctionNode> nodes = new LinkedList<>();
		AFunctionNode n = this;
		for (;;) {
			nodes.addFirst(n);
			if (n.getParent() == null) {
				break;
			}
			n = n.getParent();
		}
		return nodes;
	}

	/** 在现有的节点上添加 */
	// public AFunctionNode createNewRoot(String fullPath) {
	// String[] paths = fullPath.split("/");
	//
	// AFunctionNode n = this.getRoot();
	// boolean isMatchRoot = false;
	// for (String path : paths) {
	// if (path.isEmpty()) {
	// continue;
	// }
	// if (!isMatchRoot) {
	// if (!path.equals(n.getKey())) {
	// return null;
	// } else {
	// isMatchRoot = true;
	// }
	// } else {
	// AFunctionNode node = new AFunctionNode(path);
	// n.insertChild(node);
	// n = node;
	// }
	// }
	// return n;
	// }

	/** 创建新的目录结构 */
	// public static AFunctionNode createRoot(String fullPath) {
	// String[] paths = fullPath.split("/");
	//
	// AFunctionNode n = null;
	// for (String path : paths) {
	// if (path.isEmpty()) {
	// continue;
	// }
	// if (n == null) {
	// n = new AFunctionNode(path);
	// } else {
	// AFunctionNode node = new FunctionNode(path);
	// n.insertChild(node);
	// n = node;
	// }
	// }
	// return n;
	// }

	/** 获得层数 */
	public int getLevel() {
		int level = 0;
		AFunctionNode n = this;
		for (;;) {
			if (n.getParent() == null) {
				break;
			}
			level++;
			n = n.getParent();
		}
		return level;
	}

	public String getKey() {
		return key;
	}

	void setParent(AFunctionNode parent) {
		this.parent = parent;
	}

	public ConcurrentMap<String, AFunctionNode> getChildren() {
		return children;
	}

	// --------------------------------
	protected boolean isNewCreate() {
		return isNewCreate;
	}

	protected void setNewCreate2Old() {
		synchronized (this) {
			isNewCreate = false;
		}
	}

	String getCurrentpath() {
		if (currentpath == null) {
			currentpath = getFullPath();
		}
		return currentpath;
	}

	protected String createZKNode(String path, byte data[], CreateMode createMode) throws Exception {
		List<ACL> acl = context.getZookeeperService().getPermissions();
		if (acl == null) {
			// 无权限设置
			acl = Ids.OPEN_ACL_UNSAFE;
		}
		// 创建永久节点
		return context.getZookeeperService().create(path, data, acl, createMode);
	}

	// 设置节点数据
	protected Stat setNodeData(String path, byte data[], int version) throws Exception {
		List<ACL> acl = context.getZookeeperService().getPermissions();
		if (acl == null) {
			// 无权限设置
			acl = Ids.OPEN_ACL_UNSAFE;
		}
		// 创建永久节点
		return context.getZookeeperService().setData(path, data, version);
	}

	// --------------------------------
	public void initLocalNode(InitNodeContext nodeContext) throws Exception {
		String key = nodeContext.getChildKey();
		if (key == null) {
			return;
		}
		AFunctionNode node = (AFunctionNode) this.createAndGetLocalChild(key);
		node.initCNode(nodeContext);
		nodeContext.next();
		node.initLocalNode(nodeContext);
	}

	public void initLocalPrxNode(InitNodeContext nodeContext) throws Exception {
		String key = nodeContext.getChildKey();
		if (key == null) {
			return;
		}
		AFunctionNode node = (AFunctionNode) this.createAndGetLocalChild(key);
		node.initCPrxNode(nodeContext);
		nodeContext.next();
		node.initLocalPrxNode(nodeContext);
	}

	public AFunctionNode initNode(InitNodeContext nodeContext) throws Exception {
		String key = nodeContext.getChildKey();
		if (key == null) {
			return null;
		}
		AFunctionNode node = (AFunctionNode) this.createAndGetLocalChild(key);
		node.local2zkCreate();

		key = nodeContext.getNextChildKey();
		if (key != null) {
			nodeContext.next();
			return node.initNode(nodeContext);
		} else {
			return node;
		}
	}

	/**
	 * 初始化当前节点
	 * 
	 * @param nodeContext
	 */
	void initCNode(InitNodeContext nodeContext) {
		if (logger.isDebugEnabled()) {
			logger.debug("Path=" + getCurrentpath() + " initCNode is null run!");
		}
	}

	void initCPrxNode(InitNodeContext nodeContext) {
		if (logger.isDebugEnabled()) {
			logger.debug("Path=" + getCurrentpath() + " initCPrxNode is null run!");

			if (getCurrentpath().equals("/client/slgx1")) {
				return;
			}
		}
	}

	// --------------------------------
	public AFunctionNode selectNode(String groupId, String regionId, FunctionType functionType) {
		AFunctionNode root = getRoot();

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

	// --------------------------------
	// 一下方法对应zookeeper的事件类
	public void none() {
	}

	public void nodeCreated() {
	}

	public void nodeDeleted() {
	}

	public void nodeChildrenChanged() {
	}

	public void nodeDataChanged() {
	}

	// --------------------------------
	public void local2zkCreate() throws Exception {
	}

	public void local2zkRemove() throws Exception {
	}

	public void local2zkData() throws Exception {
	}

	public void zk2localCreate() {
	}

	// --------------------------------
	public void print() {
		System.out.println("=====================");
		StringBuilder sb = new StringBuilder();
		print(this, sb);
		System.out.println(sb.toString());
	}

	private void print(AFunctionNode n, StringBuilder sb) {
		sb.append(n);
		sb.append("(");
		ConcurrentMap<String, AFunctionNode> children = n.getChildren();
		for (AFunctionNode child : children.values()) {
			sb.append(child);
			sb.append(",");
		}
		sb.append(")");
		sb.append("\r\n");

		for (AFunctionNode child : children.values()) {
			print(child, sb);
		}
	}

	@Override
	public String toString() {
		return "FunctionNode [key=" + key + "]";
	}

	// --------------------------------
	void setContext(IFunctionContext context) {
		this.context = context;
	}

	public IFunctionContext getContext() {
		return context;
	}
}
