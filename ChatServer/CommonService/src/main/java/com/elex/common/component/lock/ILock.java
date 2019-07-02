package com.elex.common.component.lock;

import java.util.concurrent.TimeUnit;

import com.elex.common.component.lock.type.LockType;

/**
 * 锁接口
 * 
 * @author mausmars
 *
 */
public interface ILock {
	/**
	 * 锁
	 * 
	 * @param lock
	 */
	void lock();

	/**
	 * 锁
	 * 
	 * @param leaseTime
	 * @param unit
	 */
	void lock(long leaseTime, TimeUnit unit);

	/**
	 * 
	 * @return
	 */
	boolean tryLock();

	/**
	 * 
	 * @param time
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

	/**
	 * 解锁
	 * 
	 * @param lock
	 */
	void unlock();

	/**
	 * 获取所类型
	 * 
	 * @return
	 */
	LockType getLockType();
}
