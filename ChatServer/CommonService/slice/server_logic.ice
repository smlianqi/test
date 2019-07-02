#define SERVER_LOGIC

#ifndef COMMON_BASETYPE
#include "common_basetype.ice" 
#endif

#ifndef COMMON_EVENT
#include "common_event.ice" 
#endif

//逻辑服
module serverice{
module logic{
	//逻辑服务接口
	interface ILogicService{
	};
	
	
	//房间匹配成功事件
	class MatchRoomSucceedEvent extends baseice::event::IEvent{
		baseice::basetype::LongAList uids;	//推送的人
		long battleId;						//战场id
		string host;						//战场服host
		int port;							//战场服端口
	};
	
	//掠夺事件
	class PillageEvent extends baseice::event::IEvent{
		long userId; 		//用户id
	    string battleLog; 	//战报
	};
};
};