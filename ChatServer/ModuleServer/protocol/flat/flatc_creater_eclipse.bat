E:
cd E:\work\im_server\ModuleServer\protocol\flat
flatc.exe -o ../../src/main/java --java ChatMessage.fbs
flatc.exe -o ../../src/main/java --java UserMessage.fbs
flatc.exe -o ../../src/main/java --java ErrorMessage.fbs
pause