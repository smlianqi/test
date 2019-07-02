package com.elex.common.component.rpc.ice;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.elex.common.component.clsloader.ServerClassLoader;
import com.elex.common.component.function.info.FService;
import com.elex.common.component.function.info.FunctionInfo;
import com.elex.common.component.function.info.IceRpcServiceInfo;
import com.elex.common.component.function.info.ServiceAddress;
import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.rpc.IRpcFServicePrx;
import com.elex.common.component.rpc.IRpcService;
import com.elex.common.component.rpc.RpcConstant;
import com.elex.common.component.rpc.config.RpcCustomConfig;
import com.elex.common.component.rpc.config.ScRpc;
import com.elex.common.component.rpc.type.InterfaceIndexType;
import com.elex.common.net.type.NetProtocolType;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.util.netaddress.NetUtil;

import java.util.Set;

import Ice.Identity;
import Ice.ObjectAdapter;
import Ice.ObjectFactory;
import Ice.ObjectImpl;

/**
 * ice rpc服务
 * 
 * @author mausmars
 *
 */
public class IceRpcService extends AbstractService<ScRpc> implements IRpcService {
	private Ice.Communicator communicator;

	// 配置文件路径
	private static final String IceConfig_Template = "--Ice.Config=%s";
	private static final String IceUrl_Template = "%s -h %s -p %d";

	// ice类路径模板
	private final String IceClassName_Template = "com.elex.im.module.server%s.rpcservice.%sIce";

	// {适配器名:适配器}
	private Map<String, ObjectAdapter> objectAdapterMap = new HashMap<String, ObjectAdapter>();

	// -----------------------------------

	public IceRpcService(IServiceConfig serviceConfig, IGlobalContext context, ICustomConfig customConfig) {
		super(serviceConfig, context);
		this.customConfig = customConfig;
	}

	private RpcCustomConfig getRpcCustomConfig() {
		return (RpcCustomConfig) customConfig;
	}

	@Override
	public void initService() throws Exception {
		ScRpc c = getSConfig();

		// 初始化ice服务
		String configPath = String.format(IceConfig_Template, c.getExtraParamsMap().get(RpcConstant.IceConfigPathKey));
		communicator = Ice.Util.initialize(new String[] { configPath });
		// 初始化对象工厂
		Map<String, ObjectFactory> objectFactoryMap = getRpcCustomConfig().getObjectFactoryMap();
		if (objectFactoryMap != null && !objectFactoryMap.isEmpty()) {
			Set<Entry<String, ObjectFactory>> factorySet = objectFactoryMap.entrySet();
			for (Entry<String, ObjectFactory> entry : factorySet) {
				communicator.addObjectFactory(entry.getValue(), entry.getKey());
			}
		}
		NetProtocolType netProtocol = NetProtocolType.valueOf((c.getNetProtocolType()));

		String outsideNetIp = NetUtil.getOutsideNetIp(c.getHost());

		// 验证端口是否可用
		int port = NetUtil.getAvailablePort(outsideNetIp, c.getPort());
		c.setPort(port);
		c.setHost(outsideNetIp);// 最终host修改

		String url = String.format(IceUrl_Template, netProtocol.name(), outsideNetIp, c.getPort());

		// 创建适配器
		String adapterName = c.getExtraParamsMap().get(RpcConstant.IceAdapterNameKey);
		Ice.ObjectAdapter adapter = getObjectAdapterOrCreate(adapterName, url);

		// [包名，接口名，对象名]
		for (List<String> interfaceInfos : c.getInterfaceInfosList()) {
			String className = interfaceInfos.get(InterfaceIndexType.ClassName.ordinal());
			String objectId = interfaceInfos.get(InterfaceIndexType.ObjectId.ordinal());

			// 服务注册到适配器
			Identity id = communicator.stringToIdentity(objectId);
			if (adapter.find(id) != null) {
				continue;
			}
			// ice类包装本地服务
			IRpcFServicePrx localService = getRpcCustomConfig().getLocalService(objectId);

			FunctionType functionType = serviceConfig.getFunctionServiceConfig().getFunctionType();
			String iceClassName = String.format(IceClassName_Template, functionType.name(), className.substring(1));
			Class<ObjectImpl> rpcIceServiceCls = (Class<ObjectImpl>) ServerClassLoader.getClass(iceClassName);

			Constructor<ObjectImpl> con = rpcIceServiceCls.getConstructor(IRpcFServicePrx.class);
			// 创建ice服务类
			ObjectImpl service = con.newInstance(localService);

			adapter.add(service, id);
			adapter.activate();
			// 服务加到功能菜单中
			if (logger.isInfoEnabled()) {
				logger.info(
						"[service activate AdapterName=" + adapterName + " objectId=" + objectId + " url=" + url + "]");
			}
		}
	}

	@Override
	public void startupService() throws Exception {
		// 启动ice
		Thread thread = new Thread() {
			public void run() {
				communicator.waitForShutdown();
			}
		};
		thread.setName("StartIceServiceThread");
		thread.start();
	}

	@Override
	public void shutdownService() throws Exception {
		communicator.destroy();
		if (logger.isInfoEnabled()) {
			logger.info("stop Ice service over!");
		}
	}

	@Override
	public void registerService(FunctionInfo functionInfo) {
		ScRpc c = getSConfig();

		NetProtocolType netProtocolType = NetProtocolType.valueOf(c.getNetProtocolType());
		ServiceAddress serviceAddress = new ServiceAddress(c.getHost(), c.getPort(), netProtocolType);

		// 适配器名
		String adapterName = c.getExtraParamsMap().get(RpcConstant.IceAdapterNameKey);

		// TODO key
		FService service = new FService(FServiceType.IceRpc, serviceAddress);
		// [包名，接口名，对象名]
		for (List<String> interfaceInfos : c.getInterfaceInfosList()) {
			String packageName = interfaceInfos.get(InterfaceIndexType.PackageName.ordinal());
			String className = interfaceInfos.get(InterfaceIndexType.ClassName.ordinal());
			String objectId = interfaceInfos.get(InterfaceIndexType.ObjectId.ordinal());

			IceRpcServiceInfo serviceInfo = new IceRpcServiceInfo(packageName, className, objectId, adapterName);

			IRpcFServicePrx rpcFServicePrx = getRpcCustomConfig().getLocalService(objectId);
			serviceInfo.setServicePrx(rpcFServicePrx);// 绑定本地服务
			service.putServiceInfo(className, serviceInfo);
		}
		// 加入功能信息中
		functionInfo.putFService(service);
	}

	private ObjectAdapter getObjectAdapterOrCreate(String adapterName, String url) {
		if (objectAdapterMap.containsKey(adapterName)) {
			return objectAdapterMap.get(adapterName);
		} else {
			Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints(adapterName, url);
			objectAdapterMap.put(adapterName, adapter);
			return adapter;
		}
	}
}
