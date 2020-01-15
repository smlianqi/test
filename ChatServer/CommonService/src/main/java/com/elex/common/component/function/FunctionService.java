package com.elex.common.component.function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.WatchedEvent;

import com.elex.common.component.event.IEventService;
import com.elex.common.component.function.config.ScFunction;
import com.elex.common.component.function.event.MasterChangeEvent;
import com.elex.common.component.function.event.NodeDeleteEvent;
import com.elex.common.component.function.filter.NodeFilter;
import com.elex.common.component.function.info.FunctionId;
import com.elex.common.component.function.info.FunctionInfo;
import com.elex.common.component.function.info.IFunctionInfo;
import com.elex.common.component.function.node.AFunctionNode;
import com.elex.common.component.function.node.InitNodeContext;
import com.elex.common.component.function.node.LeafNodeLocalService;
import com.elex.common.component.function.node.LevelLeafMgrNode;
import com.elex.common.component.function.node.NodeManager;
import com.elex.common.component.function.node.NodePath;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.member.IMemberService;
import com.elex.common.component.net.client.INetClientService;
import com.elex.common.component.rpc.IRpcServicePrx;
import com.elex.common.component.rpc.RpcConstant;
import com.elex.common.component.zookeeper.IZookeeperService;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceType;
import com.google.common.eventbus.Subscribe;

/**
 * 功能管理服务（监听zk断网情况）
 * 
 * @author mausmars
 * 
 */
public class FunctionService extends AbstractService<ScFunction> implements IFunctionContext, IFunctionService {
	// zk分sms路径，避免和其他应用混一起造成很乱
	private String moduleName = "sms";
	// 节点过滤
	private NodeFilter nodeFilter;
	// 节点管理器
	private NodeManager nodeManager = new NodeManager();

	// 已经注册的功能服务菜单
	private Map<String, InitNodeContext> nodeContextMap = new HashMap<String, InitNodeContext>();
	// zk服务
	private IZookeeperService zookeeperService;
	private IMemberService memberService;
	private IRpcServicePrx rpcServicePrx;
	private INetClientService netClientService;

	// 事件服务器，用于事件分发
	protected IEventService eventService;

	public FunctionService(IServiceConfig serviceConfig, IGlobalContext globalContext) {
		super(serviceConfig, globalContext);
	}

	// 当前服的功能id
	@Override
	public String getCurrentServerFid() {
		IFunctionServiceConfig fsc = serviceConfig.getFunctionServiceConfig();
		String sid = FunctionId.createSid(fsc.getGroupId(), fsc.getRegionId(), fsc.getFunctionType(),
				String.valueOf(globalContext.getServerId()));
		return sid;
		// IFunctionServiceConfig fsc = serviceConfig.getFunctionServiceConfig();
		// AFunctionNode functionNode = nodeManager.selectNode(fsc.getGroupId(),
		// fsc.getRegionId(), fsc.getFunctionType());
		// LevelLeafMgrNode levelLeafMgrNode = (LevelLeafMgrNode) functionNode;
		// return levelLeafMgrNode.getCurrentFunctionId().getFid(true);
	}

	@Override
	public void initService() throws Exception {
		ScFunction cf = getSConfig();

		// 获取依赖的服务
		List<String> zookeeperSids = cf.getDependIdsMap().get(ServiceType.zookeeper.name());
		zookeeperService = getServiceManager().getService(ServiceType.zookeeper, zookeeperSids.get(0));

		// TODO 获取依赖的服务 这个代理服务是在function服务创建的时候创建的，需要修改
		rpcServicePrx = getServiceManager().getService(ServiceType.rpcprx, RpcConstant.RpcServicePrxId);

		// 不一定需要的属性
		List<String> clientSids = cf.getDependIdsMap().get(ServiceType.netclient.name());
		if (clientSids != null && !clientSids.isEmpty()) {
			netClientService = getServiceManager().getService(ServiceType.netclient, clientSids.get(0));
		}

		List<String> eventSids = cf.getDependIdsMap().get(ServiceType.event.name());
		eventService = getServiceManager().getService(ServiceType.event, eventSids.get(0));

		// 添加节点过滤
		nodeFilter = new NodeFilter();
		nodeFilter.setFunctionFilterStr(cf.getFunctionFiltersList());
		nodeFilter.setGroupFilterStr(cf.getGroupFiltersList());
		nodeFilter.setRegionFilterStr(cf.getRegionFiltersList());

		// 注册监听
		eventService.registerSync(this);
	}

