package com.elex.im.module.servertest.rpcservice;

import com.elex.common.component.rpc.IRpcFServicePrx;
import com.elex.im.module.servertest.rpcservice.bean.TestRpcBean;

import Ice.ObjectPrx;
//import serverice.test.ITestServicePrx;
//import serverice.test.TestBeanVO;
//import serverice.test.TestCheckIceException;

/**
 * 这里类型名和配置中的接口名相关 。 (sc_rpc.properties文件中的
 * RPC_InterfaceNames=["ITestServiceRPC"])。这个类在对应的工厂中是通过构造方法注入，需要实现构造方法
 * 
 * @author mausmars
 *
 */
//public class TestServiceIcePrx implements IRpcFServicePrx, ITestService {
//	// ice代理接口
//	private ITestServicePrx servicePrx;
//
//	public TestServiceIcePrx(ObjectPrx servicePrx) {
//		this.servicePrx = (ITestServicePrx) servicePrx;
//	}
//
//	@Override
//	public TestRpcBean select(int id) {
//		try {
//			TestBeanVO testBeanVO = servicePrx.select(id);
//
//			TestRpcBean testBean = new TestRpcBean();
//			testBean.setId(testBeanVO.id);
//			testBean.setName(testBeanVO.name);
//			testBean.setSex(testBeanVO.sex);
//			testBean.obtainAfter();
//			return testBean;
//		} catch (TestCheckIceException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	@Override
//	public void remove(int id) {
//		try {
//			servicePrx.remove(id);
//		} catch (TestCheckIceException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	@Override
//	public void insert(TestRpcBean testBean) {
//		try {
//			testBean.saveAfter();
//			TestBeanVO testBeanVO = new TestBeanVO();
//			testBeanVO.id = testBean.getId();
//			testBeanVO.name = testBean.getName();
//			testBeanVO.sex = testBean.getSex();
//			servicePrx.insert(testBeanVO);
//		} catch (TestCheckIceException e) {
//			throw new RuntimeException(e);
//		}
//	}
//}
