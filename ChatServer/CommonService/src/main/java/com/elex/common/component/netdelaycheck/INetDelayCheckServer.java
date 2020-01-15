package com.elex.common.component.netdelaycheck;

/**
 * 服务端网络延迟
 * 
 * @author mausmars
 *
 */
public interface INetDelayCheckServer {
	/**
	 * 请求服务器时间
	 * 
	 * @param v
	 * @param ctime
	 * @return
	 */
	DelayInfo requestServerTime(long ctime);

	/**
	 * 返回客户端延迟
	 * 
	 * @param v
	 * @param ctime
	 * @return
	 */
	void replyClientDelay(int version, long ctime);

	/**
	 * 得到延迟
	 */
	int getDelayTime();
}
