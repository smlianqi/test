package com.elex.common.component.function.info;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 功能信息
 * 
 * @author mausmars
 *
 */
public class FunctionInfo implements IFunctionInfo {
	protected static final ILogger logger = XLogUtil.logger();

	private FunctionId functionId;
	/** TODO 暂时每个类型就一个{FServiceType,服务信息} */
	private ConcurrentMap<FServiceType, FService> serviceMap;

	public FunctionInfo() {
		this.serviceMap = new ConcurrentHashMap<FServiceType, FService>();
	}

	public FunctionInfo(FunctionId functionId) {
		this.functionId = functionId;
		this.serviceMap = new ConcurrentHashMap<FServiceType, FService>();
	}

	public FunctionInfo(IDataInput in) throws IOException {
		this.read(in);
	}

	public FunctionInfo(byte[] bytes) throws IOException {
		this.initField(bytes);
	}

	public FunctionInfo(FunctionId functionId, ConcurrentHashMap<FServiceType, FService> serviceMap) {
		this.functionId = functionId;
		this.serviceMap = serviceMap;
	}

	@Override
	public void release() {
		for (FService service : serviceMap.values()) {
			service.release();
		}
	}

	public void putFService(FService service) {
		serviceMap.put(service.getFsType(), service);
	}

	public FService putIfAbsentFService(FService service) {
		FService s = serviceMap.putIfAbsent(service.getFsType(), service);
		if (s == null) {
			s = service;
		}
		return s;
	}

	public static FunctionInfo createFunctionInfo(String fid, byte[] bytes) throws IOException {
		FunctionId functionId = new FunctionId();
		functionId.setFid(fid);

		FunctionInfo functionInfo = new FunctionInfo(bytes);
		functionInfo.setFunctionId(functionId);
		return functionInfo;
	}

	@Override
	public void write(IDataOutput out) throws IOException {
		out.writeInt(serviceMap.size());
		for (FService serviceInfo : serviceMap.values()) {
			serviceInfo.write(out);
		}
	}

	@Override
	public void read(IDataInput in) throws IOException {
		int size = in.readInt();
		serviceMap = new ConcurrentHashMap<FServiceType, FService>();
		for (int i = 0; i < size; i++) {
			FService service = new FService(in);
			serviceMap.put(service.getFsType(), service);
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

	public void setFunctionId(FunctionId functionId) {
		this.functionId = functionId;
	}

	// ------------------------
	@Override
	public FunctionId getFunctionId() {
		return functionId;
	}

	@Override
	public <T extends IServiceInfo> T getServiceInfo(FServiceType fsType, String name) {
		FService service = serviceMap.get(fsType);
		if (service == null) {
			return null;
		}
		return (T) service.getServiceInfo(name);
	}

	@Override
	public <T> T getService(FServiceType fsType, String name) {
		FService service = serviceMap.get(fsType);
		if (service == null) {
			return null;
		}
		IServiceInfo serviceInfo = service.getServiceInfo(name);
		if (serviceInfo == null) {
			return null;
		}
		return serviceInfo.getService();
	}

	@Override
	public Collection<FService> getFServices() {
		return serviceMap.values();
	}

	@Override
	public FService getFService(FServiceType fsType) {
		return serviceMap.get(fsType);
	}

	@Override
	public String toString() {
		return "FunctionInfo [functionId=" + functionId + ", serviceMap=" + serviceMap + "]";
	}
}
