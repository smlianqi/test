E:
cd E:\work\work@new_game\CommonService\doc

set ICE_HOME=E:\Program Files (x86)\ZeroC\Ice-3.6.3
set PATH=%ICE_HOME%\bin;%PATH%

slice2java --output-dir ../generated -I../slice ../slice/baseice_config.ice
slice2java --output-dir ../generated -I../slice ../slice/common_basedao.ice
slice2java --output-dir ../generated -I../slice ../slice/common_basetype.ice
slice2java --output-dir ../generated -I../slice ../slice/common_constant.ice
slice2java --output-dir ../generated -I../slice ../slice/common_event.ice
slice2java --output-dir ../generated -I../slice ../slice/common_service.ice
slice2java --output-dir ../generated -I../slice ../slice/server_account.ice
slice2java --output-dir ../generated -I../slice ../slice/server_battle.ice
slice2java --output-dir ../generated -I../slice ../slice/server_config.ice
slice2java --output-dir ../generated -I../slice ../slice/server_foyer.ice
slice2java --output-dir ../generated -I../slice ../slice/server_logic.ice
slice2java --output-dir ../generated -I../slice ../slice/server_proto.ice
slice2java --output-dir ../generated -I../slice ../slice/server_room.ice
slice2java --output-dir ../generated -I../slice ../slice/server_test.ice
slice2java --output-dir ../generated -I../slice ../slice/wow_entity.ice
PAUSE



