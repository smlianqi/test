package com.elex.common.util.graphtheory;

/**
 * 端点（一个线段的其中一个端点）
 * 
 * @author mausmars
 * 
 */
public interface IVertex {
	/**
	 * 端点key
	 */
	int key();

	/**
	 * 判断是否联通
	 */
	boolean link(IVertex v);
}
