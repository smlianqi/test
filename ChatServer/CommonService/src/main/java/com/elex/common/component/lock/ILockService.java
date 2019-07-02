package com.elex.common.component.lock;

import com.elex.common.component.lock.type.LockType;
import com.elex.common.service.IService;

/**
 * 锁服务接口
 * 
 * @author mausmars
 *
 */
public interface ILockService extends IService {
	/**
	 * 获得锁
	 * 
	 * @param key
	 * @return
	 */
	ILock getLock(Object key);

	/**
	 * 获得锁超时时间
	 * 
	 * @return
	 */
	int getLockOutTime();

	/**
	 * 获取所类型
	 * 
	 * @return
	 */
	LockType getLockType();
}
