E:
cd E:\work\im_server\CommonService\slice
slice2java -I. --output-dir=../generated common_basetype.ice
slice2java -I. --output-dir=../generated common_event.ice
slice2java -I. --output-dir=../generated server_account.ice
slice2java -I. --output-dir=../generated server_battle.ice
slice2java -I. --output-dir=../generated server_logic.ice
slice2java -I. --output-dir=../generated server_pay.ice
slice2java -I. --output-dir=../generated server_proto.ice
slice2java -I. --output-dir=../generated server_room.ice
slice2java -I. --output-dir=../generated server_test.ice
pause
