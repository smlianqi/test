package com.elex.common.util.uuid;

import java.util.UUID;

/**
 * uuid
 * 
 * @author mausmars
 *
 */
public class UUIDUtil {
	public static String getUUID() {
		String id = UUID.randomUUID().toString().replace("-", "");
		return id;
	}
}
