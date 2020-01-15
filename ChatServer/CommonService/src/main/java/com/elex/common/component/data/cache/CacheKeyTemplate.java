package com.elex.common.component.data.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.elex.common.component.data.ISpread;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.common.util.string.StringUtil;

/**
 * CacheKey模板实现
 * 
 * @author mausmars
 *
 */
public class CacheKeyTemplate implements ICacheKeyTemplate {
	protected static final ILogger logger = XLogUtil.logger();

	private Class<?> cls;
	// {field1,field2... : ckey模板}
	private Map<String, CKeyTemplate> fieldsCKTMap;
	// {CacheKeyType : ckey模板集合}
	private Map<CacheKeyType, List<CKeyTemplate>> fieldsCKTTypeMap;
	// {某个域集field:ckey模板} 和某个域相关的索引
	private Map<String, List<CKeyTemplate>> fieldCKTMap;

	// 主域，按主域分类，方便删除一类数据。（比如以玩家id分类）
	private CKeyTemplate mainFieldTemplate;

	public CacheKeyTemplate(Class<?> cls) {
		this.cls = cls;
		this.fieldsCKTMap = new HashMap<>();
		this.fieldCKTMap = new HashMap<>();
		this.fieldsCKTTypeMap = new HashMap<>();
	}

	@Override
	public boolean isContainMainField() {
		return mainFieldTemplate != null;
	}

	@Override
	public String createMainFieldValue(Object obj) {
		return mainFieldTemplate.createFieldValue(obj);
	}

	@Override
	public Class<?> getCls() {
		return cls;
	}

	private CacheKey createCacheKey(CKeyTemplate ckt, Object obj, CacheKey mainKey,
			Map<String, Object> indexChangeBefore) {
		String fieldKey = ckt.createFieldKey(obj);

		CacheKey ck = new CacheKey(this);
		ck.setCacheKeyType(ckt.getCacheKeyType());
		ck.setField(fieldKey);
		ck.setMainCacheKey(mainKey);

		if (mainFieldTemplate != null) {
			ck.setMainField(mainFieldTemplate.createFieldKey(obj));
		}
		if (indexChangeBefore != null) {
			String oldFieldKey = ckt.createOldFieldKey(obj, indexChangeBefore);
			ck.setOldField(oldFieldKey);
		}
		return ck;
	}

	private CacheKey createCacheKey(CKeyTemplate ckt, Object[] fieldKeyParams, CacheKey mainKey) {
		String fieldKey = ckt.createFieldKey(fieldKeyParams);

		CacheKey ck = new CacheKey(this);
		ck.setCacheKeyType(ckt.getCacheKeyType());
		ck.setField(fieldKey);
		ck.setMainCacheKey(mainKey);
		return ck;
	}

	@Override
	public List<CacheKey> getUpdateCacheKeys(Object obj) {
		List<CacheKey> cacheKeys = new LinkedList<CacheKey>();

		ISpread spreadPojo = (ISpread) obj;

		Map<String, Object> indexChangeBefore = spreadPojo.getIndexChangeBefore();
		Collection<CKeyTemplate> ckts = getCKeyTemplates(indexChangeBefore.keySet());

		CacheKey mainKey = null;
		for (CKeyTemplate ckt : ckts) {
			if (ckt.getCacheKeyType() == CacheKeyType.CK_Main) {
				mainKey = createCacheKey(ckt, obj, null, indexChangeBefore);
				cacheKeys.add(mainKey);
			}
		}
		for (CKeyTemplate ckt : ckts) {
			if (ckt.getCacheKeyType() != CacheKeyType.CK_Main) {
				// mainKey为空，证明主索引没变化
				CacheKey ck = createCacheKey(ckt, obj, mainKey, indexChangeBefore);
				cacheKeys.add(ck);
			}
		}
		return cacheKeys;
	}

	@Override
	public List<CacheKey> getCacheKeys(Object obj, CacheKeyType cacheKeyType) {
		List<CacheKey> cacheKeys = new LinkedList<CacheKey>();

		if (cacheKeyType == CacheKeyType.CK_Main) {
			CacheKey mainKey = getMainCacheKeys(obj);
			cacheKeys.add(mainKey);
		} else {
			CacheKey mainKey = getMainCacheKeys(obj);
			List<CKeyTemplate> keyTemplates = fieldsCKTTypeMap.get(cacheKeyType);
			for (CKeyTemplate ckt : keyTemplates) {
				CacheKey ck = createCacheKey(ckt, obj, mainKey, null);
				cacheKeys.add(ck);
			}
		}
		return cacheKeys;
	}

