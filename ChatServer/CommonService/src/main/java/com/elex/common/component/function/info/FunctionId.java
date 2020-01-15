package com.elex.common.component.function.info;

import java.io.IOException;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.component.io.IWriteReadable;
import com.elex.common.util.string.StringUtil;

/**
 * 功能id
 * 
 * @author mausmars
 *
 */
public class FunctionId implements IWriteReadable {
	/** 自动id */
	private String aotuId;
	/** 组id */
	private String groupId;
	/** 区id */
	private String regionId;
	/** 功能类型 */
	private FunctionType functionType;

	public FunctionId() {
	}

	public FunctionId(String groupId, String regionId, FunctionType functionType) {
		this.groupId = groupId;
		this.regionId = regionId;
		this.functionType = functionType;
	}

	public static FunctionId createFunctionId(String fid) {
		FunctionId functionId = new FunctionId();
		functionId.setFid(fid);
		return functionId;
	}

	public FunctionId(byte[] bytes) throws IOException {
		initField(bytes);
	}

	public void write(IDataOutput out) throws IOException {
		out.writeUTF(aotuId);
		out.writeUTF(groupId);
		out.writeUTF(regionId);
		out.writeUTF(functionType.name());
	}

	public void read(IDataInput in) throws IOException {
		aotuId = in.readUTF();
		groupId = in.readUTF();
		regionId = in.readUTF();
		functionType = FunctionType.valueOf(in.readUTF());
	}

	public void initField(byte[] bytes) throws IOException {
		ByteArrayInput byteArrayInput = new ByteArrayInput(bytes);
		this.read(byteArrayInput);
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutput out = new ByteArrayOutput();
		write(out);
		return out.toByteArray();
	}

	/**
	 * 这里用#拼字符串作为sid的节点名
	 * 
	 * @return
	 */
	public String getFid(boolean isContainAotuId) {
		StringBuilder sb = new StringBuilder();
		sb.append(groupId);
		sb.append(StringUtil.SeparatorSid);
		sb.append(regionId);
		sb.append(StringUtil.SeparatorSid);
		sb.append(functionType.name());
		if (isContainAotuId) {
			sb.append(StringUtil.SeparatorAt);
			sb.append(aotuId);
		}
		return sb.toString();
	}

	public static String createSid(String groupId, String regionId, FunctionType functionType, String serverId) {
		StringBuilder sb = new StringBuilder();
		sb.append(groupId);
		sb.append(StringUtil.SeparatorSid);
		sb.append(regionId);
		sb.append(StringUtil.SeparatorSid);
		sb.append(functionType.name());
		sb.append(StringUtil.SeparatorAt);
		sb.append(serverId);
		return sb.toString();
	}

	public void setFid(String fid) {
		// 这里可能要处理带下划线的fid
		String[] fids = fid.split(StringUtil.SeparatorAt);
		if (fids.length > 1) {
			aotuId = fids[1];
		}
		fids = fids[0].split(StringUtil.SeparatorSid);
		groupId = fids[0];
		regionId = fids[1];
		functionType = FunctionType.valueOf(fids[2]);
	}

	public String getGroupId() {
		return groupId;
	}

	public FunctionType getFunctionType() {
		return functionType;
	}

	public String getRegionId() {
		return regionId;
	}

	public String getAotuId() {
		return aotuId;
	}

	public void setAotuId(String aotuId) {
		this.aotuId = aotuId;
	}

	@Override
	public String toString() {
		return "FunctionId [aotuId=" + aotuId + ", groupId=" + groupId + ", regionId=" + regionId + ", functionType="
				+ functionType + "]";
	}
}
