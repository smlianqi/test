package com.elex.common.component.script.config;

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
 * 脚本引擎配置 实体类
 */
public class ScScriptGeneral implements ISpread, IWriteReadable {
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
	 * 脚本引擎类型
	 */
	protected String scriptEngineType;
	
	@Override
	public void write(IDataOutput out) throws IOException {
			out.writeUTF(id);
			out.writeUTF(dependIds);
			out.writeUTF(readme);
			out.writeUTF(extraParams);
			out.writeUTF(scriptEngineType);
	}

	@Override
	public void read(IDataInput in) throws IOException {
			id=in.readUTF();
			dependIds=in.readUTF();
			readme=in.readUTF();
			extraParams=in.readUTF();
			scriptEngineType=in.readUTF();
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
		sb.append("ScScriptGeneral [");
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
		sb.append("scriptEngineType=");
		sb.append(scriptEngineType);
		
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
	
	public String getScriptEngineType() {
		return scriptEngineType;
	}
	
	public void setScriptEngineType(String scriptEngineType) {
		this.scriptEngineType = scriptEngineType;
	}
	
}