package com.elex.im.module.servertest.rpcservice;

import com.elex.common.component.rpc.IRpcFService;
import com.elex.common.component.rpc.IRpcFServicePrx;
import com.elex.common.component.rpc.exception.CheckException;
import com.elex.im.module.servertest.rpcservice.bean.TestRpcBean;

import Ice.Current;

/**
 * ice实现，封装本地实现
 * 
 * @author mausmars
 *
 */
//public class TestServiceIce extends _ITestServiceDisp implements IRpcFService {
//	private static final long serialVersionUID = 1L;
//
//	// 本地service
//	private ITestService services;
//
//	public TestServiceIce(IRpcFServicePrx services) {
//		this.services = (ITestService) services;
//	}
//
//	@Override
//	public IRpcFServicePrx getRpcServicePrx() {
//		return services;
//	}
//
//	@Override
//	public void select_async(AMD_ITestService_select __cb, int id, Current __current) {
//		try {
//			TestRpcBean testBean = services.select(id);
//
//			TestBeanVO testBeanVO = new TestBeanVO();
//			testBeanVO.id = testBean.getId();
//			testBeanVO.name = testBean.getName();
//			testBeanVO.sex = testBean.getSex();
//			__cb.ice_response(testBeanVO);
//		} catch (CheckException e) {
//			__cb.ice_exception(createException(e));
//		} catch (Exception ex) {
//			__cb.ice_exception(ex);
//		}
//	}
//
//	@Override
//	public void remove_async(AMD_ITestService_remove __cb, int id, Current __current) {
//		try {
//			services.remove(id);
//			__cb.ice_response();
//		} catch (CheckException e) {
//			__cb.ice_exception(createException(e));
//		} catch (Exception ex) {
//			__cb.ice_exception(ex);
//		}
//	}
//
//	@Override
//	public void insert_async(AMD_ITestService_insert __cb, TestBeanVO tb, Current __current) {
//		try {
//			TestRpcBean testBean = new TestRpcBean();
//			testBean.setId(tb.id);
//			testBean.setName(tb.name);
//			testBean.setSex(tb.sex);
//			services.insert(testBean);
//			__cb.ice_response();
//		} catch (CheckException e) {
//			__cb.ice_exception(createException(e));
//		} catch (Exception ex) {
//			__cb.ice_exception(ex);
//		}
//	}
//
//	private TestCheckIceException createException(CheckException e) {
//		return new TestCheckIceException(e);
//	}
//}
