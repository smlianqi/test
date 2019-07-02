package com.elex.common.component.function.info;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.component.io.IWriteReadable;

/**
 * 服务
 * 
 * @author mausmars
 *
 */
public class FService implements IWriteReadable {
	/** key唯一 定常量 */
	// private String key;
	/** 类型 */
	private FServiceType fsType;
	/** 服务地址 */
	private ServiceAddress serviceAddress;
	/** {接口类名ClassName 或 服务名，服务信息} */
	private ConcurrentMap<String, IServiceInfo> serviceInfoMap;

	FService() {
		this.serviceInfoMap = new ConcurrentHashMap<>();
	}

	public FService(IDataInput in) throws IOException {
		this.read(in);
	}

	public FService(FServiceType fsType, ServiceAddress serviceAddress) {
		// this.key = key;
		this.fsType = fsType;
		this.serviceAddress = serviceAddress;
		this.serviceInfoMap = new ConcurrentHashMap<>();
	}

	public void release() {
		for (IServiceInfo serviceInfo : serviceInfoMap.values()) {
			serviceInfo.release();
		}
	}

	public FService cloneObj() {
		FService obj = new FService();
		obj.setFsType(fsType);
		obj.setServiceAddress(serviceAddress);

		if (fsType == FServiceType.NettyForward) {
			for (Entry<String, IServiceInfo> entry : serviceInfoMap.entrySet()) {
				IServiceInfo v = entry.getValue().cloneObj();
				obj.putServiceInfo(entry.getKey(), v);
			}
		}
		return obj;
	}

	public <T extends IServiceInfo> T getServiceInfo(String name) {
		return (T) serviceInfoMap.get(name);
	}

	public <T> T getService(String name) {
		return getServiceInfo(name).getService();
	}

	@Override
	public void write(IDataOutput out) throws IOException {
		out.writeUTF(fsType.name());
		serviceAddress.write(out);
		out.writeInt(serviceInfoMap.size());
		for (Entry<String, IServiceInfo> entry : serviceInfoMap.entrySet()) {
			out.writeUTF(entry.getKey());
			entry.getValue().write(out);
		}
	}

	@Override
	public void read(IDataInput in) throws IOException {
		fsType = FServiceType.valueOf(in.readUTF());
		ServiceAddress sa = new ServiceAddress();
		sa.read(in);
		serviceAddress = sa;
		int size = in.readInt();
		this.serviceInfoMap = new ConcurrentHashMap<>();
		for (int i = 0; i < size; i++) {
			String k = in.readUTF();
			IServiceInfo serviceInfo = ServiceInfoFactory.create(fsType, in);
			serviceInfoMap.put(k, serviceInfo);
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

	public FServiceType getFsType() {
		return fsType;
	}

	public void setFsType(FServiceType fsType) {
		this.fsType = fsType;
	}

	public ServiceAddress getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(ServiceAddress serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public void putServiceInfo(String key, IServiceInfo serviceInfo) {
		serviceInfoMap.put(key, serviceInfo);
	}

	public Map<String, IServiceInfo> getServiceInfoMap() {
		return serviceInfoMap;
	}

	@Override
	public String toString() {
		return "FService [fsType=" + fsType + ", serviceAddress=" + serviceAddress + ", serviceInfoMap="
				+ serviceInfoMap + "]";
	}
}
