package com.elex.common.service.config;

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
 * 区配置 实体类
 */
public class ConfigFunctioninfoGeneral implements ISpread, IWriteReadable {
	/**
	 * 所属组
	 */
	protected String groupId;
	
	/**
	 * 功能类型
	 */
	protected String functionType;
	
	/**
	 * 区id
	 */
	protected String regionId;
	
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
	
	@Override
	public void write(IDataOutput out) throws IOException {
			out.writeUTF(groupId);
			out.writeUTF(functionType);
			out.writeUTF(regionId);
			out.writeUTF(dependIds);
			out.writeUTF(readme);
			out.writeUTF(extraParams);
	}

	@Override
	public void read(IDataInput in) throws IOException {
			groupId=in.readUTF();
			functionType=in.readUTF();
			regionId=in.readUTF();
			dependIds=in.readUTF();
			readme=in.readUTF();
			extraParams=in.readUTF();
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
		sb.append("ConfigFunctioninfoGeneral [");
		sb.append("groupId=");
		sb.append(groupId);
		sb.append(", ");
		sb.append("functionType=");
		sb.append(functionType);
		sb.append(", ");
		sb.append("regionId=");
		sb.append(regionId);
		sb.append(", ");
		sb.append("dependIds=");
		sb.append(dependIds);
		sb.append(", ");
		sb.append("readme=");
		sb.append(readme);
		sb.append(", ");
		sb.append("extraParams=");
		sb.append(extraParams);
		
		sb.append("]");
		return sb.toString();
	}
	
	public String getGroupId() {
		return groupId;
	}
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getFunctionType() {
		return functionType;
	}
	
	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}
	
	public String getRegionId() {
		return regionId;
	}
	
	public void setRegionId(String regionId) {
		this.regionId = regionId;
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
	
}