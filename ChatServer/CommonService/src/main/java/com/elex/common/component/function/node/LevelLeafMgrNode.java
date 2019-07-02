package com.elex.common.component.function.node;

import java.util.LinkedList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.elex.common.component.function.IFunctionCluster;
import com.elex.common.component.function.event.MasterChangeEvent;
import com.elex.common.component.function.event.NodeDeleteEvent;
import com.elex.common.component.function.info.FunctionId;
import com.elex.common.component.function.info.IFunctionInfo;
import com.elex.common.component.function.router.DefaultNodeRouter;
import com.elex.common.component.function.router.INodeRouter;
import com.elex.common.component.function.type.LeafNodeType;
import com.elex.common.service.GeneralConstant;
import com.elex.common.util.string.StringUtil;

/**
 * 叶子管理节点。如：/sms/newgame_1/r1/test
 * 
 * @author mausmars
 * 
 */
public class LevelLeafMgrNode extends AFunctionNode implements IFunctionCluster {
	// 主服务器key
	volatile FunctionId masterFunctionId;
	// 当前服务器key（如果为null就没有当前服）
	volatile FunctionId currentFunctionId;
	// 之前的主
	volatile FunctionId proMasterFunctionId;

	private INodeRouter nodeRouter;

	public LevelLeafMgrNode(String key) {
		super(key);
		this.nodeRouter = new DefaultNodeRouter();
	}

	public AFunctionNode createAndGetLocalChild(String key) {
		AFunctionNode node = (AFunctionNode) selectChild(key);
		if (node != null) {
			node.setNewCreate2Old();
			return node;
		}
		node = new LeafNodeLocalService(createId(key));
		node = insertChild(node);
		node.context = this.context;
		if (logger.isDebugEnabled()) {
			logger.debug("Local node is created! level=" + node.getLevel() + " key=" + key + " path=["
					+ node.getCurrentpath() + "]");
		}
		return node;
	}

	private String createId(String fid) {
		StringBuilder sb = new StringBuilder();
		sb.append(fid);

		if (!fid.contains(StringUtil.SeparatorAt)) {
			sb.append(StringUtil.SeparatorAt);
			sb.append(context.getGlobalContext().getServerId());
		}
		return sb.toString();
	}

	private AFunctionNode getAndCreateLocalChildPrxOrLeader(String key) {
		AFunctionNode node = (AFunctionNode) selectChild(key);
		if (node != null) {
			// 如果已经存在就返回空
			return null;
		}
		if (key.equals(GeneralConstant.ZK_SM_Leader_Path)) {
			// 首领路径
			node = new LeafNodeLeader(key);
		} else {
			// 功能服务路径
			node = new LeafNodeRemoteService(key);
		}
		node = insertChild(node);
		node.context = this.context;
		if (logger.isDebugEnabled()) {
			logger.debug("Local node is created! level=" + node.getLevel() + ",key=" + key + ",path=["
					+ node.getCurrentpath() + "]");
		}
		return node;
	}

	@Override
	public void initCPrxNode(InitNodeContext nodeContext) {
		String key = nodeContext.getNextChildKey();
		if (key == null) {
			return;
		}
		LeafNodeRemoteService node = new LeafNodeRemoteService(key);
		node = insertChild(node);
		insertNode(key);
		if (node.getFunctionInfo() == null) {
			node.setFunctionInfo(nodeContext.getFunctionInfo());
		} else {
			nodeContext.setFunctionInfo(node.getFunctionInfo());
		}
		if (node.isNewCreate()) {
			node.context = this.context;
		}
	}

