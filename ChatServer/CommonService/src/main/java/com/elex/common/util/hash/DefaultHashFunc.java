package com.elex.common.util.hash;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public class DefaultHashFunc implements IHashFunc {
	protected static final ILogger logger = XLogUtil.logger();

	public static final DefaultHashFunc defaultHashFunc = new DefaultHashFunc();

	@Override
	public long hash(String key) {
		String data = key.toString();
		// 默认使用FNV1hash算法
		final int p = 16777619;
		long hash = 2166136261L;
		for (int i = 0; i < data.length(); i++)
			hash = (hash ^ data.charAt(i)) * p;
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;
		// if (logger.isInfoEnabled()) {
		// logger.info("data=" + data + " hash=" + hash);
		// }
		return hash;
	}
}
