package com.elex.common.component.netdelaycheck;

public class DelayInfo {
	private int version;// 版本
	private long stime;// 1阶段 服务器时间
	private long ctime;// 1阶段 客户端时间
	private int step;// 阶段

	private int delay;// 延迟时间

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getStime() {
		return stime;
	}

	public void setStime(long stime) {
		this.stime = stime;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
}
