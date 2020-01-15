package com.elex.common.component.function.node;

import org.apache.zookeeper.data.Stat;

import com.elex.common.component.function.info.FunctionId;
import com.elex.common.component.function.type.LeafNodeType;

/**
 * leader节点，这个路径节点是监听该功能类型主从的节点
 * 
 * @author mausmars
 * 
 */
public class LeafNodeLeader extends LevelLeafNode {
	public LeafNodeLeader(String key) {
		super(key);
		this.leafNodeType = LeafNodeType.Leader;
	}

	@Override
	public void zk2localCreate() {
		// 创建zk节点映射；设置监听。监听功能类型
		try {
			// 获取主服务器信息
			Stat stat = context.getZookeeperService().exists(getCurrentpath(), true);
			if (stat != null) {
				Stat state = new Stat();
				byte[] leader = context.getZookeeperService().getData(getCurrentpath(), true, state);
				if (leader != null) {
					// 这里存字符串
					FunctionId functionId = FunctionId.createFunctionId(new String(leader));
					changeMasterKey(functionId, state.getCtime());
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeDataChanged() {
		// 数据改变证明服务器注册成功，这里调用ice获取功能服务信息
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Leader nodeDataChanged! pkey=" + parent.getKey());
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeDeleted() {
		try {
			// 设置为无领导
			changeMasterKey(null, 0);

			// 重新监听
			context.getZookeeperService().exists(getCurrentpath(), true);

			// 这里遍历重新选举
			for (AFunctionNode node : parent.selectAllChild()) {
				LevelLeafNode n = (LevelLeafNode) node;
				if (n.getLeafNodeType() == LeafNodeType.LocalService) {
					LeafNodeLocalService fln = (LeafNodeLocalService) n;
					fln.findLeader();
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Leader nodeDeleted! pkey=" + parent.getKey());
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeCreated() {
		try {
			// 获取并重新设置监听
			Stat state = new Stat();
			byte[] leader = context.getZookeeperService().getData(getCurrentpath(), true, state);
			if (leader != null) {
				FunctionId functionId = FunctionId.createFunctionId(new String(leader));
				changeMasterKey(functionId, state.getCtime());
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Leader nodeCreated! pkey=" + parent.getKey());
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void changeMasterKey(FunctionId functionId, long ctime) {
		// 调用父节点改变
		LevelLeafMgrNode parentNode = (LevelLeafMgrNode) this.parent;
		parentNode.changeMasterKey(functionId, ctime);
		if (logger.isDebugEnabled()) {
			logger.debug("Leader node change ! pkey=" + parent.getKey() + ",leader key=" + key + ",ctime=" + ctime);
		}
	}
}
