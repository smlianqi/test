package com.elex.common.component.function;

import java.util.List;

import com.elex.common.component.function.info.FunctionId;
import com.elex.common.component.function.info.IFunctionInfo;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.service.IService;

/**
 * 功能管理接口
 * 
 * @author mausmars
 * 
 */
public interface IFunctionService extends IService {
	/**
	 * 注册功能（为了对外暴露服务）
	 * 
	 * @param functionInfo
	 */
	void register(IFunctionInfo functionInfo);

	/**
	 * 注销功能列表（取消对外服务器）
	 * 
	 * @param fid
	 */
	void unregister(FunctionId fid);

	String getCurrentServerFid();

	/**
	 * 获得主功能服务器服务接口
	 * 
	 * @param rid
	 * @param ftype
	 * @return
	 */
	IFunctionCluster getFunctionCluster(String groupId, String regionId, FunctionType functionType);

	IFunctionCluster getFunctionCluster(FunctionId functionId);

	// -----------------------------------------
	IFunctionInfo getFunctionInfo(FunctionId functionId);

	IFunctionInfo getFunctionInfo(String fid);

	IFunctionInfo getLocalFunctionInfo();

	List<IFunctionInfo> getRemoteFunctionInfos(FunctionType functionType);

	IFunctionInfo getRandomFunctionInfo(FunctionType functionType);
	// -----------------------------------------

	/**
	 * 得到IFunctionInfo，并创建代理节点路径
	 * 
	 * @param fid
	 * @return
	 */
	IFunctionInfo getFunctionInfoAndCreatePrxNode(String fid);
}
