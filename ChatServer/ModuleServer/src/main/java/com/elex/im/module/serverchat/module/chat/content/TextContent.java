package com.elex.im.module.serverchat.module.chat.content;

import com.elex.im.module.serverchat.module.chat.type.ContentType;

/**
 * 文本内容
 * 
 * {"msg":"xx{0}xx{1}xxx","parameters":["0","1"]}
 * 
 * @author mausmars
 *
*/
public class TextContent extends AContent {
	// TODO 暂时关闭这个参数，用的时候开
	// private List<String> parameters;

	public TextContent() {
	}

	public TextContent(String msg) {
		this.msg = msg;
	}

	@Override
	public ContentType contentType() {
		return ContentType.Text;
	}

	// public List<String> getParameters() {
	// return parameters;
	// }
	//
	// public void setParameters(List<String> parameters) {
	// this.parameters = parameters;
	// }
}
