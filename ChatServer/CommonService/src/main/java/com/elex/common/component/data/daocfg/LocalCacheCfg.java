package com.elex.common.component.data.daocfg;

import com.elex.common.component.data.IDao;

/**
 * 数据库配置
 * 
 * @author mausmars
 *
 */
public class LocalCacheCfg {
	private boolean isAvailable;// 是否可用

	private IDao dao;// dao

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public IDao getDao() {
		return dao;
	}

	public void setDao(IDao dao) {
		this.dao = dao;
	}
}
