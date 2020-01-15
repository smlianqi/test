package com.elex.common.component.member;

import java.util.List;

import com.elex.common.component.function.info.IFunctionInfo;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.member.data.MemberOnline;
import com.elex.common.component.member.type.MemberType;
import com.elex.common.service.IService;

/**
 * 成员位置
 * 
 * @author mausmars
 *
 */
public interface IMemberService extends IService {
	/**
	 * 登录服务器
	 * 
	 * @param memberId
	 * @param functionType
	 * @param memberType
	 * @param sid
	 * @param attach
	 * @return
	 */
	MemberOnline loginLocalServer(String memberId, MemberType memberType, String extend);

	/**
	 * 登出服务器
	 * 
	 * @param memberId
	 * @return
	 */
	void logoutLocalServer(String memberId);

	IFunctionInfo getFunctionInfoByMemberId(String memberId, FunctionType functionType);

	/**
	 * 查询某个类型的成员，在某个功能服是否在线
	 * 
	 * @param memberId
	 * @param functionType
	 * @return
	 */
	MemberOnline getMemberOnlineByMemberIdAndFunctionType(String memberId, String functionType);

	/**
	 * 查询成员的全部在线信息
	 * 
	 * @param memberId
	 * @return
	 */
	// List<MemberOnline> getMemberOnlineByMemberId(String memberId);

	/**
	 * 查询某个sid服的
	 * 
	 * @param sid
	 * @param memberType
	 * @return
	 */
	List<MemberOnline> getMemberOnlineBySidAndMemberTypeAndFunctionType(String sid, String memberType,
			String functionType);

	// List<MemberOnline> getMemberOnlineBySid(String sid);

}
