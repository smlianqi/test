package com.elex.common.net.session;

import com.elex.common.net.IConnect;

/**
 * 连接池
 * 
 * @author mausmars
 *
 */
public interface ISessionPool extends IConnect {
	void release();

	/**
	 * 绑定，服务器端
	 * 
	 * @param index
	 * @param session
	 */
	void bindingServer(int index, ISession session);

	/**
	 * 绑定，客户端端
	 * 
	 * @param index
	 * @param session
	 */
	void bindingClient(int index, String sid, String token, ISession session);

	/**
	 * 关闭
	 */
	void close();

	int size();

	/**
	 * 获取散列索引
	 * 
	 * @param key
	 * @return
	 */
	int getIndex(String key);

	/**
	 * 获取session
	 * 
	 * @param index
	 * @return
	 */
	ISession getSession(int index);

	ISession getRandomSession();

	/**
	 * 指定散列发送
	 * 
	 * @param message
	 */
	void send(String key, Object msg);

	/**
	 * 指定索引发送
	 * 
	 * @param message
	 */
	void send(int index, Object msg);

	/**
	 * 指定发送固定
	 * 
	 * @param msg
	 */
	void send(Object msg);

	/**
	 * 随机发送
	 * 
	 * @param msg
	 */
	void sendRandom(Object msg);

}
