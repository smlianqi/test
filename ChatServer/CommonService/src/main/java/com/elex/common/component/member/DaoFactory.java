package com.elex.common.component.member;

import com.elex.common.component.ignite.IIgniteService;
import com.elex.common.component.member.data.IMemberOnlineDao;
import com.elex.common.component.member.data.MemberOnlineDao;
import com.elex.common.component.proxy.CglibMonitorInterceptor;
import com.elex.common.component.proxy.CglibProxyFactory;

public class DaoFactory {
	private CglibProxyFactory cglibProxyFactory;

	public DaoFactory() {
		CglibMonitorInterceptor interceptor = new CglibMonitorInterceptor();
		cglibProxyFactory = new CglibProxyFactory(interceptor);
	}

	public IMemberOnlineDao createMemberOnlineDao(IIgniteService igniteService) {
		// IMemberOnlineDao dao = new MemberOnlineDao(igniteService);
		IMemberOnlineDao dao = cglibProxyFactory.getInterface(MemberOnlineDao.class,
				new Class[] { IIgniteService.class }, new Object[] { igniteService });
		return dao;
	}
}
