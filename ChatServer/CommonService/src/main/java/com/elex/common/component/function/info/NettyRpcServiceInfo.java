package com.elex.common.component.function.info;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.component.io.IWriteReadable;
import com.elex.common.component.rpc.IRpcFServicePrx;

/**
 * netty rpc服务信息
 * 
 * @author mausmars
 *
 */
public class NettyRpcServiceInfo implements IServiceInfo {
	/** 包名 */
	private String packageName;
	/** 类名 */
	private String className;
	/** 对象id */
	private String objectId = "";
	/** 对象名 */
	private String objectName;//
	/** 指令映射方法 */
	private Map<Integer, MethodInfo> methodMap = new HashMap<Integer, MethodInfo>();

	/** 服务代理 */
	private IRpcFServicePrx servicePrx;

	NettyRpcServiceInfo() {
	}

	public NettyRpcServiceInfo(IDataInput in) throws IOException {
		this.read(in);
	}

	@Override
	public void release() {
	}

	@Override
	public IServiceInfo cloneObj() {
		NettyRpcServiceInfo obj = new NettyRpcServiceInfo();
		obj.packageName = packageName;
		obj.className = className;
		obj.objectId = objectId;
		obj.objectName = objectName;
		return obj;
	}

	@Override
	public FServiceType getFSType() {
		return FServiceType.NettyRpc;
	}

	@Override
	public <T> T getService() {
		return (T) servicePrx;
	}

	public NettyRpcServiceInfo(String packageName, String className, String objectId, String objectName,
			Map<Integer, MethodInfo> methodMap) {
		this.packageName = packageName;
		this.className = className;
		this.objectId = objectId;
		this.objectName = objectName;
		this.methodMap = methodMap;
	}

	@Override
	public void write(IDataOutput out) throws IOException {
		out.writeUTF(packageName);
		out.writeUTF(className);
		out.writeUTF(objectId);
		out.writeUTF(objectName);
		out.writeInt(methodMap.size());
		for (Entry<Integer, MethodInfo> entry : methodMap.entrySet()) {
			out.writeInt(entry.getKey());
			entry.getValue().write(out);
		}
	}

	@Override
	public void read(IDataInput in) throws IOException {
		packageName = in.readUTF();
		className = in.readUTF();
		objectId = in.readUTF();
		objectName = in.readUTF();
		int size = in.readInt();
		for (int i = 0; i < size; i++) {
			int key = in.readInt();
			MethodInfo methodInfo = new MethodInfo();
			methodInfo.read(in);
			methodMap.put(key, methodInfo);
		}
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

	public void setServicePrx(IRpcFServicePrx servicePrx) {
		this.servicePrx = servicePrx;
	}
}

class MethodInfo implements IWriteReadable {
	private String methodName = "";// 对象名
	private List<String> paramClassNames = new LinkedList<String>();// 参数类名

	public MethodInfo() {
	}

	public MethodInfo(byte[] bytes) throws IOException {
		initField(bytes);
	}

	public void write(IDataOutput out) throws IOException {
		out.writeUTF(methodName);
		out.writeInt(paramClassNames.size());
		for (String paramClassName : paramClassNames) {
			out.writeUTF(paramClassName);
		}
	}

	public void read(IDataInput in) throws IOException {
		methodName = in.readUTF();
		int size = in.readInt();
		for (int i = 0; i < size; i++) {
			String paramClassName = in.readUTF();
			paramClassNames.add(paramClassName);
		}
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

	// --------------------------------
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void addParamClassNames(String paramClassName) {
		paramClassNames.add(paramClassName);
	}

	public void setParamClassNames(List<String> paramClassNames) {
		this.paramClassNames = paramClassNames;
	}

	@Override
	public String toString() {
		return "MethodConfig [methodName=" + methodName + ", paramClassNames=" + paramClassNames + "]";
	}
}
