log4j 2指定配置文件

-Dlog4j.configurationFile="propertiesconfig/log4j2.xml"


//--------------------------------------------
启动参数
-Dcom.sun.management.jmxremote 
-Dcom.sun.management.jmxremote.port=9999 
-Dcom.sun.management.jmxremote.authenticate=false 
-Dcom.sun.management.jmxremote.ssl=false 
-server -Xms512m -Xmx512m -Xss256K -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:PermSize=64m
-Dio.netty.leakDetectionLevel=paranoid
-Dlog4j.configurationFile="propertiesconfig/log4j2.xml"



//--------------------------------------------
测试用
-agentpath:F:\PROGRA~2\JPROFI~1\bin\WINDOW~1\jprofilerti.dll=port=8849


-----------------------------------------
maven打包安装
mvn package -Dmaven.test.skip=true
mvn install -Dmaven.test.skip=true