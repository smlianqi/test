package com.elex.common.component.function.node;

import com.elex.common.component.function.info.IFunctionInfo;
import com.elex.common.component.function.type.LeafNodeType;

/**
 * 抽象叶子节点
 * 
 * @author mausmars
 *
 */
public abstract class LevelLeafNode extends AFunctionNode {
	private volatile IFunctionInfo functionInfo;
	protected LeafNodeType leafNodeType;

	public LevelLeafNode(String key) {
		super(key);
	}

	public LeafNodeType getLeafNodeType() {
		return leafNodeType;
	}

	public IFunctionInfo getFunctionInfo() {
		return functionInfo;
	}

	public synchronized IFunctionInfo setFunctionInfo(IFunctionInfo functionInfo) {
		IFunctionInfo temp = this.functionInfo;
		this.functionInfo = functionInfo;
		return temp;
	}

	public AFunctionNode createAndGetLocalChild(String key) {
		return null;
	}
}
