package com.elex.common.component.function.event;

import com.elex.common.component.function.IFunctionContext;
import com.elex.common.component.function.info.FunctionId;

/**
 * 主服务器变更事件
 * 
 * @author mausmars
 *
 */
public class MasterChangeEvent {
	private FunctionId masterKey;
	private FunctionId proMasterKey;
	private long ctime;
	private boolean isMaster;// 当前服务是否变为主服务

	private IFunctionContext functionContext;

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public boolean isMaster() {
		return isMaster;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public FunctionId getMasterKey() {
		return masterKey;
	}

	public void setMasterKey(FunctionId masterKey) {
		this.masterKey = masterKey;
	}

	public FunctionId getProMasterKey() {
		return proMasterKey;
	}

	public void setProMasterKey(FunctionId proMasterKey) {
		this.proMasterKey = proMasterKey;
	}

	public IFunctionContext getFunctionContext() {
		return functionContext;
	}

	public void setFunctionContext(IFunctionContext functionContext) {
		this.functionContext = functionContext;
	}

	@Override
	public String toString() {
		return "MasterChangeEvent [masterKey=" + masterKey + ", proMasterKey=" + proMasterKey + ", ctime=" + ctime
				+ ", isMaster=" + isMaster + ", functionContext=" + functionContext + "]";
	}
}
