package com.elex.im.module.servertest.rpcservice.bean;

import java.io.IOException;
import java.util.Map;

import com.elex.common.component.data.ISpread;
import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.component.io.IWriteReadable;

/**
 * 测试 实体类
 */
public class TestRpcBeanGeneral implements ISpread, IWriteReadable {
	/**
	 * id
	 */
	protected int id;

	/**
	 * 名字
	 */
	protected String name;

	/**
	 * 性别
	 */
	protected int sex;

	@Override
	public void write(IDataOutput out) throws IOException {
		out.writeInt(id);
		out.writeUTF(name);
		out.writeInt(sex);
	}

	@Override
	public void read(IDataInput in) throws IOException {
		id = in.readInt();
		name = in.readUTF();
		sex = in.readInt();
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

	public void obtainAfter() {
	}

	public void saveBefore() {
	}

	public void saveAfter() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	@Override
	public Map<String, Object> getIndexChangeBefore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T cloneEntity(boolean isSaveBefore) {
		return null;
	}

}