package com.elex.im.module.serverchat.module.chat.content;

import com.elex.common.util.json.JsonUtil;

/**
 * 抽象内容
 * 
 * @author mausmars
 *
 */
public abstract class AContent implements IContent {
	protected String msg;
	protected transient String content;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String content2Json() {
		this.content = JsonUtil.gsonObj2String(this);
		return this.content;
	}
}
