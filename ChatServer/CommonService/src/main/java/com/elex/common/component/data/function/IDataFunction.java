package com.elex.common.component.data.function;

/**
 * 数据功能接口
 * 
 * @author mausmars
 *
 */
public interface IDataFunction {
	/**
	 * 数据是否已经初始化了
	 * 
	 * @return
	 */
	boolean isInited();

	/**
	 * 初始化数据
	 */
	boolean initData(boolean newRegister);
}
