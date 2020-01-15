package com.elex.common.net.service.netty;

/**
 * 链接优化配置
 * 
 * @author mausmars
 *
 */
public class ChannelOptionConfig {
	private Integer backlog;// 最大客户端等待队列
	private Boolean keepalive;//
	private Boolean nodelay;// true禁用negal算法
	private Boolean reuseaddr;// 重用地址
	// 那么close会等到发送的数据已经确认了才返回。但是如果对方宕机，超时，那么会根据linger设定的时间返回。
	private Integer linger;

	// 设置socket调用InputStream读数据的超时时间，以毫秒为单位，如果超过这个时候，会抛出java.net.SocketTimeoutException。
	private Integer timeout;

	private Integer rcvbuf;// 接收缓存大小
	private Integer sndbuf;// 发送缓存大小
	private Integer connectTimeoutMillis;// 链接超时毫秒

	public Integer getBacklog() {
		return backlog;
	}

	public void setBacklog(Integer backlog) {
		this.backlog = backlog;
	}

	public Boolean getKeepalive() {
		return keepalive;
	}

	public void setKeepalive(Boolean keepalive) {
		this.keepalive = keepalive;
	}

	public Boolean getNodelay() {
		return nodelay;
	}

	public void setNodelay(Boolean nodelay) {
		this.nodelay = nodelay;
	}

	public Boolean getReuseaddr() {
		return reuseaddr;
	}

	public void setReuseaddr(Boolean reuseaddr) {
		this.reuseaddr = reuseaddr;
	}

	public Integer getLinger() {
		return linger;
	}

	public void setLinger(Integer linger) {
		this.linger = linger;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getRcvbuf() {
		return rcvbuf;
	}

	public void setRcvbuf(Integer rcvbuf) {
		this.rcvbuf = rcvbuf;
	}

	public Integer getSndbuf() {
		return sndbuf;
	}

	public void setSndbuf(Integer sndbuf) {
		this.sndbuf = sndbuf;
	}

	public Integer getConnectTimeoutMillis() {
		return connectTimeoutMillis;
	}

	public void setConnectTimeoutMillis(Integer connectTimeoutMillis) {
		this.connectTimeoutMillis = connectTimeoutMillis;
	}
}
