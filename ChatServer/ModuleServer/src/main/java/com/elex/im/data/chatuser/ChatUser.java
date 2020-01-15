package com.elex.im.data.chatuser;

public class ChatUser {
	/** 用户信息 */
	private String uid;

	/** 用户类型 0-user,1-server,2-npc,3-gm */
	private int userType;

	/** 语言类型 */
	private String languageType;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getLanguageType() {
		return languageType;
	}

	public void setLanguageType(String languageType) {
		this.languageType = languageType;
	}
}
