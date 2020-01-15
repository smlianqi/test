package com.elex.common.service.filter;

import com.elex.common.util.linklist.AbstractNode;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 抽象Filter
 * 
 * @author mausmars
 * 
 */
public abstract class AbstractFilter extends AbstractNode implements IFilter {
	protected static final ILogger logger = XLogUtil.logger();

	protected String name;

	public AbstractFilter(String name) {
		this.name = name;
	}

	public String key() {
		return name;
	}

	@Override
	public void doFilter(IFilter next, Object context) throws Exception {
		boolean isContinue = doWork(context);
		if (isContinue && next != null) {
			IFilter n = (IFilter) next.next();
			next.doFilter(n, context);
		}
	}

	/**
	 * 做事情
	 * 
	 * @param context
	 * @return true往下执行，false不执行
	 */
	public abstract boolean doWork(Object context) throws Exception;
}