	@Override
	public void startupService() throws Exception {
		zookeeperService.startup();
		nodeManager.init(moduleName, this);
	}

	@Override
	public void shutdownService() throws Exception {
		zookeeperService.shutdown();
	}

	// 创建代理节点
	private void createZKPrx() {
		AFunctionNode node = nodeManager.getRootNode();
		node.zk2localCreate();
	}

	// 注册服务数据
	private void createServiceData(AFunctionNode functionNode) {
		try {
			LeafNodeLocalService node = (LeafNodeLocalService) functionNode;
			node.setZLNodeData();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void register(IFunctionInfo functionInfo) {
		// 建立本地节点
		FunctionId fid = functionInfo.getFunctionId();

		NodePath nodePath = NodePath.createNodePath(moduleName, fid, false);
		InitNodeContext nc = new InitNodeContext(nodePath, functionInfo);
		initLocalNode(nc);

		// 注册空服务
		String key = NodePath.getFKeyFullPath(moduleName, fid, true);
		nodePath.setId(fid);
		nc.reset();

		nodeContextMap.put(key, nc);
		AFunctionNode functionNode = registerC(nc);

		// 创建代理节点
		createZKPrx();

		// 注册服务数据
		createServiceData(functionNode);
	}

	private AFunctionNode initLocalNode(InitNodeContext nc) {
		try {
			// 根据功能创建节点
			AFunctionNode node = nodeManager.getRootNode();
			node.initLocalNode(nc);
			return node;
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	private AFunctionNode registerC(InitNodeContext nc) {
		try {
			// 根据功能创建节点
			AFunctionNode node = nodeManager.getRootNode();
			node = node.initNode(nc);
			return node;
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	/**
	 * 注销本地功能服务
	 * 
	 * @param fid
	 * @return
	 */
	@Override
	public void unregister(FunctionId fid) {
		String key = NodePath.getFKeyFullPath(moduleName, fid, false);
		InitNodeContext nc = nodeContextMap.remove(key);
		if (nc == null) {
			return;
		}
		NodePath nodePath = NodePath.createNodePath(moduleName, fid, true);
		AFunctionNode node = nodeManager.selectNode(nodePath);
		if (node == null) {
			return;
		}
		try {
			// 移除本服应用节点
			node.local2zkRemove();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public IFunctionCluster getFunctionCluster(String groupId, String regionId, FunctionType functionType) {
		AFunctionNode tnode = nodeManager.selectNode(groupId, regionId, functionType);
		if (tnode == null) {
			return null;
		}
		return (LevelLeafMgrNode) tnode;
	}

	@Override
	public IFunctionCluster getFunctionCluster(FunctionId functionId) {
		return getFunctionCluster(functionId.getGroupId(), functionId.getRegionId(), functionId.getFunctionType());
	}

	@Override
	public IFunctionInfo getFunctionInfo(FunctionId functionId) {
		IFunctionCluster functionCluster = getFunctionCluster(functionId);
		if (functionCluster == null) {
			return null;
		}
		String fid = functionId.getFid(true);
		return functionCluster.getFunctionInfo(fid);
	}

	@Override
	public IFunctionInfo getFunctionInfo(String fid) {
		FunctionId functionId = FunctionId.createFunctionId(fid);
		IFunctionCluster functionCluster = getFunctionCluster(functionId);
		if (functionCluster == null) {
			return null;
		}
		return functionCluster.getFunctionInfo(fid);
	}

	@Override
	public List<IFunctionInfo> getRemoteFunctionInfos(FunctionType functionType) {
		IFunctionServiceConfig fsc = getFunctionServiceConfig();
		IFunctionCluster functionCluster = getFunctionCluster(fsc.getGroupId(), fsc.getRegionId(), functionType);
		return functionCluster.getRemoteFunctionInfos();
	}

	@Override
	public IFunctionInfo getRandomFunctionInfo(FunctionType functionType) {
		IFunctionServiceConfig fsc = getFunctionServiceConfig();
		IFunctionCluster functionCluster = getFunctionCluster(fsc.getGroupId(), fsc.getRegionId(), functionType);
		return functionCluster.getRandomFunctionInfo();
	}

	// -----------------------------------------
	@Override
	public IFunctionInfo getFunctionInfoAndCreatePrxNode(String fid) {
		try {
			FunctionId functionId = FunctionId.createFunctionId(fid);
			FunctionInfo functionInfo = new FunctionInfo(functionId);

			NodePath nodePath = NodePath.createNodePath(moduleName, functionId, true);
			InitNodeContext nc = new InitNodeContext(nodePath, functionInfo);
			AFunctionNode node = nodeManager.getRootNode();
			// 并创建代理节点
			node.initLocalPrxNode(nc);
			return nc.getFunctionInfo();
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	@Override
	public IFunctionInfo getLocalFunctionInfo() {
		IFunctionServiceConfig fsc = serviceConfig.getFunctionServiceConfig();
		IFunctionCluster functionCluster = getFunctionCluster(fsc.getGroupId(), fsc.getRegionId(),
				fsc.getFunctionType());
		if (functionCluster == null) {
			return null;
		}
		return functionCluster.getLocalFunctionInfo();
	}

	// ------------------------------------------------
	/**
	 * zk状态变更事件
	 * 
	 * @param event
	 */
	@Subscribe
	public void listen(WatchedEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("EventListener WatchedEvent event=" + event);
		}
		try {
			String path = event.getPath();
			if (path == null) {
				return;
			}
			NodePath e = new NodePath(path);

			AFunctionNode node = nodeManager.selectNode(e);
			if (node == null) {
				if (logger.isWarnEnabled()) {
					logger.warn("No answer path=[" + e.toString() + "!]");
				}
				return;
			}
			switch (event.getType()) {
			case None:
				if (logger.isDebugEnabled()) {
					logger.debug("ZKEvent None path=" + path);
				}
				node.none();
				break;
			case NodeCreated:
				if (logger.isDebugEnabled()) {
					logger.debug("ZKEvent NodeCreated path=" + path);
				}
				node.nodeCreated();
				break;
			case NodeDeleted:
				if (logger.isDebugEnabled()) {
					logger.debug("ZKEvent NodeDeleted path=" + path);
				}
				node.nodeDeleted();
				break;
			case NodeDataChanged:
				if (logger.isDebugEnabled()) {
					logger.debug("ZKEvent NodeDataChanged path=" + path);
				}
				node.nodeDataChanged();
				break;
			case NodeChildrenChanged:
				if (logger.isDebugEnabled()) {
					logger.debug("ZKEvent NodeChildrenChanged path=" + path);
				}
				node.nodeChildrenChanged();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	// @Subscribe
	public void listen(MasterChangeEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("EventListener MasterChangeEvent event=" + event);
		}
		// if (event.isMaster()) {
		// IFunctionContext context = event.getFunctionContext();
		// IMemberService memberService = context.getMemberService();
		// if (event.getProMasterKey() != null) {
		// memberService.removeAllMemberNetsite(event.getProMasterKey());
		// }
		// }
	}

	// @Subscribe
	public void listen(NodeDeleteEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("EventListener NodeDeleteEvent event=" + event);
		}
		// if (event.isMaster()) {
		// IFunctionContext context = event.getFunctionContext();
		// IMemberService memberService = context.getMemberService();
		// // 如果是主
		// memberService.removeAllMemberNetsite(event.getFunctionId());
		// }
	}

	// ------------------------------------------------
	@Override
	public NodeFilter getNodeFilter() {
		return nodeFilter;
	}

	@Override
	public IMemberService getMemberService() {
		return memberService;
	}

	@Override
	public IZookeeperService getZookeeperService() {
		return zookeeperService;
	}

	public IRpcServicePrx getRpcServicePrx() {
		return rpcServicePrx;
	}

	@Override
	public INetClientService getNetClientService() {
		return netClientService;
	}

	@Override
	public IEventService getEventService() {
		return eventService;
	}
}
