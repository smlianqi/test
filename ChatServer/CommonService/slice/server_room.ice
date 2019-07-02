#define SERVER_ROOM

#ifndef COMMON_BASETYPE
#include "common_basetype.ice" 
#endif

#ifndef COMMON_EVENT
#include "common_event.ice" 
#endif

module serverice{
module room{
	class UserTroop;
	["java:type:java.util.LinkedList<UserTroop>"] sequence<UserTroop> UserTroopLList;
	
	class UserTroop{
		long userId;
		string name;
		int playerType; //1 user; 2 npc
		baseice::basetype::ByteArray userTroopData;
	};


	//房间服务接口
	interface IRoomService{
		["amd","ami"]void matchupGame(int matchType,UserTroopLList userTroops,string params); //匹配战场
		
		["amd","ami"]void cancelMatchupGame(long teamId);		//取消匹配
		["amd","ami"]void leaveTeam(long userId);				//离开队伍
	};
};
};