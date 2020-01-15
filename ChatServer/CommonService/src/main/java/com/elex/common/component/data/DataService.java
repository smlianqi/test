package com.elex.common.component.data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;

import com.elex.common.component.cache.ICacheService;
import com.elex.common.component.cache.type.CacheInteractionType;
import com.elex.common.component.clsloader.ServerClassLoader;
import com.elex.common.component.data.cache.RedisCacheDao;
import com.elex.common.component.data.config.ScData;
import com.elex.common.component.data.daocfg.DataCfg;
import com.elex.common.component.data.operate.DaoOperateService;
import com.elex.common.component.data.operate.IDaoOperateService;
import com.elex.common.component.database.IDatabaseService;
import com.elex.common.component.database.ISqlSession;
import com.elex.common.component.threadbox.IThreadBoxService;
import com.elex.common.component.threadbox.ThreadBoxAttachType;
import com.elex.common.component.threadpool.IPoolExecutor;
import com.elex.common.component.threadpool.IThreadPoolService;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceType;
import com.elex.common.util.json.JsonUtil;
import com.google.gson.reflect.TypeToken;

/**
 * 数据服务
 * 
 * @author mausmars
 *
 */
public class DataService extends AbstractService<ScData> implements IDataService {
	// {entity_name:IDao}不要有相同的实体类名
	private Map<String, DaoKit> daoKitMap = new HashMap<String, DaoKit>();

	// package.小写pojo.pojoXXXXXDao
	private String LevelDatabaseDao_Template = "%s.%s.custom.%sMybatisGeneralCustomDao";
	private String LevelCacheDao_Template = "%s.%s.custom.%sCacheCustomDao";
	private String LevelFixedDao_Template = "%s.%s.custom.%sFixedCustomDao";
	private String LevelLocalCacheDao_Template = "%s.%s.custom.%sEHCacheCustomDao";

	private String Domain_Template = "%s.%s.domain.%s";
	// ---------------------------------
	// 缓存服务
	private Map<CacheInteractionType, ICacheService> cacheServiceMap = new HashMap<CacheInteractionType, ICacheService>();
	// 数据库服务
	private IDatabaseService databaseService;
	// 线程池服务
	private IThreadPoolService threadPoolService;
	// 线程box服务
	private IThreadBoxService threadBoxService;

	private RedisCacheDao remoteCacheDao;

	public DataService(IServiceConfig serviceConfig, IGlobalContext context) {
		super(serviceConfig, context);
	}

	@Override
	public void initService() throws Exception {
		ScData sc = getSConfig();

		// 获取依赖的数据库服务
		List<String> databaseSids = sc.getDependIdsMap().get(ServiceType.database.name());
		this.databaseService = getServiceManager().getService(ServiceType.database, databaseSids.get(0));

		// 获取依赖的缓存服务
		List<String> redisSids = sc.getDependIdsMap().get(ServiceType.cache.name());
		for (String redisSid : redisSids) {
			ICacheService cacheService = getServiceManager().getService(ServiceType.cache, redisSid);
			cacheServiceMap.put(cacheService.getCacheInteractionType(), cacheService);
		}

		// 异步数据库操作线程池
		List<String> threadpoolSids = sc.getDependIdsMap().get(ServiceType.threadpool.name());
		if (threadpoolSids != null) {
			threadPoolService = getServiceManager().getService(ServiceType.threadpool, threadpoolSids.get(0));
		}

		List<String> threadboxSids = sc.getDependIdsMap().get(ServiceType.threadbox.name());
		if (threadboxSids != null) {
			threadBoxService = getServiceManager().getService(ServiceType.threadbox, threadboxSids.get(0));
		}

		if (cacheServiceMap.containsKey(CacheInteractionType.Remote)) {
			this.remoteCacheDao = new RedisCacheDao();// redis
		}
		// 初始化daoKit
		initDaoKit(sc);

		// 创建数据库表
		createTable();
	}

