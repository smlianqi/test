package com.elex.common.component.function.config;

import java.io.IOException;
import java.util.Map;

import com.elex.common.component.data.ISpread;
import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.component.io.IWriteReadable;

/**
 * 功能配置(类型201) 实体类
 */
public class ScFunctionGeneral implements ISpread, IWriteReadable {
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
	 * 根路径名
	 */
	protected String rootPath;
	
	/**
	 * 组过滤
	 */
	protected String groupFilters;
	
	/**
	 * 区过滤
	 */
	protected String regionFilters;
	
	/**
	 * 功能管理配置
	 */
	protected String functionFilters;
	
	@Override
	public void write(IDataOutput out) throws IOException {
			out.writeUTF(id);
			out.writeUTF(dependIds);
			out.writeUTF(readme);
			out.writeUTF(extraParams);
			out.writeUTF(rootPath);
			out.writeUTF(groupFilters);
			out.writeUTF(regionFilters);
			out.writeUTF(functionFilters);
	}

	@Override
	public void read(IDataInput in) throws IOException {
			id=in.readUTF();
			dependIds=in.readUTF();
			readme=in.readUTF();
			extraParams=in.readUTF();
			rootPath=in.readUTF();
			groupFilters=in.readUTF();
			regionFilters=in.readUTF();
			functionFilters=in.readUTF();
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
		sb.append("ScFunctionGeneral [");
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
		sb.append("rootPath=");
		sb.append(rootPath);
		sb.append(", ");
		sb.append("groupFilters=");
		sb.append(groupFilters);
		sb.append(", ");
		sb.append("regionFilters=");
		sb.append(regionFilters);
		sb.append(", ");
		sb.append("functionFilters=");
		sb.append(functionFilters);
		
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
	
	public String getRootPath() {
		return rootPath;
	}
	
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	
	public String getGroupFilters() {
		return groupFilters;
	}
	
	public void setGroupFilters(String groupFilters) {
		this.groupFilters = groupFilters;
	}
	
	public String getRegionFilters() {
		return regionFilters;
	}
	
	public void setRegionFilters(String regionFilters) {
		this.regionFilters = regionFilters;
	}
	
	public String getFunctionFilters() {
		return functionFilters;
	}
	
	public void setFunctionFilters(String functionFilters) {
		this.functionFilters = functionFilters;
	}
	
}