	@Override
	public void zk2localCreate() {
		// 创建zk节点映射；设置监听。监听功能类型
		try {
			// 这里不用设计过滤，如果不需要这个类型的服务上一层就过滤掉了，这层不会被创建
			Stat stat = context.getZookeeperService().exists(getCurrentpath(), false);
			if (stat == null) {
				return;
			}
			createLeaderNode();
			createChilderNode();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void initCNode(InitNodeContext nodeContext) {
	}

	void createLeaderNode() {
		// 创建领导节点的映射节点
		AFunctionNode leaderNode = getAndCreateLocalChildPrxOrLeader(GeneralConstant.ZK_SM_Leader_Path);
		if (leaderNode != null) {
			leaderNode.zk2localCreate();
		}
	}

	@Override
	public void local2zkCreate() throws Exception {
		String path = getCurrentpath();
		if (context.getZookeeperService().exists(path, true) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("ZK server is exist node,no create node:[" + path + "]");
			}
			return;
		}
		String node = createZKNode(path, null, CreateMode.PERSISTENT);
		if (logger.isDebugEnabled()) {
			logger.debug("Create ZK node [" + node + "]");
		}
		// 设置子节点监听
		context.getZookeeperService().getChildren(getCurrentpath(), true);
	}

	@Override
	public void nodeDataChanged() {
		try {
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeDeleted() {
		try {
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeCreated() {
		try {
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeChildrenChanged() {
		// 之前设置过监听，后边要继续监听
		try {
			createChilderNode();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	// 初始化或监听zk子节点变化，创建node
	private void createChilderNode() throws Exception {
		List<String> childrenNodeStrs = context.getZookeeperService().getChildren(getCurrentpath(), true);
		for (String childrenNodeStr : childrenNodeStrs) {
			LevelLeafNode childNode = (LevelLeafNode) getAndCreateLocalChildPrxOrLeader(childrenNodeStr);
			if (childNode == null) {
				continue;
			}
			if (childNode.getLeafNodeType() == LeafNodeType.RemoteService) {
				LeafNodeRemoteService cn = (LeafNodeRemoteService) childNode;
				cn.zk2localCreate();
			}
		}
	}

	@Override
	public void none() {

	}

	@Override
	public void local2zkRemove() throws Exception {

	}

	// 加入节点
	void insertNode(String fid) {
		nodeRouter.insertNode(fid);
	}

	// 删除节点
	void removeNode(FunctionId functionId) {
		String fid = functionId.getFid(true);
		nodeRouter.removeNode(fid);

		IFunctionInfo functionInfo = getFunctionInfo(fid);
		NodeDeleteEvent event = new NodeDeleteEvent();
		event.setFunctionId(functionInfo.getFunctionId());
		event.setMaster(isCurrentMaster());// 是否是主
		event.setFunctionContext(getContext());
		// 通知
		context.getEventService().notify(event);
	}

	// 主节点变换
	void changeMasterKey(FunctionId key, long ctime) {
		if (key == null) {
			// 记录上一次的主key
			proMasterFunctionId = masterFunctionId;
			masterFunctionId = key;
			return;
		} else {
			masterFunctionId = key;
		}
		MasterChangeEvent event = new MasterChangeEvent();
		event.setMasterKey(key);
		event.setProMasterKey(proMasterFunctionId);
		event.setCtime(ctime);
		event.setMaster(isCurrentMaster());// 是否是主
		event.setFunctionContext(getContext());

		// 通知
		context.getEventService().notify(event);
	}

	// 设置当前服功能id
	void setCurrentKey(FunctionId functionId) {
		this.currentFunctionId = functionId;
	}

	public FunctionId getCurrentFunctionId() {
		return this.currentFunctionId;
	}

	// -------------------------------
	@Override
	public boolean isAvailable() {
		return masterFunctionId != null || currentFunctionId != null;
	}

	@Override
	public boolean isCurrentMaster() {
		// 当前服是主服
		if (masterFunctionId == null || currentFunctionId == null) {
			return false;
		}
		return masterFunctionId.getFid(true) == currentFunctionId.getFid(true);
	}

	@Override
	public List<IFunctionInfo> getRemoteFunctionInfos() {
		List<IFunctionInfo> list = new LinkedList<>();
		for (AFunctionNode node : selectAllChild()) {
			LevelLeafNode n = (LevelLeafNode) node;
			if (n.getLeafNodeType() == LeafNodeType.RemoteService) {
				list.add(n.getFunctionInfo());
			}
		}
		return list;
	}

	@Override
	public IFunctionInfo getFunctionInfo(String fid) {
		LevelLeafNode leafNode = (LevelLeafNode) selectChild(fid);
		if (leafNode == null) {
			return null;
		}
		return leafNode.getFunctionInfo();
	}

	@Override
	public IFunctionInfo getRandomFunctionInfo() {
		String k = nodeRouter.randomNode();
		if (k != null) {
			LevelLeafNode leafNode = (LevelLeafNode) selectChild(k);
			if (leafNode != null) {
				return leafNode.getFunctionInfo();
			}
			leafNode = (LevelLeafNode) selectChild(currentFunctionId.getFid(true));
			if (leafNode != null) {
				return leafNode.getFunctionInfo();
			}
		}
		// 从本地取
		return getLocalFunctionInfo();
	}

	@Override
	public IFunctionInfo getLocalFunctionInfo() {
		if (currentFunctionId == null) {
			return null;
		}
		String key = currentFunctionId.getFid(true);
		LevelLeafNode leafNode = (LevelLeafNode) selectChild(key);
		if (leafNode == null) {
			return null;
		}
		return leafNode.getFunctionInfo();
	}
}
