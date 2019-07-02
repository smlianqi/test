E:
cd E:\work\im_server\CommonService\protocol\flat
flatc.exe -o ../../src/main/java/ --java CommonMsgBody.fbs
flatc.exe -o ../../src/main/java/ --java InnerProto.fbs
pause