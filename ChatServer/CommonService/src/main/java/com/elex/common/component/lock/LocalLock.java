package com.elex.common.component.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.elex.common.component.lock.type.LockType;

/**
 * 本地锁
 * 
 * @author mausmars
 *
 */
public class LocalLock implements ILock, Lock {
	private Lock lock;

	public LocalLock() {
		lock = new ReentrantLock();
	}

	@Override
	public LockType getLockType() {
		return LockType.LocalLock;
	}

	@Override
	public void lock() {
		lock.lock();
	}

	@Override
	public void lock(long leaseTime, TimeUnit unit) {
	}

	@Override
	public boolean tryLock() {
		return lock.tryLock();
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return lock.tryLock(time, unit);
	}

	@Override
	public void unlock() {
		lock.unlock();
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		lock.lockInterruptibly();
	}

	@Override
	public Condition newCondition() {
		return lock.newCondition();
	}
}
