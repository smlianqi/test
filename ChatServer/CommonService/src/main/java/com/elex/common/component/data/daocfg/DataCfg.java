package com.elex.common.component.data.daocfg;

public class DataCfg {
	private String pojoName; // 类名
	private String packageName;// 包名

	private DatabaseCfg database;// 数据库配置
	private LocalCacheCfg localcache; // 本地缓存配置
	private RemoteCacheCfg remotecache;// 远端缓存配置

	private String daoGroupType;// dao组类型

	/**
	 * db是否异步
	 * 
	 * @return
	 */
	public boolean isDatabaseAsync() {
		if (database == null || !database.isAsync()) {
			return false;
		}

		if ((localcache == null || !localcache.isAvailable()) && (remotecache == null || !remotecache.isAvailable())) {
			// 如果没有缓存就不用异步
			return false;
		}

		if (database.getDaoOperateService() == null) {
			return false;
		}

		return true;
	}

	public boolean isDatabaseAvailable() {
		return database != null && database.isAvailable();
	}

	public boolean isLocalcacheAvailable() {
		return localcache != null && localcache.isAvailable();
	}

	public boolean isRemotecacheAvailable() {
		return remotecache != null && remotecache.isAvailable();
	}

	public String getPojoName() {
		return pojoName;
	}

	public void setPojoName(String pojoName) {
		this.pojoName = pojoName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public DatabaseCfg getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseCfg database) {
		this.database = database;
	}

	public LocalCacheCfg getLocalcache() {
		return localcache;
	}

	public void setLocalcache(LocalCacheCfg localcache) {
		this.localcache = localcache;
	}

	public RemoteCacheCfg getRemotecache() {
		return remotecache;
	}

	public void setRemotecache(RemoteCacheCfg remotecache) {
		this.remotecache = remotecache;
	}

	public String getDaoGroupType() {
		return daoGroupType;
	}

	public void setDaoGroupType(String daoGroupType) {
		this.daoGroupType = daoGroupType;
	}
}