	private void initDaoKit(ScData sc) throws Exception {
		String rid = serviceConfig.getFunctionServiceConfig().getRegionId();

		List<DataCfg> dataCfgs = JsonUtil.gsonString2Obj(sc.getTableConfigs(), new TypeToken<List<DataCfg>>() {
		}.getType());

		for (DataCfg dataCfg : dataCfgs) {
			if (dataCfg.isDatabaseAvailable()) {
				// 初始化数据库层dao
				IDao<?> databaseLevelDao = initDatabaseLevelDao(dataCfg);

				dataCfg.getDatabase().setDao(databaseLevelDao);
				if (dataCfg.getDatabase().isPrefix()) {
					dataCfg.getDatabase().setPrefix(rid);
				}
				dataCfg.getDatabase().setDataService(this);
			}
			if (dataCfg.isLocalcacheAvailable()) {
				// 本地缓存
				IDao<?> localCacheLevelDao = initLocalCacheLevelDao(dataCfg);
				dataCfg.getLocalcache().setDao(localCacheLevelDao);
			}
			if (dataCfg.isRemotecacheAvailable()) {
				dataCfg.getRemotecache().setRemoteCacheDao(remoteCacheDao);
			}

			// 初始化缓存层dao
			IDao<?> cacheLevelDao = initCacheLevelDao(dataCfg);
			// 初始化固定层dao
			IDao<?> fixedLevelDao = initFixedLevelDao(dataCfg, cacheLevelDao);

			// 用实体全名做key
			String domainClassName = String.format(Domain_Template, dataCfg.getPackageName(),
					dataCfg.getPojoName().toLowerCase(), dataCfg.getPojoName());

			DaoKit daoKit = new DaoKit(fixedLevelDao, dataCfg, this);
			daoKitMap.put(domainClassName, daoKit);
		}
	}

