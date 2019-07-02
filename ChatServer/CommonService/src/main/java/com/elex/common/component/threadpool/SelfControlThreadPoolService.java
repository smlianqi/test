package com.elex.common.component.threadpool;

import com.elex.common.component.threadpool.config.ScThreadpool;
import com.elex.common.component.threadpool.type.ThreadpoolType;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.util.hash.HashType;
import com.elex.common.util.hash.IHashFunc;
import com.elex.common.util.hash.SimpleHashFunction;

/**
 * 自控制线程池
 * 
 * @author mausmars
 *
 */
public class SelfControlThreadPoolService extends AbstractService<ScThreadpool> implements IThreadPoolService {
	private IPoolExecutor poolExecutor;

	public SelfControlThreadPoolService(IServiceConfig serviceConfig, IGlobalContext context) {
		super(serviceConfig, context);
	}

	@Override
	public void initService() throws Exception {
		ScThreadpool scThreadpool = getSConfig();

		// 线程数量
		int threadCount = (int) ((double) scThreadpool.getExtraParamsMap().get("threadCount"));
		String hashTypeStr = (String) scThreadpool.getExtraParamsMap().get("hashType");
		String threadName = scThreadpool.getName();

		// 设置hash函数
		HashType hashType = HashType.valueOf(hashTypeStr);
		IHashFunc hashFunc = null;
		switch (hashType) {
		case Simple:
			hashFunc = new SimpleHashFunction();
			break;
		default:
			break;
		}
		SelfControlPoolExecutor poolExecutor = new SelfControlPoolExecutor(threadCount, threadName);
		poolExecutor.setHashFunc(hashFunc);
		this.poolExecutor = poolExecutor;
	}

	@Override
	public void startupService() throws Exception {
		poolExecutor.startup();
	}

	@Override
	public void shutdownService() throws Exception {
		poolExecutor.shutdown(false);
	}

	@Override
	public IPoolExecutor getPoolExecutor() {
		return poolExecutor;
	}

	@Override
	public ThreadpoolType getThreadpoolType() {
		return ThreadpoolType.SelfControl;
	}
}
