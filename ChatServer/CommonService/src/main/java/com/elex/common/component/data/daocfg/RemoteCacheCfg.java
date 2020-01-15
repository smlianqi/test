package com.elex.common.component.data.daocfg;

import com.elex.common.component.data.cache.ICacheDao;

public class RemoteCacheCfg {
	private boolean isAvailable;// 是否可用

	protected ICacheDao remoteCacheDao;

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public ICacheDao getRemoteCacheDao() {
		return remoteCacheDao;
	}

	public void setRemoteCacheDao(ICacheDao remoteCacheDao) {
		this.remoteCacheDao = remoteCacheDao;
	}
}