	@Override
	public CacheKey getCacheKeys(String[] fieldNames, Object[] fieldKeyParams) {
		String cktKey = getCKTKey(fieldNames);
		CKeyTemplate ckt = fieldsCKTMap.get(cktKey);
		return createCacheKey(ckt, fieldKeyParams, null);
	}

	@Override
	public CacheKey getMainCacheKeys(Object obj) {
		CKeyTemplate mainCKT = getMainCKeyTemplate();
		return createCacheKey(mainCKT, obj, null, null);
	}

	@Override
	public CacheKey getMainCacheKeys(Object[] fieldKeyParams) {
		CKeyTemplate mainCKT = getMainCKeyTemplate();
		return createCacheKey(mainCKT, fieldKeyParams, null, null);
	}

	@Override
	public List<CacheKey> getAllCacheKeys(Object obj) {
		List<CacheKey> cacheKeys = new LinkedList<CacheKey>();

		CacheKey mainKey = getMainCacheKeys(obj);
		cacheKeys.add(mainKey);

		for (CKeyTemplate ckt : fieldsCKTMap.values()) {
			if (ckt.getCacheKeyType() != CacheKeyType.CK_Main) {
				CacheKey ck = createCacheKey(ckt, obj, mainKey, null);
				cacheKeys.add(ck);
			}
		}
		return cacheKeys;
	}

	private CKeyTemplate getMainCKeyTemplate() {
		List<CKeyTemplate> keyTemplates = fieldsCKTTypeMap.get(CacheKeyType.CK_Main);
		return keyTemplates.get(0);
	}

	public void addTemplate(CacheKeyType cacheKeyType, String[] fieldNames) {
		CKeyTemplate ckt = new CKeyTemplate(cacheKeyType, fieldNames);

		String cktKey = getCKTKey(fieldNames);
		fieldsCKTMap.put(cktKey, ckt);

		List<CKeyTemplate> keyTemplates = fieldsCKTTypeMap.get(cacheKeyType);
		if (keyTemplates == null) {
			keyTemplates = new LinkedList<CKeyTemplate>();
			fieldsCKTTypeMap.put(cacheKeyType, keyTemplates);
		}
		keyTemplates.add(ckt);

		for (String fieldName : fieldNames) {
			List<CKeyTemplate> ckts = fieldCKTMap.get(fieldName);
			if (ckts == null) {
				ckts = new LinkedList<CKeyTemplate>();
				fieldCKTMap.put(fieldName, ckts);
			}
			ckts.add(ckt);
		}
	}

	private Collection<CKeyTemplate> getCKeyTemplates(Collection<String> fieldNames) {
		Set<CKeyTemplate> ckt = new HashSet<CKeyTemplate>();
		for (String fieldName : fieldNames) {
			ckt.addAll(fieldCKTMap.get(fieldName));
		}
		return ckt;
	}

	// private Collection<CKeyTemplate> getCKeyTemplates(String[] fieldNames) {
	// Set<CKeyTemplate> ckt = new HashSet<CKeyTemplate>();
	// for (String fieldName : fieldNames) {
	// ckt.addAll(fieldCKTMap.get(fieldName));
	// }
	// return ckt;
	// }

	// private Collection<CKeyTemplate> getCKeyTemplates(String fieldName) {
	// return fieldCKTMap.get(fieldName);
	// }

	public void testPrint() {
		for (CKeyTemplate kt : fieldsCKTMap.values()) {
			logger.info(kt.getCacheKeyType() + " " + kt.getTemplate());
		}
	}

	private String getCKTKey(String[] fieldNames) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, lastIndex = fieldNames.length - 1; i < fieldNames.length; i++) {
			sb.append(fieldNames[i]);
			if (i < lastIndex) {
				sb.append(StringUtil.Comma);
			}
		}
		return sb.toString();
	}

	// --------------------
	public void setMainField(String mainField) {
		this.mainFieldTemplate = new CKeyTemplate(null, new String[] { mainField });
	}
}
