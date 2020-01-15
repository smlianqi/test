package com.elex.common.component.member.data;

public class MemberOnlineKey {
	private String memberId;

	private String functionType;

	public MemberOnlineKey() {
	}

	public MemberOnlineKey(String memberId, String functionType) {
		this.memberId = memberId;
		this.functionType = functionType;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MemberOnlineKey key = (MemberOnlineKey) o;
		return memberId.equals(key.memberId) && functionType.equals(key.functionType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((functionType == null) ? 0 : functionType.hashCode());
		result = prime * result + ((memberId == null) ? 0 : memberId.hashCode());
		return result;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getFunctionType() {
		return functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}
}
