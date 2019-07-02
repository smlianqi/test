#define SERVER_MAP

#ifndef COMMON_EVENT
#include "common_event.ice" 
#endif

module serverice{
module map{
	// 地图信息
	class MapInfo{
		string host;							//map服host
		int port;								//map服端口
	};
	
	// 地图网络接口
	interface IMapService{
		["amd","ami"] MapInfo getMapInfo();		//获取地图服信息
	};
};
};