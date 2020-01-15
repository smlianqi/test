package com.elex.common.component.data.cache;

/**
 * 缓存key
 * 
 * @author mausmars
 *
 */
public class CacheKey {
	private ICacheKeyTemplate cacheKeyTemplate;

	/** class 的 SimpleName */
	private String tableName; // 表
	private CacheKey mainCacheKey;// 对应的主索引

	private CacheKeyType cacheKeyType;// 索引类型
	private String field;// 索引key（通过模板赋值完的）
	private String oldField;// 旧索引key（通过模板赋值完的）

	private String mainField;// 主域
	private String oldMainField;// 主域

	public CacheKey(ICacheKeyTemplate cacheKeyTemplate) {
		this.cacheKeyTemplate = cacheKeyTemplate;
		this.tableName=cacheKeyTemplate.getClass().getSimpleName();
	}

	public String getFieldKey() {
		return getTypeStr(new String[] { tableName, field });
	}

	public String getOldFieldKey() {
		return getTypeStr(new String[] { tableName, oldField });
	}

	public String getTableKey() {
		return getTypeStr(new String[] { tableName, cacheKeyType.suffix });
	}

	public String getMainFieldKey() {
		if (mainField == null) {
			return null;
		}
		return getTypeStr(new String[] { tableName, mainField, cacheKeyType.suffix });
	}

	public static String getKey(String tableName, CacheKeyType cacheKeyType) {
		return getTypeStr(new String[] { tableName, cacheKeyType.suffix });
	}

	public static String getTableKey(String tableName, CacheKeyType cacheKeyType) {
		return getTypeStr(new String[] { tableName, cacheKeyType.suffix });
	}

	public static String getMainFieldKey(String tableName, String mainField, CacheKeyType cacheKeyType) {
		return getTypeStr(new String[] { tableName, mainField, cacheKeyType.suffix });
	}

	private static String getTypeStr(String[] strs) {
		StringBuilder sb = new StringBuilder();
		int lastIndex = strs.length - 1;
		for (int i = 0; i < strs.length; i++) {
			sb.append(strs[i]);
			if (i < lastIndex) {
				sb.append("_");
			}
		}
		return sb.toString();
	}

	public String getField() {
		return field;
	}

	public byte[] getFieldByte() {
		return field.getBytes();
	}

	public CacheKey getMainCacheKey() {
		return mainCacheKey;
	}

	public CacheKeyType getCacheKeyType() {
		return cacheKeyType;
	}

	public String getOldField() {
		return oldField;
	}

	public byte[] getOldFieldByte() {
		return oldField.getBytes();
	}

	public void setOldField(String oldField) {
		this.oldField = oldField;
	}

	public ICacheKeyTemplate getCacheKeyTemplate() {
		return cacheKeyTemplate;
	}

	public String getMainField() {
		return mainField;
	}

	public void setMainField(String mainField) {
		this.mainField = mainField;
	}

	public String getOldMainField() {
		return oldMainField;
	}

	public void setOldMainField(String oldMainField) {
		this.oldMainField = oldMainField;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setCacheKeyType(CacheKeyType cacheKeyType) {
		this.cacheKeyType = cacheKeyType;
	}

	public void setMainCacheKey(CacheKey mainCacheKey) {
		this.mainCacheKey = mainCacheKey;
	}

	public String getTableName() {
		return tableName;
	}
}
