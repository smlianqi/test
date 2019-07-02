package com.elex.common.component.ignite.config;

import java.io.IOException;
import java.util.Map;

import com.elex.common.component.data.ISpread;
import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.component.io.IWriteReadable;

/**
 * ignite配置 实体类
 */
public class ScIgniteGeneral implements ISpread, IWriteReadable {
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
	 * 发现类型(zookeeper,)
	 */
	protected String discoveryType;
	
	/**
	 * host模板
	 */
	protected String host;
	
	/**
	 * 起始端口
	 */
	protected int port;
	
	/**
	 * 是否为client
	 */
	protected boolean isClient;
	
	/**
	 * 工作目录
	 */
	protected String workDirectory;
	
	/**
	 * 交换分区目录
	 */
	protected String swapDirectory;
	
	@Override
	public void write(IDataOutput out) throws IOException {
			out.writeUTF(id);
			out.writeUTF(dependIds);
			out.writeUTF(readme);
			out.writeUTF(extraParams);
			out.writeUTF(discoveryType);
			out.writeUTF(host);
			out.writeInt(port);
			out.writeBoolean(isClient);
			out.writeUTF(workDirectory);
			out.writeUTF(swapDirectory);
	}

	@Override
	public void read(IDataInput in) throws IOException {
			id=in.readUTF();
			dependIds=in.readUTF();
			readme=in.readUTF();
			extraParams=in.readUTF();
			discoveryType=in.readUTF();
			host=in.readUTF();
			port=in.readInt();
			isClient=in.readBoolean();
			workDirectory=in.readUTF();
			swapDirectory=in.readUTF();
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
		sb.append("ScIgniteGeneral [");
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
		sb.append("discoveryType=");
		sb.append(discoveryType);
		sb.append(", ");
		sb.append("host=");
		sb.append(host);
		sb.append(", ");
		sb.append("port=");
		sb.append(port);
		sb.append(", ");
		sb.append("isClient=");
		sb.append(isClient);
		sb.append(", ");
		sb.append("workDirectory=");
		sb.append(workDirectory);
		sb.append(", ");
		sb.append("swapDirectory=");
		sb.append(swapDirectory);
		
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
	
	public String getDiscoveryType() {
		return discoveryType;
	}
	
	public void setDiscoveryType(String discoveryType) {
		this.discoveryType = discoveryType;
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
	
	public boolean getIsClient() {
		return isClient;
	}
	
	public void setIsClient(boolean isClient) {
		this.isClient = isClient;
	}
	
	public String getWorkDirectory() {
		return workDirectory;
	}
	
	public void setWorkDirectory(String workDirectory) {
		this.workDirectory = workDirectory;
	}
	
	public String getSwapDirectory() {
		return swapDirectory;
	}
	
	public void setSwapDirectory(String swapDirectory) {
		this.swapDirectory = swapDirectory;
	}
	
}