	private void createTable() {
		// 创建表
		ISqlSession sqlSession = databaseService.openSession();
		try {
			for (DaoKit daoKit : daoKitMap.values()) {
				DataCfg dataCfg = daoKit.getDataCfg();
				if (dataCfg.getDatabase().isAvailable() && dataCfg.getDatabase().isCreateTable()) {
					try {
						daoKit.getDao().createTable(daoKit.getParams(sqlSession));
					} catch (PersistenceException e) {
						if (e.getMessage().contains("already exists")) {
							// 如果是重复创建表就不打印错误了
							continue;
						}
					} catch (Exception e) {
						if (logger.isDebugEnabled()) {
							logger.debug("CreateTable is fail! pojoName=" + dataCfg.getPojoName(), e);
						}
					}
				}
			}
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	@Override
	public void startupService() throws Exception {
		this.databaseService.startup();

		if (threadPoolService != null) {
			threadPoolService.startup();
		}

		if (threadBoxService != null) {
			threadBoxService.startup();
		}

		for (ICacheService cacheService : cacheServiceMap.values()) {
			cacheService.startup();
		}

		ICacheService remoteCacheService = cacheServiceMap.get(CacheInteractionType.Remote);
		if (remoteCacheService != null) {
			Object remoteCache = remoteCacheService.createCache(null);
			this.remoteCacheDao.setCache(remoteCache);
		}
	}

	@Override
	public void shutdownService() throws Exception {
		// this.databaseService.shutdown();
		// this.redisService.shutdown();
	}

	@Override
	public IDaoOperateService getDaoOperateService() {
		IDaoOperateService daoOperateService = threadBoxService.fetch(ThreadBoxAttachType.DaoOperateService);
		if (daoOperateService == null) {
			daoOperateService = new DaoOperateService(this);
			threadBoxService.store(ThreadBoxAttachType.DaoOperateService, daoOperateService);
		}
		return daoOperateService;
	}

	@Override
	public void daoOperateExecute() {
		IDaoOperateService daoOperateService = threadBoxService.fetch(ThreadBoxAttachType.DaoOperateService);

		IPoolExecutor poolExecutor = null;
		if (threadPoolService != null) {
			poolExecutor = threadPoolService.getPoolExecutor();
		}
		if (daoOperateService != null) {
			daoOperateService.execute(poolExecutor);
		}
	}

	@Override
	public void closeThreadBoxSession() {
		ISqlSession sqlSession = threadBoxService.fetch(ThreadBoxAttachType.SqlSession);
		if (sqlSession != null) {
			sqlSession.close();
		}
	}

	@Override
	public ISqlSession openSession(boolean isThreadBox) {
		if (!isThreadBox) {
			return databaseService.openSession();
		}
		ISqlSession sqlSession = threadBoxService.fetch(ThreadBoxAttachType.SqlSession);
		if (sqlSession == null && isThreadBox) {
			sqlSession = databaseService.openSession();
			threadBoxService.store(ThreadBoxAttachType.SqlSession, sqlSession);
		}
		return sqlSession;
	}

	// 初始化数据库层dao
	private IDao<?> initDatabaseLevelDao(DataCfg dataCfg) throws Exception {
		String databaseLevelDaoClassName = String.format(LevelDatabaseDao_Template, dataCfg.getPackageName(),
				dataCfg.getPojoName().toLowerCase(), dataCfg.getPojoName());
		Class<IDao<?>> databaseLevelDaoClass = (Class<IDao<?>>) ServerClassLoader.getClass(databaseLevelDaoClassName);
		IDao<?> databaseLevelDao = databaseLevelDaoClass.newInstance();
		return databaseLevelDao;
	}

	private IDao<?> initLocalCacheLevelDao(DataCfg dataCfg) throws Exception {
		String daoClassName = String.format(LevelLocalCacheDao_Template, dataCfg.getPackageName(),
				dataCfg.getPojoName().toLowerCase(), dataCfg.getPojoName());
		Class<IDao<?>> daoClass = (Class<IDao<?>>) ServerClassLoader.getClass(daoClassName);

		IDao<?> dao = daoClass.newInstance();

		Method createCacheConfigMethod = daoClass.getSuperclass().getDeclaredMethod("createCacheConfig", null);
		Object result = createCacheConfigMethod.invoke(dao, null);// 创建缓存配置

		ICacheService cacheService = cacheServiceMap.get(CacheInteractionType.Local);
		Object cache = cacheService.createCache(result);// 创建缓存

		Method setCacheMethod = daoClass.getSuperclass().getDeclaredMethod("setCache", Object.class);// 设置缓存
		setCacheMethod.invoke(dao, cache);

		return dao;
	}

	// 初始化缓存层dao
	private IDao<?> initCacheLevelDao(DataCfg dataCfg) throws Exception {
		String cacheLevelDaoClassName = String.format(LevelCacheDao_Template, dataCfg.getPackageName(),
				dataCfg.getPojoName().toLowerCase(), dataCfg.getPojoName());
		Class<IDao<?>> cacheLevelDaoClass = (Class<IDao<?>>) ServerClassLoader.getClass(cacheLevelDaoClassName);
		// 初始化dao
		IDao<?> cacheLevelDao = cacheLevelDaoClass.getConstructor(DataCfg.class).newInstance(dataCfg);
		return cacheLevelDao;
	}

	// 初始化固定层dao
	private IDao<?> initFixedLevelDao(DataCfg dataCfg, IDao<?> cacheDao) throws Exception {
		String fixedLevelDaoClassName = String.format(LevelFixedDao_Template, dataCfg.getPackageName(),
				dataCfg.getPojoName().toLowerCase(), dataCfg.getPojoName());
		Class<IDao<?>> fixedLevelDaoClass = (Class<IDao<?>>) ServerClassLoader.getClass(fixedLevelDaoClassName);
		IDao<?> fixedLevelDao = fixedLevelDaoClass.newInstance();

		// 这里是父类的方法
		Method setDaoMethod = fixedLevelDaoClass.getSuperclass().getDeclaredMethod("setDao", IDao.class);
		setDaoMethod.invoke(fixedLevelDao, cacheDao);
		return fixedLevelDao;
	}

	@Override
	public DaoKit getDaoKit(String pojoClassName) {
		return daoKitMap.get(pojoClassName);
	}

	@Override
	public DaoKit getDaoKit(Class<?> pojoClass) {
		return getDaoKit(pojoClass.getCanonicalName());
	}
}
