#define SERVER_TEST

module serverice{
module test{
	class TestBeanVO{
		int id;			//id
		string name;	//名字
		int sex; 		//性别
	};
	
	exception TestCheckIceException{
	}; 
	
	interface ITestService{
		["amd","ami"]TestBeanVO select(int id)throws TestCheckIceException;
		["amd","ami"]void remove(int id)throws TestCheckIceException;
		["amd","ami"]void insert(TestBeanVO tb)throws TestCheckIceException;
	};
};
};