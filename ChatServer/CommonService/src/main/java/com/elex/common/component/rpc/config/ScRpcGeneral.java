package com.elex.common.component.rpc.config;

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
public class ScRpcGeneral implements ISpread, IWriteReadable {
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
	 * host模板
	 */
	protected String host;
	
	/**
	 * 起始端口
	 */
	protected int port;
	
	/**
	 * 服务对象id
	 */
	protected String interfaceInfos;
	
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
			out.writeUTF(interfaceInfos);
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
			interfaceInfos=in.readUTF();
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
		sb.append("ScRpcGeneral [");
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
		sb.append("interfaceInfos=");
		sb.append(interfaceInfos);
		
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
	
	public String getInterfaceInfos() {
		return interfaceInfos;
	}
	
	public void setInterfaceInfos(String interfaceInfos) {
		this.interfaceInfos = interfaceInfos;
	}
	
}