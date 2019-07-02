package com.elex.im.module.serverchat.module.chat.content;

import java.util.LinkedList;
import java.util.List;

import com.elex.im.module.serverchat.module.chat.type.ContentType;

/**
 * 联盟操作内容
 * 
 * @author mausmars
 *
 */
public class AllianceOperationContent extends AContent {
	private String gdsId; // 字符串的gdsId
	private List<String> paramValues = new LinkedList<>();
	private List<Integer> paramValueTypes = new LinkedList<>();

	@Override
	public ContentType contentType() {
		return ContentType.AllianceOperation;
	}

	public void addParamValue(String paramValue, int contentValueType) {
		paramValues.add(paramValue);
		paramValueTypes.add(contentValueType);
	}

	public String getGdsId() {
		return gdsId;
	}

	public void setGdsId(String gdsId) {
		this.gdsId = gdsId;
	}

	public List<String> getParamValues() {
		return paramValues;
	}

	public void setParamValues(List<String> params) {
		this.paramValues = params;
	}

	public List<Integer> getParamValueTypes() {
		return paramValueTypes;
	}

	public void setParamValueTypes(List<Integer> paramValueTypes) {
		this.paramValueTypes = paramValueTypes;
	}
}
