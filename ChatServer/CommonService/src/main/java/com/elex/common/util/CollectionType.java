package com.elex.common.util;

import java.util.ArrayList;
import java.util.List;

import com.elex.common.util.linklist.AbstractNode;

/**
 * 集合类型
 * 
 * @author mausmars
 *
 */
public class CollectionType extends AbstractNode {
	public static enum CollectionTypeEnum {
		List, //
		Set, //
		Map, //
		;
	}

	private CollectionTypeEnum type;
	private String suffix;

	private List<String> kvType = new ArrayList<String>();

	public CollectionType(CollectionTypeEnum type) {
		this.type = type;
	}

	public void addKVType(String kv) {
		kvType.add(kv);
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		return suffix;
	}

	public CollectionTypeEnum getType() {
		return type;
	}

	public List<String> getKvType() {
		return kvType;
	}

	public static CollectionType getCollectionType(String typeStr) {
		typeStr = typeStr.trim();
		int index = typeStr.indexOf("<");
		int lastIndex = typeStr.lastIndexOf(">");

		String type = typeStr.substring(0, index).trim();
		typeStr = typeStr.substring(index + 1, lastIndex).trim();
		CollectionTypeEnum ct = CollectionTypeEnum.valueOf(type);
		CollectionType collectionType = new CollectionType(ct);
		if (ct == CollectionTypeEnum.Map) {
			int i = typeStr.indexOf(",");
			collectionType.addKVType(typeStr.substring(0, i).trim());
			typeStr = typeStr.substring(i + 1).trim();
		}
		if (typeStr.contains("<")) {
			CollectionType nct = getCollectionType(typeStr);
			collectionType.snext(nct);
		} else {
			collectionType.addKVType(typeStr);
		}
		if (collectionType.getType() == CollectionTypeEnum.Map) {
			String kvt = collectionType.getKvType().get(0);
			collectionType.setSuffix("<" + kvt + "," + typeStr + ">");
		} else {
			collectionType.setSuffix("<" + typeStr + ">");
		}
		return collectionType;
	}

	public static void main(String args[]) {
		// String typeStr = "List<List<Integer>>";
		// CollectionType collectionType = getCollectionType(typeStr);
		// System.out.println(collectionType);
		String typeStr = "List<Map<String,List<Map<Integer,Integer>>>>";
		CollectionType collectionType = getCollectionType(typeStr);
		System.out.println(collectionType);

		for (;;) {
			System.out.println(
					collectionType.getType() + " " + collectionType.getKvType() + " " + collectionType.getSuffix());
			collectionType = (CollectionType) collectionType.next();
			if (collectionType == null) {
				break;
			}
		}
	}

	@Override
	public Object key() {
		return null;
	}

}
