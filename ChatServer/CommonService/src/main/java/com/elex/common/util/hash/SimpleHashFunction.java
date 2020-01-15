package com.elex.common.util.hash;

/**
 * 简单哈希
 * 
 * @author mausmars
 *
 */
public class SimpleHashFunction implements IHashFunc {
	@Override
	public long hash(String key) {
		return (long) key.hashCode();
	}
}
