package com.elex.common.component.function.node;

import com.elex.common.component.function.info.IFunctionInfo;

/**
 * 初始化节点上下文
 * 
 * @author mausmars
 *
 */
public class InitNodeContext {
	private NodePath nodePath;

	private IFunctionInfo functionInfo;

	private int level = 0;

	public InitNodeContext(NodePath nodePath, IFunctionInfo functionInfo) {
		this.nodePath = nodePath;
		this.functionInfo = functionInfo;
	}

	public void reset() {
		level = 0;
	}

	public NodePath getNodePath() {
		return nodePath;
	}

	public int getLevel() {
		return nodePath.getLevel();
	}

	public String getChildKey() {
		return nodePath.getChildKey(level);
	}

	public String getNextChildKey() {
		return nodePath.getChildKey(level + 1);
	}

	public void next() {
		level++;
	}

	public IFunctionInfo getFunctionInfo() {
		return functionInfo;
	}

	public void setFunctionInfo(IFunctionInfo functionInfo) {
		this.functionInfo = functionInfo;
	}
}
