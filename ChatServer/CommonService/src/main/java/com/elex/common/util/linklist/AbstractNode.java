package com.elex.common.util.linklist;

/**
 * 抽象节点
 * 
 * @author mausmars
 * 
 */
public abstract class AbstractNode implements INode {
	protected INode prevNode;// 前一个Node
	protected INode nextNode;// 后一个Node

	@Override
	public INode prev() {
		return prevNode;
	}

	@Override
	public void sprev(INode node) {
		prevNode = node;
	}

	@Override
	public INode next() {
		return nextNode;
	}

	@Override
	public void snext(INode node) {
		nextNode = node;
	}
}
