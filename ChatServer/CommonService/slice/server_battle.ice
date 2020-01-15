#define SERVER_BATTLE

#ifndef COMMON_BASETYPE
#include "common_basetype.ice" 
#endif

#ifndef COMMON_EVENT
#include "common_event.ice" 
#endif

#ifndef SERVER_ROOM
#include "server_room.ice" 
#endif

//战场
module serverice{
module battle{
	class BattleTeam;
	["java:type:java.util.LinkedList<BattleTeam>"] sequence<BattleTeam> BattleTeamLList;

    //战斗列表
	class BattleInfo{
		string host;							//战场服host
		int port;								//战场服端口
		long battleId;				        	//战场id
	};
    
    class BattleTeam{
    	serverice::room::UserTroopLList userTroops;
    };
    
	//战场服务接口
	interface IBattleService{
		//创建一个战场（战场原型id，参与者id列表）
		["amd","ami"] BattleInfo createBattle(int battleType, int battleKeyId, BattleTeamLList battleTeam);
	};
	
    //战场结束事件
	class BattleOverEvent extends baseice::event::IEvent{
	    long battleId; //战场id
	};
	
	//战场结果事件
	class BattleResultEvent extends baseice::event::IEvent{
		long battleId;			//战场id
		long userId; 			//用户id
		string param;			//参数
	};
};
};
