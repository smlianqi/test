#define SERVER_MAIL

#ifndef COMMON_EVENT
#include "common_event.ice" 
#endif

//邮件服务
module serverice{
module mail{
	//邮件服务
	interface IMailService{
		//mail
		["amd","ami"]void sendMail(long sender,long revider,string titell,string context);
		
		
	};
};
};