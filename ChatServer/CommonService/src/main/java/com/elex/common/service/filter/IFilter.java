package com.elex.common.service.filter;

import com.elex.common.util.linklist.INode;

/**
 * 过滤接口
 * 
 * @author mausmars
 * 
 */
public interface IFilter extends INode {
	/**
	 * 执行
	 * 
	 * @param next
	 * @param context
	 */
	void doFilter(IFilter next, Object context) throws Exception;
}
