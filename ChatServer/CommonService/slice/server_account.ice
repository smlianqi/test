#define SERVER_ACCOUNT

#ifndef COMMON_EVENT
#include "common_event.ice" 
#endif


//检查服
module serverice{
module account{
	//账号状态{ 下线，在线，拉黑  }
	enum AccountStateEnum { Offline=0 ,Online=1, Blacklist=2 };
	
	//---
	//状态{ 其他错误，成功，密码错误  }
	enum StateEnum { Fail=0, Success, PasswordError };
	
	//结果
	class LoginResult{
		StateEnum state;	//状态
		string token;
		long accountId;		//账号id
		string rids;		//{区id：账号s}
		
		int rid;			//已经登陆区
		int lsid;			//逻辑服id
		int asid;			//账号服id
	};
	
	class ChooseRegionResult{
		StateEnum state;	//状态
		
		string ip;			//ip地址
		int port;			//端口
	};
	
	//账号管理接口
	interface IAccountService{
		["amd","ami"]LoginResult login(string account, int channel, string password);	//登陆账号（如果没有自动创建并登陆，有就登陆）
		["amd","ami"]ChooseRegionResult chooseRegion(string token, int rid);			//选区
		["amd","ami"]long checkLogin(string token, int rid, long userId);				//验证登陆的token
		["amd","ami"]void loginOut(long userId);										//登出的token
	};
};
};