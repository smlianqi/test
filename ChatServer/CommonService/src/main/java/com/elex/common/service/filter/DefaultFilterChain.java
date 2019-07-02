package com.elex.common.service.filter;

import com.elex.common.util.linklist.AbstractNodeChain;

/**
 * 过滤链
 * 
 * @author mausmars
 * 
 */
public class DefaultFilterChain extends AbstractNodeChain<IFilter> implements IFilterChain {
	public DefaultFilterChain(String name) {
		super(name);
	}

	@Override
	public void doFilter(IFilter next, Object context) throws Exception {
		IFilter filter = selectFirst();
		if (filter != null) {
			filter.doFilter((IFilter) filter.next(), context);
		}
	}
}
