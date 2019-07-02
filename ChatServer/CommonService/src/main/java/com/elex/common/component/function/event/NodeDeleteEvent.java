package com.elex.common.component.function.event;

import com.elex.common.component.function.IFunctionContext;
import com.elex.common.component.function.info.FunctionId;

/**
 * 节点移除事件
 * 
 * @author mausmars
 *
 */
public class NodeDeleteEvent {
	// 当前服是否为主节点
	private boolean isMaster;
	private FunctionId functionId;

	private IFunctionContext functionContext;

	public FunctionId getFunctionId() {
		return functionId;
	}

	public void setFunctionId(FunctionId functionId) {
		this.functionId = functionId;
	}

	public boolean isMaster() {
		return isMaster;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public IFunctionContext getFunctionContext() {
		return functionContext;
	}

	public void setFunctionContext(IFunctionContext functionContext) {
		this.functionContext = functionContext;
	}

	@Override
	public String toString() {
		return "NodeDeleteEvent [isMaster=" + isMaster + ", functionId=" + functionId + ", functionContext="
				+ functionContext + "]";
	}
}
