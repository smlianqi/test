package com.elex.common.component.member.data;

import java.util.List;

import com.elex.common.component.data.IDao;

public interface IMemberOnlineDao extends IDao<MemberOnline> {
	MemberOnline selectByMemberIdAndFunctionType(String memberId, String functionType);

	// List<MemberOnline> selectByMemberId(String memberId);

	List<MemberOnline> selectBySidAndMemberTypeAndFunctionType(String sid, String memberType, String functionType);

	// List<MemberOnline> selectBySid(String sid);
}
