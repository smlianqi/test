package com.elex.common.component.data.cache;

import java.lang.reflect.Field;
import java.util.Map;

import com.elex.common.util.string.StringUtil;

/**
 * 索引查询组合模板
 * 
 * @author mausmars
 *
 */
public class CKeyTemplate {
	private String[] fieldNames; // 索引对应的属性名
	private CacheKeyType cacheKeyType; // 索引类型

	// {field1:%s,field2:%s......}
	private String template;

	// {%s_%s..}
	private String fieldValueTemplate;

	public CKeyTemplate(CacheKeyType cacheKeyType, String[] fieldNames) {
		this.cacheKeyType = cacheKeyType;
		this.fieldNames = fieldNames;

		// 创建模板
		this.template = getFieldTemplate(fieldNames);
		this.fieldValueTemplate = getFieldValueTemplate(fieldNames);
	}

	public String createOldFieldKey(Object obj, Map<String, Object> indexChangeBefore) {
		Object[] params = new Object[fieldNames.length];
		int i = 0;
		try {
			for (String needFieldName : fieldNames) {
				Object oldValue = indexChangeBefore.get(needFieldName);

				if (oldValue == null) {
					Field field = obj.getClass().getSuperclass().getDeclaredField(needFieldName);
					// Field field =
					// obj.getClass().getDeclaredField(needFieldName);
					field.setAccessible(true);
					params[i] = String.valueOf(field.get(obj));
				} else {
					params[i] = oldValue;
				}
				i++;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return String.format(template, params);
	}

	public String createFieldKey(Object obj) {
		Object[] params = getFieldValue(obj);
		return String.format(template, params);
	}

	// 域的值
	public String createFieldValue(Object obj) {
		Object[] params = getFieldValue(obj);
		return String.format(fieldValueTemplate, params);
	}

	private Object[] getFieldValue(Object obj) {
		Object[] params = new Object[fieldNames.length];
		int i = 0;
		try {
			for (String needFieldName : fieldNames) {
				Field field = obj.getClass().getSuperclass().getDeclaredField(needFieldName);
				// Field field = obj.getClass().getDeclaredField(needFieldName);
				field.setAccessible(true);
				params[i] = String.valueOf(field.get(obj));
				i++;
			}
			return params;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String createFieldKey(Object[] params) {
		for (int i = 0; i < params.length; i++) {
			params[i] = String.valueOf(params[i]);
		}
		return String.format(template, params);
	}

	private String getFieldTemplate(String[] fieldNames) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0, lastIndex = fieldNames.length - 1; i < fieldNames.length; i++) {
			sb.append(fieldNames[i]);
			sb.append(":");
			sb.append("%s");

			if (i < lastIndex) {
				sb.append(StringUtil.Comma);
			}
		}
		sb.append("}");
		return sb.toString();
	}

	private String getFieldValueTemplate(String[] fieldNames) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, lastIndex = fieldNames.length - 1; i < fieldNames.length; i++) {
			sb.append("%s");
			if (i < lastIndex) {
				sb.append(StringUtil.SeparatorUnderline);
			}
		}
		return sb.toString();
	}

	public CacheKeyType getCacheKeyType() {
		return cacheKeyType;
	}

	public String getTemplate() {
		return template;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((template == null) ? 0 : template.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CKeyTemplate other = (CKeyTemplate) obj;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		return true;
	}

}
