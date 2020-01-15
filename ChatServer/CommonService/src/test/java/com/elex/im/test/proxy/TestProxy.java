package com.elex.im.test.proxy;

import com.elex.common.component.proxy.CglibMonitorInterceptor;
import com.elex.common.component.proxy.CglibProxyFactory;
import com.elex.im.test.proxy.test.ITest;
import com.elex.im.test.proxy.test.Test;

public class TestProxy {

	public static void main(String[] args) {
		CglibMonitorInterceptor interceptor = new CglibMonitorInterceptor();
		CglibProxyFactory cglibProxyFactory = new CglibProxyFactory(interceptor);

		ITest test = cglibProxyFactory.getInterface(Test.class, new Class[] { int.class }, new Object[] { 1 });

		int v = test.test(1);
		System.out.println(v);
	}
}
