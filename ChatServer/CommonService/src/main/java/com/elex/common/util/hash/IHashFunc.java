package com.elex.common.util.hash;

/**
 * Hash算法对象，用于自定义hash算法
 */
public interface IHashFunc {
	/**
	 * 计算hash值
	 * 
	 * @param key
	 * @return
	 */
	long hash(String key);
}
