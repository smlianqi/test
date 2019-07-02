package com.elex.common.util.priorityqueue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * 优先级队列
 * 
 * @author mausmars
 *
 */
public class PriorityQueueMap {
	// {执行时间:cell}
	private TreeMap<Long, Set<QueueCell>> queue = new TreeMap<Long, Set<QueueCell>>();
	private Map<String, QueueCell> valueMap = new HashMap<String, QueueCell>();

	public Set<QueueCell> pollCell(long executeTime) {
		Entry<Long, Set<QueueCell>> entry = queue.firstEntry();
		if (entry == null) {
			return null;
		}
		if (entry.getKey() > executeTime) {
			return null;
		}
		queue.remove(entry.getKey());
		for (QueueCell cell : entry.getValue()) {
			valueMap.remove(cell.getKey());
		}
		return entry.getValue();
	}

	public void removeCell(long time) {
		Set<QueueCell> cells = queue.remove(time);
		if (cells == null) {
			return;
		}
		for (QueueCell cell : cells) {
			valueMap.remove(cell.getKey());
		}
	}

	public void putCell(QueueCell cell) {
		Set<QueueCell> cells = queue.get(cell.getTime());
		if (cells == null) {
			cells = new HashSet<QueueCell>();
			queue.put(cell.getTime(), cells);
		}
		valueMap.put(cell.getKey(), cell);
		cells.add(cell);
	}

	public QueueCell removeCell(String key) {
		QueueCell cell = valueMap.remove(key);
		if (cell != null) {
			Set<QueueCell> cells = queue.get(cell.getTime());
			cells.remove(cell);
			if (cells.size() <= 0) {
				queue.remove(cell.getTime());
			}
		}
		return cell;
	}

	public QueueCell getCell(String key) {
		return valueMap.get(key);
	}

	public static void main(String[] args) {
		PriorityQueueMap pq = new PriorityQueueMap();

		for (int i = 10; i > 0; i--) {
			QueueCell cell = new QueueCell();
			cell.setKey(String.valueOf(i));
			cell.setTime(i);
			cell.setObj(String.valueOf(i));
			pq.putCell(cell);
		}
		System.out.println(pq.pollCell(99));
		System.out.println(pq.pollCell(99));
		System.out.println(pq.removeCell("8"));
		System.out.println(pq.removeCell("4"));
	}
}
