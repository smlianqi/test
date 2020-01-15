package com.elex.im.module.servertest.rpcservice;

import com.elex.common.component.rpc.IRpcFServicePrx;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.module.servertest.rpcservice.bean.TestRpcBean;

/**
 * 本地实现接口
 * 
 * @author mausmars
 *
 */
public class TestLocalService implements IRpcFServicePrx, ITestService {
	protected static final ILogger logger = XLogUtil.logger();

	@Override
	public TestRpcBean select(int id) {
		if (logger.isDebugEnabled()) {
			logger.debug("TestLocalService select! id=" + id);
		}
		TestRpcBean testRpcBean = new TestRpcBean();
		testRpcBean.setId(id);
		testRpcBean.setName("response_" + id);
		testRpcBean.setSex(1);
		return testRpcBean;
	}

	@Override
	public void remove(int id) {
		if (logger.isDebugEnabled()) {
			logger.debug("TestLocalService remove! id=" + id);
		}
	}

	@Override
	public void insert(TestRpcBean pojo) {
		if (logger.isDebugEnabled()) {
			logger.debug("TestLocalService insert! name=" + pojo.getName() + ",id=" + pojo.getId());
		}
	}
}
