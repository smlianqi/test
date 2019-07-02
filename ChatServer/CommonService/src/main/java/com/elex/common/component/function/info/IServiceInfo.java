package com.elex.common.component.function.info;

import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.io.IWriteReadable;

/**
 * 服务接口
 * 
 * @author mausmars
 *
 */
public interface IServiceInfo extends IWriteReadable {
	FServiceType getFSType();

	/**
	 * 获得发送接口，rpc为代理接口，netty服务为sessionpool
	 * 
	 * @return
	 */
	<T> T getService();

	IServiceInfo cloneObj();

	void release();
}
