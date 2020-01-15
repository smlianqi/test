package com.elex.im.module.serverchat.module.chat.content;

import com.elex.im.module.serverchat.module.chat.type.ContentType;

/**
 * 内容接口
 * 
 * @author mausmars
 *
 */
public interface IContent {
	/**
	 * 核心内容
	 * 
	 * @return
	 */
	String getMsg();

	/**
	 * 获取原文
	 * 
	 * @return
	 */
	String getContent();

	/**
	 * content转json
	 * 
	 * @return
	 */
	String content2Json();

	/**
	 * 内容类型
	 * 
	 * @return
	 */
	ContentType contentType();
}
