package com.elex.common.net.session;

import com.elex.common.net.IConnect;
import com.elex.common.net.type.SessionAttachType;

/**
 * seesion 接口
 * 
 * @author mausmars
 * 
 * 
 */
public interface ISession extends IConnect {
	/**
	 * 直接写入filter层
	 * 
	 * @param msg
	 */
	void write(Object msg);

	/**
	 * 直接过消息转换层
	 * 
	 * @param msg
	 * @return
	 */
	void send(Object msg);

	/**
	 * 关闭
	 * 
	 * @param immediately
	 */
	void close();

	/**
	 * 获取sessionId
	 * 
	 * @return
	 */
	String getSessionId();

	/**
	 * session是否为打开状态
	 * 
	 * @return
	 */
	boolean isOpen();

	/**
	 * 设置附加属性
	 * 
	 * @param key
	 * @param attach
	 * @return
	 */
	void setAttach(SessionAttachType key, Object attach);

	/**
	 * 
	 * 获取附加值
	 * 
	 * @param key
	 * @return
	 */
	<T> T getAttach(SessionAttachType key);

	/**
	 * 替换连接
	 * 
	 * @param channel
	 */
	void replaceChannel(Object channel);
}
