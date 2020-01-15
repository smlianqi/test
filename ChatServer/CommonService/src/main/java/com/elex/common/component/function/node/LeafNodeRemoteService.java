package com.elex.common.component.function.node;

import com.elex.common.component.function.info.*;
import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.function.type.LeafNodeType;
import com.elex.common.component.net.client.INetClientService;
import com.elex.common.component.rpc.IRpcFServicePrx;
import com.elex.common.component.rpc.IRpcServicePrx;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.service.netty.INetNettyClient;
import com.elex.common.net.service.netty.NettyTcpClient;
import com.elex.common.net.service.netty.session.ForwardSubSessionBox;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.session.ISession;
import com.elex.common.net.session.ISessionPool;
import com.elex.common.net.session.SessionPool;
import com.elex.common.net.type.SessionAttachType;

/**
 * 其他服的功能节点。 node=/sms/gid/ftype/fid#n
 * 
 * @author mausmars
 * 
 */
public class LeafNodeRemoteService extends LevelLeafNode {
	public LeafNodeRemoteService(String key) {
		super(key);
		leafNodeType = LeafNodeType.RemoteService;
	}

	@Override
	public void zk2localCreate() {
		// 创建zk节点映射；设置监听。监听功能类型
		try {
			// 这里不为空，就初始化functionMenu
			initFunctionServicePrx(getCurrentpath());
			insertNode();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeDataChanged() {
		// 数据改变证明服务器注册成功，这里调用ice获取功能服务信息
		try {
			initFunctionServicePrx(getCurrentpath());
			insertNode();
			if (logger.isDebugEnabled()) {
				logger.debug("LocalService nodeDataChanged! pkey=" + parent.getKey() + " key=" + key);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeDeleted() {
		try {
			removeNode();
			// 设置服务器为空，不可用
			IFunctionInfo functionInfo = setFunctionInfo(null);
			if (functionInfo != null) {
				// 释放资源
				functionInfo.release();
			}
			// 移除自己
			this.remove();
			if (logger.isDebugEnabled()) {
				logger.debug("LocalService nodeDeleted! pkey=" + parent.getKey() + " key=" + key);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void nodeCreated() {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("LocalService nodeCreated! pkey=" + parent.getKey() + " key=" + key);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	// ------------------------------------------
	private void initFunctionServicePrx(String path) throws Exception {
		byte[] bytes = context.getZookeeperService().getData(path, true, null);
		if (bytes == null) {
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("###### initFunctionServicePrx data=[" + new String(bytes) + "] path=" + path);
		}
		// 从zk上获取服务信息
		IFunctionInfo fi = FunctionInfo.createFunctionInfo((String) this.key, bytes);

		for (FService service : fi.getFServices()) {
			switch (service.getFsType()) {
			case IceRpc:
				// 初始化服务代理
				if (context.getRpcServicePrx() != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("########### initFunctionServicePrx  key=" + key);
					}
					initRpcFServicePrx(fi, context.getRpcServicePrx());
				}
				break;
			case NettyRpc:

				break;
			case NettyForward:
				if (context.getNetClientService() != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("########### initFunctionServicePrx  key=" + key);
					}
					initForwardFServiceClient(fi, context.getNetClientService());
				}
				break;
			default:
				break;
			}
		}
		setFunctionInfo(fi);
	}

	// 调用父节点移除节点环
	private void removeNode() {
		LevelLeafMgrNode parentNode = (LevelLeafMgrNode) this.parent;
		parentNode.removeNode(getFunctionInfo().getFunctionId());
	}

	// 调用父节点插入节点环
	private void insertNode() {
		LevelLeafMgrNode parentNode = (LevelLeafMgrNode) this.parent;
		parentNode.insertNode((String) key);
	}

	private void initForwardFServiceClient(IFunctionInfo functionInfo, INetClientService netClientService) {
		FService service = functionInfo.getFService(FServiceType.NettyForward);

		ServiceAddress sa = service.getServiceAddress();

		String serverId = context.getCurrentServerFid();
		for (IServiceInfo serviceInfo : service.getServiceInfoMap().values()) {
			// 这里做强转
			NettyForwardServiceInfo si = (NettyForwardServiceInfo) serviceInfo;
			SessionPool sessionPool = new SessionPool(si.getConnectCount());
			for (int i = 0; i < si.getConnectCount(); i++) {
				INetNettyClient netClient = netClientService.createNetClient(sa.getHost(), sa.getPort());
				ISession session = netClient.getSession();
				// 绑定
				sessionPool.bindingClient(i, serverId, si.getToken(), session);
				//huangyuanqiang 做特殊处理
				try {
					if(netClient instanceof NettyTcpClient) {
						((NettyTcpClient)netClient).registerListener();
					}
				} catch (InterruptedException e) {
					throw new RuntimeException("注册net客户端的监听器时发生错误！");
				}
			}
			si.setClientSessionPool(sessionPool);
			if (logger.isDebugEnabled()) {
				logger.debug("### InitForwardFServiceClient [" + sa.getHost() + ":" + sa.getPort() + "] connectCount="
						+ si.getConnectCount());
			}
			// 发送绑定
			sendBindingSocketUp(sessionPool);
		}
	}

	private void sendBindingSocketUp(ISessionPool sessionPool) {
		for (int i = 0; i < sessionPool.size(); i++) {
			ISession session = sessionPool.getSession(i);

			SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
			ForwardSubSessionBox forwardSessionInfo = sessionBox.getSubSessionBox();
			IMessage msg = sessionBox.getMessageCreater().createBindingSocketUpMessage(forwardSessionInfo.getSid(),
					forwardSessionInfo.getToken(), i);
			// 发送消息
			sessionPool.send(i, msg);
		}
	}

	private void initRpcFServicePrx(IFunctionInfo functionInfo, IRpcServicePrx rpcServicePrx) {
		FService service = functionInfo.getFService(FServiceType.IceRpc);

		ServiceAddress sa = service.getServiceAddress();
		FunctionId functionId = functionInfo.getFunctionId();
		for (IServiceInfo serviceInfo : service.getServiceInfoMap().values()) {
			// 这里做强转
			IceRpcServiceInfo si = (IceRpcServiceInfo) serviceInfo;

			IRpcFServicePrx servicePrx = rpcServicePrx.createServicePrx(si.getObjectId(),
					sa.getNetProtocolType().name(), sa.getHost(), sa.getPort(), si.getPackageName(), si.getClassName(),
					functionId.getFunctionType());
			si.setServicePrx(servicePrx);
			if (logger.isDebugEnabled()) {
				logger.debug("### InitRpcFServicePrx [" + si.getPackageName() + "." + si.getClassName() + "] objId="
						+ si.getObjectId());
			}
		}
	}
}
