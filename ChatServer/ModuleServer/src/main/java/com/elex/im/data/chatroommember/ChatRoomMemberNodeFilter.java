package com.elex.im.data.chatroommember;

import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgnitePredicate;

import com.elex.common.component.function.type.FunctionType;

/**
 * 序列化的尽量不要用匿名类！！！！！！
 * 
 * @author mausmars
 *
 */
public class ChatRoomMemberNodeFilter implements IgnitePredicate<ClusterNode> {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean apply(ClusterNode e) {
		Object v = e.attribute(FunctionType.chat.name());
		return v != null;
	}
}