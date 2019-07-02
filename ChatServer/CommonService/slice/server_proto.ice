#define SERVER_PROTO

#ifndef COMMON_EVENT
#include "common_event.ice" 
#endif


#ifndef COMMON_BASETYPE
#include "common_basetype.ice" 
#endif

module serverice{
	module proto{
		dictionary<int,baseice::basetype::ByteArray> protoMap;
		dictionary<string,protoMap> StringProtoListMap;//返回的原型信息
		
		class ChangeProtoInfo{
			int version; //版本号
			baseice::basetype::StringAList protoNameList; //静态数据类名
		};
		
		class ProtoInfo{
			int version; //版本号
			StringProtoListMap map;
		};
		
		//管理接口
		interface IProtoService{
			ProtoInfo getPrototype(ChangeProtoInfo info);//得到变化的原型数据
			
			ChangeProtoInfo checkPrototype(ChangeProtoInfo info,int iRegion,int iModule);//检测原型
		};
		
		//原型更新接口事件
		class ProtoEvent extends baseice::event::IEvent{
			ChangeProtoInfo changeInfo;
		};
	};
};
