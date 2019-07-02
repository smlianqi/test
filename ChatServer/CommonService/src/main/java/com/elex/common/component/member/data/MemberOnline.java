package com.elex.common.component.member.data;

/**
 * 成员在线
 * 
 * @author mausmars
 *
 */
public class MemberOnline {
	/** 成员id */
	private String memberId;

	/** 成员类型 */
	private String memberType;

	/** 服务器功能类型 */
	private String functionType;

	/** 服务器id */
	private String sid;

	/** 登录时间 */
	private long loginTime;

	/** 扩展字段 */
	private String extend;

	public MemberOnline() {
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getFunctionType() {
		return functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	@Override
	public String toString() {
		return "MemberOnline [memberId=" + memberId + ", memberType=" + memberType + ", functionType=" + functionType
				+ ", sid=" + sid + ", loginTime=" + loginTime + ", extend=" + extend + "]";
	}
}