package com.elex.common.component.function.info;

import java.io.IOException;
import java.util.Collection;

import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.io.IWriteReadable;

/**
 * 功能信息
 * 
 * @author mausmars
 *
 */
public interface IFunctionInfo extends IWriteReadable {
	/**
	 * 释放资源
	 */
	void release();

	/**
	 * 获取功能id
	 * 
	 * @return
	 */
	FunctionId getFunctionId();

	void putFService(FService service);

	FService putIfAbsentFService(FService service);

	/**
	 * 获取序列化数据
	 * 
	 * @return
	 * @throws IOException
	 */
	byte[] getBytes() throws IOException;

	/**
	 * 
	 * @return
	 */
	<T extends IServiceInfo> T getServiceInfo(FServiceType fsType, String name);

	<T> T getService(FServiceType fsType, String name);

	Collection<FService> getFServices();

	FService getFService(FServiceType fsType);
}
