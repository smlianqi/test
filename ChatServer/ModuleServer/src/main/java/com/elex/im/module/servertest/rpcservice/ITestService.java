package com.elex.im.module.servertest.rpcservice;

import com.elex.common.component.rpc.IRpcFServicePrx;
import com.elex.im.module.servertest.rpcservice.bean.TestRpcBean;

/**
 * 接口
 * 
 * @author mausmars
 *
 */
public interface ITestService extends IRpcFServicePrx {
	TestRpcBean select(int id);

	void remove(int id);

	void insert(TestRpcBean testRpcBean);
}
