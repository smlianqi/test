package com.elex.common.component.data;

import java.util.HashMap;
import java.util.Map;

import com.elex.common.component.data.daocfg.DataCfg;
import com.elex.common.component.database.ISqlSession;
import com.elex.common.util.string.StringUtil;

/**
 * dao工具包
 * 
 * @author mausmars
 *
 */
public class DaoKit {
	private IDao<?> fixedLevelDao;
	private DataCfg dataCfg;
	private IDataService dataService;

	public DaoKit(IDao<?> fixedLevelDao, DataCfg dataCfg, IDataService dataService) {
		this.fixedLevelDao = fixedLevelDao;
		this.dataCfg = dataCfg;
		this.dataService = dataService;
	}

	/**
	 * 获得参数
	 * 
	 * @return
	 */
	public Map<String, Object> getParams() {
		return getParams(true);
	}

	public <T extends IDao<?>> T getDao() {
		return (T) fixedLevelDao;
	}

	public Map<String, Object> getParams(ISqlSession sqlSession) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(DataConstant.SQLSessionKey, sqlSession.getSession());

		if (!dataCfg.getDatabase().isPrefix()) {
			params.put(DataConstant.TableNumberKey, "");
		} else {
			params.put(DataConstant.TableNumberKey, dataCfg.getDatabase().getPrefix() + StringUtil.SeparatorUnderline);
		}
		return params;
	}

	public Map<String, Object> getParams(boolean isClearCache) {
		Map<String, Object> params = new HashMap<String, Object>();

		if (dataCfg.isDatabaseAvailable()) {
			ISqlSession sqlSession = dataService.openSession(true);
			params.put(DataConstant.SQLSessionKey, sqlSession.getSession());
		}
		if (!dataCfg.getDatabase().isPrefix()) {
			params.put(DataConstant.TableNumberKey, "");
		} else {
			params.put(DataConstant.TableNumberKey, dataCfg.getDatabase().getPrefix() + StringUtil.SeparatorUnderline);
		}
		params.put(DataConstant.IsClearCacheKey, isClearCache);
		return params;
	}

	public Map<String, Object> getParamsUploadData() {
		Map<String, Object> params = getParams(true);
		params.put(DataConstant.IsUploadData, DataConstant.IsUploadData);
		return params;
	}

	public Map<String, Object> getParamsUnloadData() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(DataConstant.IsUnloadData, DataConstant.IsUnloadData);
		return params;
	}

	DataCfg getDataCfg() {
		return dataCfg;
	}
}
