package com.elex.im.data.chatroom;

import org.apache.ignite.lang.IgniteBiPredicate;

public class ScanQueryFilter implements IgniteBiPredicate<String, ChatRoom>{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean apply(String key, ChatRoom value) {
		return true;
	}
}
