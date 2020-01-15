package com.elex.common.component.net.config;

import java.io.IOException;
import java.util.Map;

import com.elex.common.component.data.ISpread;
import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.component.io.IWriteReadable;

/**
 * 网络客户端(类型400)（通信类型3） 实体类
 */
public class ScNetclientGeneral implements ISpread, IWriteReadable {
	/**
	 * 唯一id
	 */
	protected String id;
	
	/**
	 * 依赖服务
	 */
	protected String dependIds;
	
	/**
	 * 备注
	 */
	protected String readme;
	
	/**
	 * 扩展配置
	 */
	protected String extraParams;
	
	/**
	 * 网络服务类型
	 */
	protected String netServiceType;
	
	/**
	 * 网络协议
	 */
	protected String netProtocolType;
	
	/**
	 * 客户端host
	 */
	protected String host;
	
	/**
	 * 客户端端口
	 */
	protected int port;
	
	/**
	 * reactor线程数
	 */
	protected int subReactorThread;
	
	/**
	 * handler线程数
	 */
	protected int handlerThread;
	
	/**
	 * 全部空闲时间
	 */
	protected int allIdleTimeSeconds;
	
	/**
	 * 读空闲时间
	 */
	protected int readerIdleTimeSeconds;
	
	/**
	 * 写空闲时间
	 */
	protected int writerIdleTimeSeconds;
	
	/**
	 * 消息协议类型
	 */
	protected String megProtocolType;
	
	@Override
	public void write(IDataOutput out) throws IOException {
			out.writeUTF(id);
			out.writeUTF(dependIds);
			out.writeUTF(readme);
			out.writeUTF(extraParams);
			out.writeUTF(netServiceType);
			out.writeUTF(netProtocolType);
			out.writeUTF(host);
			out.writeInt(port);
			out.writeInt(subReactorThread);
			out.writeInt(handlerThread);
			out.writeInt(allIdleTimeSeconds);
			out.writeInt(readerIdleTimeSeconds);
			out.writeInt(writerIdleTimeSeconds);
			out.writeUTF(megProtocolType);
	}

	@Override
	public void read(IDataInput in) throws IOException {
			id=in.readUTF();
			dependIds=in.readUTF();
			readme=in.readUTF();
			extraParams=in.readUTF();
			netServiceType=in.readUTF();
			netProtocolType=in.readUTF();
			host=in.readUTF();
			port=in.readInt();
			subReactorThread=in.readInt();
			handlerThread=in.readInt();
			allIdleTimeSeconds=in.readInt();
			readerIdleTimeSeconds=in.readInt();
			writerIdleTimeSeconds=in.readInt();
			megProtocolType=in.readUTF();
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
		sb.append("ScNetclientGeneral [");
		sb.append("id=");
		sb.append(id);
		sb.append(", ");
		sb.append("dependIds=");
		sb.append(dependIds);
		sb.append(", ");
		sb.append("readme=");
		sb.append(readme);
		sb.append(", ");
		sb.append("extraParams=");
		sb.append(extraParams);
		sb.append(", ");
		sb.append("netServiceType=");
		sb.append(netServiceType);
		sb.append(", ");
		sb.append("netProtocolType=");
		sb.append(netProtocolType);
		sb.append(", ");
		sb.append("host=");
		sb.append(host);
		sb.append(", ");
		sb.append("port=");
		sb.append(port);
		sb.append(", ");
		sb.append("subReactorThread=");
		sb.append(subReactorThread);
		sb.append(", ");
		sb.append("handlerThread=");
		sb.append(handlerThread);
		sb.append(", ");
		sb.append("allIdleTimeSeconds=");
		sb.append(allIdleTimeSeconds);
		sb.append(", ");
		sb.append("readerIdleTimeSeconds=");
		sb.append(readerIdleTimeSeconds);
		sb.append(", ");
		sb.append("writerIdleTimeSeconds=");
		sb.append(writerIdleTimeSeconds);
		sb.append(", ");
		sb.append("megProtocolType=");
		sb.append(megProtocolType);
		
		sb.append("]");
		return sb.toString();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDependIds() {
		return dependIds;
	}
	
	public void setDependIds(String dependIds) {
		this.dependIds = dependIds;
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
	
	public String getNetServiceType() {
		return netServiceType;
	}
	
	public void setNetServiceType(String netServiceType) {
		this.netServiceType = netServiceType;
	}
	
	public String getNetProtocolType() {
		return netProtocolType;
	}
	
	public void setNetProtocolType(String netProtocolType) {
		this.netProtocolType = netProtocolType;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getSubReactorThread() {
		return subReactorThread;
	}
	
	public void setSubReactorThread(int subReactorThread) {
		this.subReactorThread = subReactorThread;
	}
	
	public int getHandlerThread() {
		return handlerThread;
	}
	
	public void setHandlerThread(int handlerThread) {
		this.handlerThread = handlerThread;
	}
	
	public int getAllIdleTimeSeconds() {
		return allIdleTimeSeconds;
	}
	
	public void setAllIdleTimeSeconds(int allIdleTimeSeconds) {
		this.allIdleTimeSeconds = allIdleTimeSeconds;
	}
	
	public int getReaderIdleTimeSeconds() {
		return readerIdleTimeSeconds;
	}
	
	public void setReaderIdleTimeSeconds(int readerIdleTimeSeconds) {
		this.readerIdleTimeSeconds = readerIdleTimeSeconds;
	}
	
	public int getWriterIdleTimeSeconds() {
		return writerIdleTimeSeconds;
	}
	
	public void setWriterIdleTimeSeconds(int writerIdleTimeSeconds) {
		this.writerIdleTimeSeconds = writerIdleTimeSeconds;
	}
	
	public String getMegProtocolType() {
		return megProtocolType;
	}
	
	public void setMegProtocolType(String megProtocolType) {
		this.megProtocolType = megProtocolType;
	}
	
}