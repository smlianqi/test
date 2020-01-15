package com.elex.common.component.function.info;

import java.io.IOException;

import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.io.IDataInput;

public class ServiceInfoFactory {
	public static IServiceInfo create(FServiceType fsType, IDataInput in) throws IOException {
		IServiceInfo serviceInfo = null;
		switch (fsType) {
		case IceRpc:
			serviceInfo = new IceRpcServiceInfo(in);
			break;
		case NettyRpc:
			serviceInfo = new NettyRpcServiceInfo(in);
			break;
		case NettyForward:
			serviceInfo = new NettyForwardServiceInfo(in);
			break;
		default:
			break;
		}
		return serviceInfo;
	}
}
