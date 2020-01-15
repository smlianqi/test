package com.elex.common.component.rpc;

import com.elex.common.component.function.info.FunctionInfo;
import com.elex.common.service.IService;

/**
 * rpc底层服务接口 （封装底层网络服务）
 * 
 * @author mausmars
 *
 */
public interface IRpcService extends IService {
	/**
	 * 注册rpc服务
	 * 
	 * @param listeners
	 */
	void registerService(FunctionInfo functionInfo);
}
