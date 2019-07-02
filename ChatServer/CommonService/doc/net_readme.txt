log4j 2指定配置文件

-Dlog4j.configurationFile="propertiesconfig/log4j2.xml"

-----------------------------------------
maven打包安装
mvn package -Dmaven.test.skip=true
mvn install -Dmaven.test.skip=true

==================================
TCP
MARK（xxx，三字节） 
后续长度（四字节）
内容（Protobuf=CommonMsgBody）

message CommonMsgBody{
	uint32 commandId = 1; 	//指令id
	bytes content = 2;	//消息内容
	uint32 version = 3;	//指令版本号
}
--------------------
UPD

上行（暂时没指令）
内容（Protobuf=CommonMsgBody）

下行
后续长度（四字节）
内容（Protobuf=CommonMsgBody）

--------------------
websocket

内容（Protobuf=CommonMsgBody）
--------------------
http

内容（Protobuf=CommonMsgBody）
==================================