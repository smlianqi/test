package com.elex.common.service.configloader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.elex.common.component.cache.config.CacheServiceConfig;
import com.elex.common.component.data.config.DataServiceConfig;
import com.elex.common.component.database.config.DatabaseServiceConfig;
import com.elex.common.component.event.config.EventServiceConfig;
import com.elex.common.component.function.config.FunctionServiceConfig;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.ignite.config.IgniteServiceConfig;
import com.elex.common.component.lock.config.LockServiceConfig;
import com.elex.common.component.member.config.MemberServiceConfig;
import com.elex.common.component.net.config.ChannelOptionConfig;
import com.elex.common.component.net.config.NetClientServiceConfig;
import com.elex.common.component.net.config.NetServerServiceConfig;
import com.elex.common.component.prototype.config.PrototypeServiceConfig;
import com.elex.common.component.script.config.ScriptServiceConfig;
import com.elex.common.component.task.config.TaskServiceConfig;
import com.elex.common.component.threadbox.config.ThreadBoxServiceConfig;
import com.elex.common.component.threadpool.config.ThreadpoolServiceConfig;
import com.elex.common.component.timeout.config.TimeoutServiceConfig;
import com.elex.common.component.zookeeper.config.ZookeeperServiceConfig;
import com.elex.common.service.GeneralConstant;
import com.elex.common.service.config.FunctionInfoServiceConfig;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.config.ProvidingFunction;
import com.elex.common.service.type.ServiceType;
import com.elex.common.util.file.FileUtil;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 本地文件loader
 * 
 * @author mausmars
 *
 */
public class LocalServiceConfigLoader implements IServiceConfigLoader {
	protected static final ILogger logger = XLogUtil.logger();

	private Map<ServiceType, ICustomConfig> customConfigMap = new HashMap<>();

	private String FunctionInfoPath_Template = "propertiesconfig/%s/service/functioninfo_%s.properties";
	// (serviceType_serviceId)
	private String ServiceConfigPath_Template = "propertiesconfig/%s/service/%s_%s.properties";

	// 运维配置路径
	private String OperationServiceConfigPath_Template = "operationconfig/%s_config.properties";

	@Override
	public ProvidingFunction loadOperationConfig(FunctionType functionType) {
		String operationServiceConfigPath = String.format(OperationServiceConfigPath_Template, functionType.name());

		File file = new File(operationServiceConfigPath);
		if (file.exists()) {
			ServiceFileConfig sf = getProperties(file);
			Properties properties = sf.getProperties();
			return new ProvidingFunction(functionType, properties);
		}
		return null;
	}

	@Override
	public IFunctionServiceConfig loadConfigFSC(ProvidingFunction providingFunction) {
		String ft = providingFunction.getFunctionType().name().toLowerCase();
		String functionInfoPath = String.format(FunctionInfoPath_Template, ft, ft);

		ServiceFileConfig sf = getProperties(functionInfoPath);
		return new FunctionInfoServiceConfig(sf, providingFunction);
	}

	@Override
	public IServiceConfig loadConfigSC(ServiceType serviceType, String serviceId,
			IFunctionServiceConfig functionServiceConfig) {
		String serviceConfigPath = String.format(ServiceConfigPath_Template,
				functionServiceConfig.getFunctionType().name().toLowerCase(), serviceType.name().toLowerCase(),
				serviceId);

		ServiceFileConfig serviceFileConfig = getProperties(serviceConfigPath);

		IServiceConfig serviceConfig = null;
		switch (serviceType) {
		case cache:
			serviceConfig = new CacheServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case zookeeper:
			serviceConfig = new ZookeeperServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case database:
			serviceConfig = new DatabaseServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case threadpool:
			serviceConfig = new ThreadpoolServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case threadbox:
			serviceConfig = new ThreadBoxServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case objectpool:
			break;
		case timeout:
			serviceConfig = new TimeoutServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case netclient:
			NetClientServiceConfig netClientServiceConfig = new NetClientServiceConfig(serviceFileConfig,
					functionServiceConfig);
			String netClientCOId = netClientServiceConfig.getExtraParamsMap();
			if (netClientCOId != null) {
				String ft = functionServiceConfig.getFunctionType().name();
				String channeloptionConfigPath = String.format(ServiceConfigPath_Template, ft,
						GeneralConstant.ChannelOptionKey, netClientCOId);
				ServiceFileConfig sf = getProperties(channeloptionConfigPath);
				ChannelOptionConfig channelOptionConfig = new ChannelOptionConfig(sf);
				netClientServiceConfig.putChannelOption(channelOptionConfig.getChannelOption());
			}
			serviceConfig = netClientServiceConfig;
			break;
		case netserver:
			NetServerServiceConfig netServerServiceConfig = new NetServerServiceConfig(serviceFileConfig,
					functionServiceConfig);
			String netServerCOId = netServerServiceConfig.getExtraParamsMap();
			if (netServerCOId != null) {
				String ft = functionServiceConfig.getFunctionType().name();
				String channeloptionConfigPath = String.format(ServiceConfigPath_Template, ft,
						GeneralConstant.ChannelOptionKey, netServerCOId);
				ServiceFileConfig sf = getProperties(channeloptionConfigPath);
				ChannelOptionConfig channelOptionConfig = new ChannelOptionConfig(sf);
				netServerServiceConfig.putChannelOption(channelOptionConfig.getChannelOption());
			}
			serviceConfig = netServerServiceConfig;

			break;
		case rpc:
			serviceConfig = new com.elex.common.component.rpc.config.RpcServiceConfig(serviceFileConfig,
					functionServiceConfig);
			break;
		case function:
			serviceConfig = new FunctionServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case data:
			serviceConfig = new DataServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case member:
			serviceConfig = new MemberServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case lock:
			serviceConfig = new LockServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case task:
			serviceConfig = new TaskServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case prototype:
			serviceConfig = new PrototypeServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case script:
			serviceConfig = new ScriptServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case ignite:
			serviceConfig = new IgniteServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		case event:
			serviceConfig = new EventServiceConfig(serviceFileConfig, functionServiceConfig);
			break;
		default:
			break;
		}
		return serviceConfig;
	}

	@Override
	public IServiceConfig loadConfigSC(String serviceType, String serviceId, IFunctionServiceConfig fsc) {
		return loadConfigSC(ServiceType.valueOf(serviceType), serviceId, fsc);
	}

	private ServiceFileConfig getProperties(String filePath) {
		return getProperties(new File(filePath));
	}

	private ServiceFileConfig getProperties(File file) {
		Properties p = FileUtil.createProperties(file);
		if (p == null) {
			return null;
		}
		// 去掉文件后缀
		String[] fileNames = file.getName().split("\\.");
		ServiceFileConfig serviceFileConfig = new ServiceFileConfig();
		serviceFileConfig.setFileName(fileNames[0]);
		serviceFileConfig.setProperties(p);
		return serviceFileConfig;
	}

	@Override
	public ICustomConfig getCustomConfig(ServiceType serviceType) {
		return customConfigMap.get(serviceType);
	}

	public void putCustomConfig(ICustomConfig customConfig) {
		for (ServiceType serviceType : customConfig.getServiceTypes()) {
			customConfigMap.put(serviceType, customConfig);
		}
	}
}
