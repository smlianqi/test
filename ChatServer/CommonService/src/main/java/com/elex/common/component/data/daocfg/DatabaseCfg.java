package com.elex.common.component.data.daocfg;

import com.elex.common.component.data.IDao;
import com.elex.common.component.data.IDataService;
import com.elex.common.component.data.operate.IDaoOperateService;

/**
 * 数据库配置
 * 
 * @author mausmars
 *
 */
public class DatabaseCfg {
	private boolean isAvailable;// 是否可用
	private boolean isAsync;// 是否异步
	private boolean isCreateTable;// 是否创建表
	private boolean isPrefix;// 表是否有前缀

	private String prefix;

	private IDao dao;// dao
	private IDataService dataService;

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public boolean isAsync() {
		return isAsync;
	}

	public void setAsync(boolean isAsync) {
		this.isAsync = isAsync;
	}

	public boolean isCreateTable() {
		return isCreateTable;
	}

	public void setCreateTable(boolean isCreateTable) {
		this.isCreateTable = isCreateTable;
	}

	public boolean isPrefix() {
		return isPrefix;
	}

	public void setPrefix(boolean isPrefix) {
		this.isPrefix = isPrefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public IDao getDao() {
		return dao;
	}

	public void setDao(IDao dao) {
		this.dao = dao;
	}

	public IDaoOperateService getDaoOperateService() {
		return dataService.getDaoOperateService();
	}

	public void setDataService(IDataService dataService) {
		this.dataService = dataService;
	}
}
