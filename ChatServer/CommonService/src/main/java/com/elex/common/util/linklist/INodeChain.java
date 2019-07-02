package com.elex.common.util.linklist;

public interface INodeChain extends INode {
	INode selectFirst(); // 查询first的node

	INode selectLast(); // 查询last的node

	INode selectNode(String name); // 查询name的node

	boolean isContainNode(String name); // 是否包含节点

	boolean insertToNodeBefore(String name, INode node); // 插入到name前边

	boolean insertToNodeAfter(String name, INode node); // 插入到name后边

	boolean insertNodeToFirst(INode node); // 插入到列表首部，如果存在name不插入

	boolean insertNodeToLast(INode node); // 插入到列表尾部，如果存在name不插入

	INode removeNode(String name); // 移除

	boolean replaceNode(INode node); // 替换，如果不存在就失败

};