package com.elex.common.component.prototype;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.elex.common.component.cache.ICacheService;
import com.elex.common.component.cache.type.CacheInteractionType;
import com.elex.common.component.clsloader.ServerClassLoader;
import com.elex.common.component.data.IDao;
import com.elex.common.component.data.ISpread;
import com.elex.common.component.data.type.TableConfigType;
import com.elex.common.component.prototype.config.ScPrototype;
import com.elex.common.component.prototype.type.PrototypeFileType;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceType;
import com.elex.common.util.file.FileUtil;
import com.elex.common.util.json.JsonUtil;

import java.util.Set;

/**
 * 静态数据客户端服务
 * 
 * @author mausmars
 *
 */
public class PrototypeService extends AbstractService<ScPrototype> implements IPrototypeService {
	// {entity_name:IDao}不要有相同的实体类名
	private Map<Class<?>, IDao<Object>> daoMap = new HashMap<>();

	private String LevelLocalCacheDao_Template = "%s.%s.custom.%sEHCacheCustomDao";
	private String Domain_Template = "%s.%s.domain.%s";

	private String JsonFilePath_Template = "%s/%s";
	private String VersionFile_Template = "version";
	private String JsonFile_Template = "%s/%s.json";

	private Map<CacheInteractionType, ICacheService> cacheServiceMap = new HashMap<CacheInteractionType, ICacheService>();

	public PrototypeService(IServiceConfig serviceConfig, IGlobalContext context) {
		super(serviceConfig, context);
	}

	@Override
	public void initService() throws Exception {
		ScPrototype sc = getSConfig();

		// 获取依赖的缓存服务
		List<String> redisSids = sc.getDependIdsMap().get(ServiceType.cache.name());
		for (String redisSid : redisSids) {
			ICacheService cacheService = getServiceManager().getService(ServiceType.cache, redisSid);
			cacheServiceMap.put(cacheService.getCacheInteractionType(), cacheService);
		}

		String rid = serviceConfig.getFunctionServiceConfig().getRegionId();
		String jsonPath = String.format(JsonFilePath_Template, sc.getPath(), rid);

		File jsonFilePath = new File(jsonPath);
		if (!jsonFilePath.exists()) {
			// 创建目录
			jsonFilePath.mkdirs();
		}
		File versionFilePath = new File(jsonPath + File.pathSeparator + VersionFile_Template);
		if (!versionFilePath.exists()) {
			// 创建文件
			versionFilePath.createNewFile();
		}

		// 创建dao
		for (Entry<String, List<String>> entry : sc.getTableConfigsMap().entrySet()) {
			String pojoName = entry.getKey();
			String daoPackage = entry.getValue().get(TableConfigType.Package.value());
			// 用实体全名做key
			String domainClassName = String.format(Domain_Template, daoPackage, pojoName.toLowerCase(), pojoName);

			// 初始化数据库层dao
			IDao<Object> databaseLevelDao = initLocalCacheLevelDao(daoPackage, pojoName);
			// 加载数据
			Class<?> domainClass = (Class<?>) ServerClassLoader.getClass(domainClassName);
			// 加入dao
			daoMap.put(domainClass, databaseLevelDao);
		}

		Set<Class<?>> loadedClass = new HashSet<Class<?>>();

		// 加载特殊类型数据
		Map<String, List<String>> extraParamsMap = sc.getExtraParamsMap();
		for (Entry<String, List<String>> entry : extraParamsMap.entrySet()) {
			PrototypeFileType type = PrototypeFileType.valueOf(entry.getKey());
			switch (type) {
			case WorldMap:
				List<String> values = entry.getValue();
				String file = values.get(0);
				String className = values.get(1);

				Class<IDataLoader> loaderClass = (Class<IDataLoader>) ServerClassLoader.getClass(className);
				IDataLoader loader = loaderClass.newInstance();// 初始化dao

				// 加载数据
				Class<?>[] clses = loader.load(jsonPath, sc, daoMap, file);
				for (Class<?> cls : clses) {
					// 记录加载过的class
					loadedClass.add(cls);
				}
				break;
			default:
				break;
			}
		}

		// 加载数据
		boolean isSuccess = true;
		for (Entry<Class<?>, IDao<Object>> entry : daoMap.entrySet()) {
			Class<?> domainClass = entry.getKey();
			if (loadedClass.contains(domainClass)) {
				continue;
			}
			IDao<Object> databaseLevelDao = (IDao<Object>) entry.getValue();
			// 插入dao
			if (!insertDao(jsonPath, domainClass, databaseLevelDao)) {
				isSuccess = false;
			}
		}
		if (!isSuccess) {
			throw new RuntimeException("Prototype init is error!");
		} else {
			logger.info("Prototype init is success!");
		}
	}

	@Override
	public void startupService() throws Exception {
	}

	@Override
	public void shutdownService() throws Exception {
	}

	private boolean insertDao(String jsonPath, Class<?> domainClass, IDao<Object> databaseLevelDao) {
		String pojoName = domainClass.getSimpleName();

		String jsonFileName = String.format(JsonFile_Template, jsonPath, pojoName);

		int row = 1;
		String lc = "";
		try {
			File jsonFile = new File(jsonFileName);
			if (!jsonFile.exists()) {
				// 文件不存在
				throw new RuntimeException("Prototype file no exists! pojoName=" + pojoName);
			}
			List<String> lineContents = FileUtil.readFileLineContent(jsonFile);
			for (String lineContent : lineContents) {
				lc = lineContent;
				Object pojo = JsonUtil.transferJsonTOJavaBean(lineContent, domainClass);
				ISpread spread = (ISpread) pojo;
				spread.saveBefore();
				databaseLevelDao.insert(pojo, null);
				row++;
			}
		} catch (Exception ex) {
			logger.error("PojoName=" + pojoName + ",row=" + row + ", content=" + lc, ex);
			return false;
		}
		return true;
	}

	private IDao<Object> initLocalCacheLevelDao(String daoPackage, String pojoName) throws Exception {
		String daoClassName = String.format(LevelLocalCacheDao_Template, daoPackage, pojoName.toLowerCase(), pojoName);
		Class<IDao<Object>> daoClass = (Class<IDao<Object>>) ServerClassLoader.getClass(daoClassName);

		ICacheService cacheService = cacheServiceMap.get(CacheInteractionType.Local);

		IDao<Object> dao = daoClass.newInstance();// 初始化dao

		Method createCacheConfigMethod = daoClass.getSuperclass().getDeclaredMethod("createCacheConfig", null);
		Object result = createCacheConfigMethod.invoke(dao, null);// 创建缓存配置

		Object cache = cacheService.createCache(result);// 创建缓存

		Method setCacheMethod = daoClass.getSuperclass().getDeclaredMethod("setCache", Object.class);
		setCacheMethod.invoke(dao, cache);
		return dao;
	}

	// 初始化数据库层dao
	// private IDao<Object> initDatabaseLevelDao(String daoPackage, String
	// pojoName) throws Exception {
	// String databaseLevelDaoClassName =
	// String.format(LevelPrototypeDao_Template, daoPackage,
	// pojoName.toLowerCase(),
	// pojoName);
	// Class<IDao<Object>> databaseLevelDaoClass = (Class<IDao<Object>>)
	// ServerClassLoader
	// .getClass(databaseLevelDaoClassName);
	// IDao<Object> databaseLevelDao = databaseLevelDaoClass.newInstance();
	// return databaseLevelDao;
	// }

	@Override
	public <T extends IDao<?>> T getPrototype(Class<?> cls) {
		return (T) daoMap.get(cls);
	}
}
