package com.elex.common.component.net.config;

import java.util.Date;
import java.util.Map;

import com.elex.common.component.data.ISpread;
import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.component.io.IWriteReadable;

import java.io.IOException;

/**
 * rpc配置（通信类型1） 实体类
 */
public class ChannelOptionGeneral implements ISpread, IWriteReadable {
	/**
	 * 唯一id
	 */
	protected String id;
	
	/**
	 * 备注
	 */
	protected String readme;
	
	/**
	 * 扩展配置
	 */
	protected String extraParams;
	
	/**
	 * 最大客户端等待队列
	 */
	protected int backlog;
	
	/**
	 * 保活
	 */
	protected boolean keepalive;
	
	/**
	 * true禁用negal算法
	 */
	protected boolean nodelay;
	
	/**
	 * 重用地址
	 */
	protected boolean reuseaddr;
	
	/**
	 * 那么close会等到发送的数据已经确认了才返回。但是如果对方宕机，超时，那么会根据linger设定的时间返回。
	 */
	protected int linger;
	
	/**
	 * 设置socket调用InputStream读数据的超时时间，以毫秒为单位，如果超过这个时候，会抛出java.net.SocketTimeoutException
	 */
	protected int timeout;
	
	/**
	 * 接收缓存大小
	 */
	protected int rcvbuf;
	
	/**
	 * 发送缓存大小
	 */
	protected int sndbuf;
	
	/**
	 * 链接超时毫秒
	 */
	protected int connectTimeout;
	
	@Override
	public void write(IDataOutput out) throws IOException {
			out.writeUTF(id);
			out.writeUTF(readme);
			out.writeUTF(extraParams);
			out.writeInt(backlog);
			out.writeBoolean(keepalive);
			out.writeBoolean(nodelay);
			out.writeBoolean(reuseaddr);
			out.writeInt(linger);
			out.writeInt(timeout);
			out.writeInt(rcvbuf);
			out.writeInt(sndbuf);
			out.writeInt(connectTimeout);
	}

	@Override
	public void read(IDataInput in) throws IOException {
			id=in.readUTF();
			readme=in.readUTF();
			extraParams=in.readUTF();
			backlog=in.readInt();
			keepalive=in.readBoolean();
			nodelay=in.readBoolean();
			reuseaddr=in.readBoolean();
			linger=in.readInt();
			timeout=in.readInt();
			rcvbuf=in.readInt();
			sndbuf=in.readInt();
			connectTimeout=in.readInt();
	}
	
	@Override
	public byte[] getBytes() throws IOException {
		ByteArrayOutput out = new ByteArrayOutput();
		write(out);
		return out.toByteArray();
	}

	@Override
	public void initField(byte[] bytes) throws IOException {
		ByteArrayInput byteArrayInput = new ByteArrayInput(bytes);
		this.read(byteArrayInput);
	}
	
	@Override
	public void obtainAfter() {
	}

	@Override
	public void saveBefore() {
	}

	@Override
	public void saveAfter() {
	}

	@Override
	public <T> T cloneEntity(boolean isSaveBefore) {
		return null;
	}
	
	@Override
	public Map<String, Object> getIndexChangeBefore() {
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("ChannelOptionGeneral [");
		sb.append("id=");
		sb.append(id);
		sb.append(", ");
		sb.append("readme=");
		sb.append(readme);
		sb.append(", ");
		sb.append("extraParams=");
		sb.append(extraParams);
		sb.append(", ");
		sb.append("backlog=");
		sb.append(backlog);
		sb.append(", ");
		sb.append("keepalive=");
		sb.append(keepalive);
		sb.append(", ");
		sb.append("nodelay=");
		sb.append(nodelay);
		sb.append(", ");
		sb.append("reuseaddr=");
		sb.append(reuseaddr);
		sb.append(", ");
		sb.append("linger=");
		sb.append(linger);
		sb.append(", ");
		sb.append("timeout=");
		sb.append(timeout);
		sb.append(", ");
		sb.append("rcvbuf=");
		sb.append(rcvbuf);
		sb.append(", ");
		sb.append("sndbuf=");
		sb.append(sndbuf);
		sb.append(", ");
		sb.append("connectTimeout=");
		sb.append(connectTimeout);
		
		sb.append("]");
		return sb.toString();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getReadme() {
		return readme;
	}
	
	public void setReadme(String readme) {
		this.readme = readme;
	}
	
	public String getExtraParams() {
		return extraParams;
	}
	
	public void setExtraParams(String extraParams) {
		this.extraParams = extraParams;
	}
	
	public int getBacklog() {
		return backlog;
	}
	
	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}
	
	public boolean getKeepalive() {
		return keepalive;
	}
	
	public void setKeepalive(boolean keepalive) {
		this.keepalive = keepalive;
	}
	
	public boolean getNodelay() {
		return nodelay;
	}
	
	public void setNodelay(boolean nodelay) {
		this.nodelay = nodelay;
	}
	
	public boolean getReuseaddr() {
		return reuseaddr;
	}
	
	public void setReuseaddr(boolean reuseaddr) {
		this.reuseaddr = reuseaddr;
	}
	
	public int getLinger() {
		return linger;
	}
	
	public void setLinger(int linger) {
		this.linger = linger;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public int getRcvbuf() {
		return rcvbuf;
	}
	
	public void setRcvbuf(int rcvbuf) {
		this.rcvbuf = rcvbuf;
	}
	
	public int getSndbuf() {
		return sndbuf;
	}
	
	public void setSndbuf(int sndbuf) {
		this.sndbuf = sndbuf;
	}
	
	public int getConnectTimeout() {
		return connectTimeout;
	}
	
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
}