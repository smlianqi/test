package com.elex.common.util.linklist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象链
 * 
 * @author mausmars
 * 
 * @param <T>
 */
public abstract class AbstractNodeChain<T extends INode> extends AbstractNode implements INodeChain {
	private int size; // 节点个数
	protected INode head; // 链的头节点
	protected INode tail; // 链的尾节点

	protected Object key;
	protected Map<Object, INode> nodeMap = new HashMap<Object, INode>(); // 节点映射

	public AbstractNodeChain(Object key) {
		this.key = key;
	}

	public AbstractNodeChain(Object key, List<T> nodes) {
		this.key = key;
		for (T node : nodes) {
			insertNodeToLast(node);
		}
	}

	@Override
	public boolean insertToNodeBefore(String key, INode node) {
		if (!nodeMap.containsKey(key) || nodeMap.containsKey(node.key())) {
			return false;
		}
		INode rnode = nodeMap.get(key);
		if (head == rnode) {
			return insertNodeToFirst(node);
		}
		node.sprev(rnode.prev());
		node.snext(rnode);
		rnode.prev().snext(node);
		rnode.sprev(node);
		nodeMap.put(node.key(), node);
		size++;
		return true;
	}

	@Override
	public boolean insertToNodeAfter(String key, INode node) {
		if (!nodeMap.containsKey(key) || nodeMap.containsKey(node.key())) {
			return false;
		}
		INode rnode = nodeMap.get(key);
		if (tail == rnode) {
			return insertNodeToLast(node);
		}
		node.sprev(rnode);
		node.snext(rnode.next());
		rnode.next().sprev(node);
		rnode.snext(node);
		nodeMap.put(node.key(), node);
		size++;
		return true;
	}

	@Override
	public boolean insertNodeToFirst(INode node) {
		if (nodeMap.containsKey(node.key())) {
			return false;
		}
		if (head == null) {
			head = node;
			tail = node;
		} else {
			head.sprev(node);
			node.snext(head);
			head = node;
		}
		nodeMap.put(node.key(), node);
		size++;
		return true;
	}

	@Override
	public boolean insertNodeToLast(INode node) {
		if (nodeMap.containsKey(node.key())) {
			return false;
		}
		if (tail == null) {
			head = node;
			tail = node;
		} else {
			tail.snext(node);
			node.sprev(tail);
			tail = node;
		}
		nodeMap.put(node.key(), node);
		size++;
		return true;
	}

	@Override
	public INode removeNode(String key) {
		INode node = nodeMap.remove(key);
		if (node == null) {
			return node;
		}
		if (node.prev() != null) {
			node.prev().snext(node.next());
		} else {
			head = node.next();
		}

		if (node.next() != null) {
			node.next().sprev(node.prev());
		} else {
			tail = node.prev();
		}
		node.sprev(null);
		node.snext(null);
		size--;
		return node;
	}

	@Override
	public boolean replaceNode(INode node) {
		if (!nodeMap.containsKey(node.key())) {
			return false;
		}
		INode rnode = nodeMap.get(key);
		node.sprev(rnode.prev());
		node.snext(rnode.next());
		rnode.sprev(null);
		rnode.snext(null);
		nodeMap.put(node.key(), node);
		return true;
	}

	@Override
	public T selectNode(String key) {
		return (T) nodeMap.get(key);
	}

	@Override
	public T selectFirst() {
		return (T) head;
	}

	@Override
	public T selectLast() {
		return (T) tail;
	}

	@Override
	public boolean isContainNode(String key) {
		return nodeMap.containsKey(key);
	}

	public int getSize() {
		return size;
	}

	public Object key() {
		return key;
	}
}
