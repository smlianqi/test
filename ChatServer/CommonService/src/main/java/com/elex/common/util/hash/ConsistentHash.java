package com.elex.common.util.hash;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 一致哈希
 * 
 * @author mausmars
 * 
 * @param <T>
 */
public class ConsistentHash<T> {
	protected static final ILogger logger = XLogUtil.logger();

	// 复制的节点个数（默认为3）
	private int numberOfReplicas = 3;
	// Hash计算对象，用于自定义hash算法
	private IHashFunc hashFunc = DefaultHashFunc.defaultHashFunc;
	// ---------------------------
	// 一致性Hash环
	private final SortedMap<Long, T> circle = new TreeMap<Long, T>();

	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 增加节点<br>
	 * 每增加一个节点，就会在闭环上增加给定复制节点数<br>
	 * 例如复制节点数是2，则每调用此方法一次，增加两个虚拟节点，这两个节点指向同一Node
	 * 由于hash算法会调用node的toString方法，故按照toString去重
	 * 
	 * @param node
	 *            节点对象
	 */
	public void add(T node) {
		lock.writeLock().lock();
		try {
			for (int index = 0; index < numberOfReplicas; index++) {
				// 哈希值作为key
				circle.put(getKey(node, index), node);
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void add(List<T> nodes) {
		lock.writeLock().lock();
		try {
			for (T node : nodes) {
				for (int index = 0; index < numberOfReplicas; index++) {
					// 哈希值作为key
					circle.put(getKey(node, index), node);
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 移除节点的同时移除相应的虚拟节点
	 * 
	 * @param node
	 *            节点对象
	 */
	public void remove(T node) {
		lock.writeLock().lock();
		try {
			for (int index = 0; index < numberOfReplicas; index++) {
				circle.remove(getKey(node, index));
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	private long getKey(T node, int index) {
		return hashFunc.hash(node.toString() + index);
	}

	/**
	 * 获得一个最近的顺时针节点
	 * 
	 * @param key
	 *            为给定键取Hash，取得顺时针方向上最近的一个虚拟节点对应的实际节点
	 * @return 节点对象
	 */
	public T get(String key) {
		if (circle.isEmpty()) {
			return null;
		}
		long hash = hashFunc.hash(key);
		
		if (!circle.containsKey(hash)) {
			// 返回此映射的部分视图，其键大于等于hash
			SortedMap<Long, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}

	public Collection<T> getNodes() {
		return circle.values();
	}

	// ------------------------
	public void setNumberOfReplicas(int numberOfReplicas) {
		this.numberOfReplicas = numberOfReplicas;
	}

	public void setHashFunc(IHashFunc hashFunc) {
		this.hashFunc = hashFunc;
	}
}