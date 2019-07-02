package com.elex.common.util.uuid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.elex.common.util.random.RandomUtil;

/**
 * 全服唯一的id生成器
 */
public class UniqueIdGenerator {
	private final static long sequenceMask = -1L ^ (-1L << 12);
	// 机器ID最大值
	private final static long maxServerId = -1L ^ (-1L << 12);

	// sequence 为12位，如果1ms创建了2^12个对象等
	private long sequence = 0L;
	private long lastTimestamp = -1L;

	// 锁
	private Lock lock = new ReentrantLock();

	// 服务器id
	private int serverId;
	// 差值
	private long twepoch;

	public UniqueIdGenerator() {
		this("2017-12-01");
	}

	public UniqueIdGenerator(String startTime) {
		int serverId = RandomUtil.generalRrandom(4000);
		init(serverId, startTime);
	}

	public UniqueIdGenerator(int serverId, String startTime) {
		init(serverId, startTime);
	}

	/**
	 * 初始化唯一标识 exp
	 * 
	 * @param serverId
	 * @param startTime
	 *            "2016-01-01"
	 */
	private void init(int serverId, String startTime) {
		if (serverId > maxServerId || serverId < 0) {
			throw new IllegalArgumentException("serverId Id can't be greater than %d or less than 0");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		try {
			twepoch = sdf.parse(startTime).getTime();
		} catch (ParseException e) {
			throw new IllegalArgumentException("startTime Format error");
		}
		if (twepoch > System.currentTimeMillis()) {
			throw new IllegalArgumentException("startTime Format error");
		}

		this.serverId = serverId;
		// 服务器id占12位（4096个可用id）
		// xxxx xxxx xxxx xxxx | xxxx xxxx xxxx xxxx
		// xxxx xxxx mmmm mmmm | mmmm 0000 0000 0000
		this.serverId <<= 12;
	}

	/**
	 * 生成ID
	 *
	 * @return
	 */
	public long nextID() {
		long timestamp = timeGen();// 当前时间
		if (timestamp < lastTimestamp) {
			try {
				throw new Exception("Clock moved backwards.  Refusing to generate id for " + (lastTimestamp - timestamp)
						+ " milliseconds");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			lock.lock();
			if (lastTimestamp == timestamp) {
				// 当前毫秒内，则+1
				sequence = (sequence + 1) & sequenceMask;
				if (sequence == 0) {
					// 当前毫秒内计数满了，则等待下一秒
					timestamp = tilNextMillis(lastTimestamp);
				}
			} else {
				sequence = 0;
			}
			lastTimestamp = timestamp;

			// ID偏移组合生成最终的ID，并返回ID
			// 服务启动时间（保证在40位，34年）
			// xxxx xxxx xxxx xxxx | xxxx xxxx xxxx xxxx
			// xxxx xxxx 0000 0000 | 0000 0000 0000 0000
			long nextId = ((timestamp - twepoch) << 24) | serverId | sequence;
			return nextId;
		} finally {
			lock.unlock();
		}
	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			// 循环等待
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	/**
	 * 获得当前系统时间, 单位:ms
	 *
	 * @return
	 */
	private long timeGen() {
		return System.currentTimeMillis();
	}

	public static void main(String args[]) {
	}
}
