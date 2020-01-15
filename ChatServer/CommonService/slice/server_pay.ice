#define SERVER_PAY

#ifndef COMMON_EVENT
#include "common_event.ice" 
#endif

module serverice{
module pay{
	// 付费网络接口
	interface IPayService{
		//核查订单，针对苹果的查询接口
		["amd","ami"]void checkReceipt(int channel,int userId, int regionId, string receiptContent);
	};
	
	//付费成功事件
	class PaySucceedEvent extends baseice::event::IEvent{
		long uids;	//推送的人
	};
};
};