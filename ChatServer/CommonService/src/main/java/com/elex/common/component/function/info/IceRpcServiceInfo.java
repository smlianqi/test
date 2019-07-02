package com.elex.common.component.function.info;

import java.io.IOException;

import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.component.rpc.IRpcFServicePrx;

/**
 * ice rpc服务信息
 * 
 * @author mausmars
 *
 */
public class IceRpcServiceInfo implements IServiceInfo {
	/** 包名 */
	private String packageName;
	/** 类名 */
	private String className;
	/** 对象id */
	private String objectId = "";
	/** 适配名 */
	private String adapterName;

	/** 服务代理 */
	private IRpcFServicePrx servicePrx;

	IceRpcServiceInfo() {
	}

	public IceRpcServiceInfo(IDataInput in) throws IOException {
		this.read(in);
	}

	@Override
	public void release() {
	}

	@Override
	public IServiceInfo cloneObj() {
		IceRpcServiceInfo obj = new IceRpcServiceInfo();
		obj.packageName = packageName;
		obj.className = className;
		obj.objectId = objectId;
		obj.adapterName = adapterName;
		return obj;
	}

	@Override
	public FServiceType getFSType() {
		return FServiceType.IceRpc;
	}

	public IceRpcServiceInfo(String packageName, String className, String objectId, String adapterName) {
		this.packageName = packageName;
		this.className = className;
		this.objectId = objectId;
		this.adapterName = adapterName;
	}

	@Override
	public void write(IDataOutput out) throws IOException {
		out.writeUTF(packageName);
		out.writeUTF(className);
		out.writeUTF(objectId);
		out.writeUTF(adapterName);
	}

	@Override
	public void read(IDataInput in) throws IOException {
		packageName = in.readUTF();
		className = in.readUTF();
		objectId = in.readUTF();
		adapterName = in.readUTF();
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

	public String getInterfaceInfos() {
		StringBuilder sb = new StringBuilder();
		sb.append("[\"");
		sb.append(packageName);
		sb.append("\",\"");
		sb.append(className);
		sb.append("\",\"");
		sb.append(objectId);
		sb.append("\"]");
		return sb.toString();
	}

	public String getObjectId() {
		return objectId;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}

	@Override
	public <T> T getService() {
		return (T) servicePrx;
	}

	public void setServicePrx(IRpcFServicePrx servicePrx) {
		this.servicePrx = servicePrx;
	}
}
