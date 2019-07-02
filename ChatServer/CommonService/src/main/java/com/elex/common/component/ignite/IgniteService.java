package com.elex.common.component.ignite;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.ignite.config.IDataRegionConfig;
import com.elex.common.component.ignite.config.IgniteCustomConfig;
import com.elex.common.component.ignite.config.ScIgnite;
import com.elex.common.component.ignite.type.DiscoveryType;
import com.elex.common.component.ignite.type.ExtraParamsType;
import com.elex.common.component.ignite.type.IgniteUserAttrType;
import com.elex.common.component.zookeeper.IZookeeperService;
import com.elex.common.component.zookeeper.config.ScZookeeper;
import com.elex.common.component.zookeeper.config.ZookeeperServiceConfig;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.ICustomConfig;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceType;
import com.elex.common.util.log.LoggerType;
import com.elex.common.util.netaddress.NetUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.*;
import org.apache.ignite.events.EventType;
import org.apache.ignite.logger.log4j2.Log4J2Logger;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.zk.TcpDiscoveryZookeeperIpFinder;
import org.apache.ignite.spi.eventstorage.memory.MemoryEventStorageSpi;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IgniteService extends AbstractService<ScIgnite> implements IIgniteService {
    protected Ignite ignite;

    public IgniteService(IServiceConfig serviceConfig, IGlobalContext globalContext, ICustomConfig customConfig) {
        super(serviceConfig, globalContext);
        this.customConfig = customConfig;
    }

    @Override
    public void initService() throws Exception {
        ScIgnite c = getSConfig();

        IgniteConfiguration cfg = new IgniteConfiguration();
        // 基础配置
        setBaseConfig(cfg, c);
        // 设置发现服务
        setDiscovery(cfg, c);
        // 设置事件
        setEvent(cfg);
        // 设置用户数据
        setUserAttr(cfg);
        // 设置内存
        setMemoryEventStorage(cfg);
        // 设置持久化
        setDataStorage(cfg, c);

        // Start Ignite node.
        this.ignite = Ignition.start(cfg);
		String dependencyServerType = null;
		if(getFunctionServiceConfig().getFunctionType().equals(FunctionType.gate)) {
			dependencyServerType = FunctionType.chat.name();
		}
//		else if(getFunctionServiceConfig().getFunctionType().equals(FunctionType.chat)) {
//			dependencyServerType = FunctionType.gate.name();
//		}
		if(dependencyServerType != null) {
			//等待chat或gate服务的ignite服务都启动起来
			while(this.ignite.cluster().forAttribute("serverType", dependencyServerType).nodes().size() <= 0) {
				Thread.sleep(500);
			}
		}
        ignite.cluster().active(true);
        System.err.println("#######################################################");
        System.err.println("#######################################################");
        System.err.println(getFunctionServiceConfig().getFunctionType() + "启动了!!!!!!!!");
        System.err.println("#######################################################");
        System.err.println("#######################################################");
        // Setting the baseline topology to a specific Ignite cluster topology version.
//		ignite.cluster().setBaselineTopology(0);
    }

    private static final String Permissions_Template = "%s:%s";
    private static final String PermissionsType = "digest";

    private void setDiscovery(IgniteConfiguration cfg, ScIgnite c) throws Exception {
        DiscoveryType discoveryType = DiscoveryType.valueOf(c.getDiscoveryType());

        switch (discoveryType) {
            case zookeeper: {
                // 设置zk发现
                String outsideNetIp = NetUtil.getOutsideNetIp(getSConfig().getHost());
                String rootPath = c.getExtraParamsMap().get(ExtraParamsType.RootPath.getKey());
                String serviceName = c.getExtraParamsMap().get(ExtraParamsType.ServiceName.getKey());

                ScZookeeper scZookeeper = getScZookeeper(c);
                CuratorFramework zkClient = CuratorFrameworkFactory.builder().retryPolicy(new RetryNTimes(0, 1000))
                        .connectionTimeoutMs(3000).sessionTimeoutMs(5000).connectString(scZookeeper.getHosts()).build();
                // 设置zookeeper权限
                // setZookeeperPermission(scZookeeper, zkClient);

                TcpDiscoveryZookeeperIpFinder ipFinder = new TcpDiscoveryZookeeperIpFinder();
                ipFinder.setCurator(zkClient);
                ipFinder.setAllowDuplicateRegistrations(false);// 允许重复注册
                ipFinder.setBasePath(rootPath);// 根节点名字
                ipFinder.setServiceName(serviceName);// 服务节点名字
                ipFinder.setRetryPolicy(new RetryNTimes(3, 3000));// 重试策略

                TcpDiscoverySpi spi = new TcpDiscoverySpi();
                spi.setIpFinder(ipFinder);
                spi.setLocalPort(c.getPort());
                spi.setLocalPortRange(100);
                spi.setLocalAddress(outsideNetIp);
                spi.setSocketTimeout(1000);
                spi.setNetworkTimeout(1000);
                spi.setStatisticsPrintFrequency(100);

                // 设置
                cfg.setDiscoverySpi(spi);
                break;
            }
            default:
                break;
        }
    }

    private void setZookeeperPermission(ScZookeeper scZookeeper, CuratorFramework zkClient) throws Exception {
        String permissionStr = null;
        List<String> permissionsParam = scZookeeper.getPermissionsList();
        if (permissionsParam != null && !permissionsParam.isEmpty()) {
            permissionStr = String.format(Permissions_Template, permissionsParam.get(0), permissionsParam.get(1));
        }
        Id id = new Id(PermissionsType, DigestAuthenticationProvider.generateDigest(permissionStr));

        List<ACL> permissions = new LinkedList<>();
        ACL acl = new ACL(ZooDefs.Perms.ALL, id);
        permissions.add(acl);
        zkClient.setACL().withACL(permissions);
    }

    private void setBaseConfig(IgniteConfiguration cfg, ScIgnite c) {
        cfg.setPeerClassLoadingEnabled(true);

        // TODO 保证就一个目录。存储目录id要唯一（暂时用-服务类型+ip+端口）
//		cfg.setConsistentId(UUID.randomUUID());
        cfg.setConsistentId("CodChat-" + getFunctionServiceConfig().getFunctionType());

        // 设置工作目录
        cfg.setWorkDirectory(c.getWorkDirectory());

//        //设置原子配置
        AtomicConfiguration atomicCfg = new AtomicConfiguration();
        atomicCfg.setCacheMode(CacheMode.PARTITIONED);
        atomicCfg.setBackups(1);
        cfg.setAtomicConfiguration(atomicCfg);

        // 是否为客户端模式
        cfg.setClientMode(c.getIsClient());

        LoggerType loggerType = logger.getLoggerType();
        switch (loggerType) {
            case Slf4j:
                setSlf4j(cfg);
                break;
            case Log4j2:
                setLog4j2(cfg);
                break;
            default:
                break;
        }
    }

    private void setSlf4j(IgniteConfiguration cfg) {
        cfg.setGridLogger(new Slf4jLogger(logger.getRawLogger()));
    }

    private void setLog4j2(IgniteConfiguration cfg) {
        String longPath = System.getProperty("log4j.configurationFile");
        URL url = null;
        if (longPath == null) {
            url = Thread.currentThread().getContextClassLoader().getResource("log4j2.xml");
        } else {
            File file = new File(longPath);
            try {
                url = file.toURI().toURL();
            } catch (MalformedURLException e) {
                logger.error("", e);
            }
        }
        try {
            // 设置日志
            cfg.setGridLogger(new Log4J2Logger(url));
        } catch (IgniteCheckedException e) {
            logger.error("", e);
        }
    }

    private ScZookeeper getScZookeeper(ScIgnite c) {
        ScZookeeper scZookeeper = null;

        List<String> zookeeperSids = c.getDependIdsMap().get(ServiceType.zookeeper.name());
        if (zookeeperSids != null) {
            IZookeeperService zookeeperService = getServiceManager().getService(ServiceType.zookeeper,
                    zookeeperSids.get(0));

            ZookeeperServiceConfig zookeeperServiceConfig = zookeeperService.getConfig();
            scZookeeper = zookeeperServiceConfig.getConfig();
        }
        return scZookeeper;
    }

    private void setMemoryEventStorage(IgniteConfiguration cfg) {
        MemoryEventStorageSpi evtSpi = new MemoryEventStorageSpi();

        cfg.setEventStorageSpi(evtSpi);
    }

    private void setEvent(IgniteConfiguration cfg) {
        // cfg.setIncludeEventTypes(EventType.EVT_TASK_STARTED,
        // EventType.EVT_TASK_FINISHED, EventType.EVT_TASK_FAILED,
        // EventType.EVT_TASK_TIMEDOUT, EventType.EVT_TASK_SESSION_ATTR_SET,
        // EventType.EVT_TASK_REDUCED,
        // EventType.EVT_CACHE_OBJECT_PUT, EventType.EVT_CACHE_OBJECT_READ,
        // EventType.EVT_CACHE_OBJECT_REMOVED);
        cfg.setIncludeEventTypes(EventType.EVTS_ALL);
    }

    private void setDataStorage(IgniteConfiguration cfg, ScIgnite c) {
        Boolean isisDataStorage = Boolean
                .parseBoolean(c.getExtraParamsMap().get(ExtraParamsType.DataStorageState.getKey()));
        if (!isisDataStorage) {
            return;
        }
        // 数据存储配置
        DataStorageConfiguration storageCfg = new DataStorageConfiguration();
        // Set WAL Mode. [FSYNC,LOG_ONLY,BACKGROUND,NONE]
        storageCfg.setWalMode(WALMode.LOG_ONLY); //  写到磁盘就返回
        storageCfg.setWriteThrottlingEnabled(true);

        if (customConfig != null) {
            IgniteCustomConfig cc = (IgniteCustomConfig) customConfig;
            IDataRegionConfig drc = cc.getDataRegionConfig();
            if (drc != null) {
                DataRegionConfiguration[] cs = drc.createDataRegionConfigurations(getSConfig());
                storageCfg.setDataRegionConfigurations(cs);
            }
        }
        // storageCfg.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
        // 持久化设置
        cfg.setDataStorageConfiguration(storageCfg);
    }

    private void setUserAttr(IgniteConfiguration cfg) {
        IFunctionServiceConfig functionServiceConfig = serviceConfig.getFunctionServiceConfig();
        Map<String, String> userAttrs = new HashMap<>();
        // 组-gid
        userAttrs.put(IgniteUserAttrType.group.name(), functionServiceConfig.getGroupId());
        // 区-rid
        userAttrs.put(IgniteUserAttrType.region.name(), functionServiceConfig.getRegionId());
        // 服务器类型chat 或 gate
        userAttrs.put("serverType", getFunctionServiceConfig().getFunctionType().name());
        // 类型-serverId
        userAttrs.put(functionServiceConfig.getFunctionType().name(), String.valueOf(globalContext.getServerId()));
        cfg.setUserAttributes(userAttrs);
    }

    @Override
    public void startupService() throws Exception {
    }

    @Override
    public void shutdownService() throws Exception {
        this.ignite.cluster().active(false);
    }

    @Override
    public Ignite getIgnite() {
        return ignite;
    }
}
