package com.elex.common.component.function.filter;

/**
 * 节点过滤
 * 
 * @author mausmars
 *
 */
public interface INodeFilter {
	/**
	 * 过滤组
	 * 
	 * @param node
	 * @return
	 */
	boolean isGroupFilterNode(String node);

	/**
	 * 过滤区
	 * 
	 * @param node
	 * @return
	 */
	boolean isRegionFilterNode(String node);

	/**
	 * 过滤功能
	 * 
	 * @param node
	 * @return
	 */
	boolean isFunctionFilterNode(String node);
}
