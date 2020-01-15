package com.elex.common.component.function;

import java.util.List;

import com.elex.common.component.function.info.IFunctionInfo;

/**
 * 功能集群接口（组一级下，某一类型的功能服务管理）
 * 
 * @author mausmars
 * 
 */
public interface IFunctionCluster {
	/**
	 * 该功能集群是否可用
	 * 
	 * @return
	 */
	boolean isAvailable();

	/**
	 * 该功能当前服是否为主服务
	 * 
	 * @return
	 */
	boolean isCurrentMaster();

	IFunctionInfo getRandomFunctionInfo();

	IFunctionInfo getFunctionInfo(String fid);

	/**
	 * 获取所有远端服务节点
	 * 
	 * @return
	 */
	List<IFunctionInfo> getRemoteFunctionInfos();

	IFunctionInfo getLocalFunctionInfo();
}
