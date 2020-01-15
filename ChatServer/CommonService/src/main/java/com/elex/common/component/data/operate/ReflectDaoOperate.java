package com.elex.common.component.data.operate;

import java.lang.reflect.Method;
import java.util.Map;

import com.elex.common.component.data.DataConstant;
import com.elex.common.component.data.IDao;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 利用反射调用dao方法
 * 
 * @author Administrator
 * 
 */
public class ReflectDaoOperate implements IDaoOperate {
	protected static final ILogger logger = XLogUtil.logger();

	private IDao<?> dao;// dao
	private String methdoName;// 方法名字
	private Object[] params;// 参数
	private Class<?>[] paramTypes;// 参数类型
	private IDaoCallBack daoCallBack;// 回调接口

	private String key;
	private String groupKey;

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Object execute(Object attach) {
		Object resulte = null;
		try {
			Class<? extends IDao> daoClass = dao.getClass();

			Method method = daoClass.getMethod(methdoName, paramTypes);
			method.setAccessible(true);
			// 参数

			if (logger.isDebugEnabled()) {
				logger.debug(
						"DaoOperate execute! ClassName=" + daoClass.getSimpleName() + ", methdoName=" + methdoName);
			}

			Object lastParam = params[params.length - 1];
			if ((lastParam instanceof Map) && attach != null) {
				Map<String, Object> map = (Map<String, Object>) lastParam;
				map.put(DataConstant.SQLSessionKey, attach);
			}

			resulte = method.invoke(dao, params); // 自动装箱
			if (daoCallBack != null) {
				// 回调接口
				daoCallBack.executeOver(resulte);
			}
			return resulte;
		} catch (Exception e) {
			logger.error("", e);
			if (daoCallBack != null) {
				// 回调接口
				daoCallBack.excuteException(e);
			}
		}
		return resulte;
	}

	@Override
	public boolean canMerge() {
		return true;
	}

	@Override
	public void merge(IDaoOperate daoOperate) {
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	public void setDao(IDao<?> dao) {
		this.dao = dao;
	}

	public void setMethdoName(String methdoName) {
		this.methdoName = methdoName;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public void setDaoCallBack(IDaoCallBack daoCallBack) {
		this.daoCallBack = daoCallBack;
	}

	public void setParamTypes(Class<?>[] paramTypes) {
		this.paramTypes = paramTypes;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
