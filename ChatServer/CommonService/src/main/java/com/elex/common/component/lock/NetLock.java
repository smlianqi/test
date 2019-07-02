package com.elex.common.component.lock;

import io.netty.util.concurrent.Future;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import org.redisson.core.RLock;

import com.elex.common.component.lock.type.LockType;

/**
 * 网络锁
 * 
 * @author mausmars
 *
 */
public class NetLock implements ILock, RLock {
	private RLock lock;

	public NetLock(RLock lock) {
		this.lock = lock;
	}

	@Override
	public void lock() {
		lock.lock();
	}

	@Override
	public LockType getLockType() {
		return LockType.NetLock;
	}

	@Override
	public void unlock() {
		lock.unlock();
	}

	@Override
	public void lock(long leaseTime, TimeUnit unit) {
		lock.lock(leaseTime, unit);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		lock.lockInterruptibly();
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
	public Condition newCondition() {
		return lock.newCondition();
	}

	@Override
	public boolean expire(long timeToLive, TimeUnit timeUnit) {
		return lock.expire(timeToLive, timeUnit);
	}

	@Override
	public boolean expireAt(long timestamp) {
		return lock.expireAt(timestamp);
	}

	@Override
	public boolean expireAt(Date timestamp) {
		return lock.expireAt(timestamp);
	}

	@Override
	public boolean clearExpire() {
		return lock.clearExpire();
	}

	@Override
	public long remainTimeToLive() {
		return lock.remainTimeToLive();
	}

	@Override
	public void migrate(String host, int port, int database) {
		lock.migrate(host, port, database);
	}

	@Override
	public boolean move(int database) {
		return lock.move(database);
	}

	@Override
	public String getName() {
		return lock.getName();
	}

	@Override
	public boolean delete() {
		return lock.delete();
	}

	@Override
	public void rename(String newName) {
		lock.rename(newName);
	}

	@Override
	public boolean renamenx(String newName) {
		return false;
	}

	@Override
	public Future<Void> migrateAsync(String host, int port, int database) {
		return lock.migrateAsync(host, port, database);
	}

	@Override
	public Future<Boolean> moveAsync(int database) {
		return lock.moveAsync(database);
	}

	@Override
	public Future<Boolean> deleteAsync() {
		return lock.deleteAsync();
	}

	@Override
	public Future<Void> renameAsync(String newName) {
		return lock.renameAsync(newName);
	}

	@Override
	public Future<Boolean> renamenxAsync(String newName) {
		return lock.renamenxAsync(newName);
	}

	@Override
	public Future<Boolean> expireAsync(long timeToLive, TimeUnit timeUnit) {
		return lock.expireAsync(timeToLive, timeUnit);
	}

	@Override
	public Future<Boolean> expireAtAsync(Date timestamp) {
		return lock.expireAtAsync(timestamp);
	}

	@Override
	public Future<Boolean> expireAtAsync(long timestamp) {
		return lock.expireAtAsync(timestamp);
	}

	@Override
	public Future<Boolean> clearExpireAsync() {
		return lock.clearExpireAsync();
	}

	@Override
	public Future<Long> remainTimeToLiveAsync() {
		return lock.remainTimeToLiveAsync();
	}

	@Override
	public void lockInterruptibly(long leaseTime, TimeUnit unit) throws InterruptedException {
		lock.lockInterruptibly(leaseTime, unit);
	}

	@Override
	public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
		return lock.tryLock(waitTime, leaseTime, unit);
	}

	@Override
	public void forceUnlock() {
		lock.forceUnlock();
	}

	@Override
	public boolean isLocked() {
		return lock.isLocked();
	}

	@Override
	public boolean isHeldByCurrentThread() {
		return lock.isHeldByCurrentThread();
	}

	@Override
	public int getHoldCount() {
		return lock.getHoldCount();
	}
}
