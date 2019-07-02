package com.elex.common.component.function.node;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.data.Stat;

import com.elex.common.component.function.type.LeafNodeType;
import com.elex.common.service.GeneralConstant;
import com.elex.common.util.string.StringUtil;

/**
 * 当前服的功能节点。 node=/sms/gid/ftype/fid#n
 * 
 * @author mausmars
 * 
 */
public class LeafNodeLocalService extends LevelLeafNode {
	public LeafNodeLocalService(String fid) {
		super(fid);
		this.leafNodeType = LeafNodeType.LocalService;
	}

	@Override
	public void initCNode(InitNodeContext nodeContext) {
		setFunctionInfo(nodeContext.getFunctionInfo());

		String[] sids = ((String) getKey()).split(StringUtil.SeparatorAt);
		String aotuId = sids[sids.length - 1];
		getFunctionInfo().getFunctionId().setAotuId(aotuId);

		// 设置当前功能id
		((LevelLeafMgrNode) this.parent).setCurrentKey(getFunctionInfo().getFunctionId());
	}

	@Override
	public void local2zkCreate() throws Exception {
		String path = getCurrentpath();
		String node = createZKNode(path, null, CreateMode.EPHEMERAL);// 把配置信息保存到zk上

		if (logger.isDebugEnabled()) {
			logger.debug("Register node (create ZK temp node)=[" + node + "]");
		}
	}

	public void setZLNodeData() throws Exception {
		String path = getCurrentpath();
		byte[] data = getFunctionInfo().getBytes();// 序列化全部菜单

		// 设置节点数据
		setNodeData(path, data, -1);
		// 选举
		findLeader();
	}

	@Override
	public void local2zkRemove() throws Exception {
		// 删除zk上的节点，这里为服务器主动删除
		context.getZookeeperService().delete((String) this.key, -1);
		this.remove();// 移除当前的本地
		if (logger.isDebugEnabled()) {
			logger.debug("Unregister node=[" + (String) this.key + "]");
		}
	}

	/**
	 * 选举主服务器，如果是当前服的才参与
	 * 
	 * @throws InterruptedException
	 */
	void findLeader() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Find leader start!");
		}
		String leaderPath = getLeaderPath();
		try {
			Stat stat = context.getZookeeperService().exists(leaderPath, true);
			if (stat != null) {
				byte[] leader = gainLeaderData(leaderPath);
				if (leader == null) {
					// 这里成功就表示是主服务器，后注册的抛异常
					leader = ((String) this.key).getBytes();
					createLeaderNode(leaderPath, leader);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Find leader finished! leader key=" + new String(leader));
				}
			} else {
				byte[] leader = ((String) this.key).getBytes();
				try {
					createLeaderNode(leaderPath, leader);
					if (logger.isDebugEnabled()) {
						logger.debug("Find leader finished! leader key=" + new String(leader));
					}
				} catch (NodeExistsException e) {
					// 节点已经存在
					leader = gainLeaderData(leaderPath);
					if (logger.isDebugEnabled()) {
						logger.debug("Leader Node Exists! key=" + new String(leader));
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private byte[] gainLeaderData(String leaderPath) throws Exception {
		return context.getZookeeperService().getData(leaderPath, true, null);
	}

	private void createLeaderNode(String leaderPath, byte[] leader) throws Exception {
		createZKNode(leaderPath, leader, CreateMode.EPHEMERAL);
	}

	private String getLeaderPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(parent.getFullPath());
		sb.append(StringUtil.SeparatorSlash);
		sb.append(GeneralConstant.ZK_SM_Leader_Path);
		return sb.toString();
	}
}
