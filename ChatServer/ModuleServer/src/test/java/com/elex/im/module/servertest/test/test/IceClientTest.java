package com.elex.im.module.servertest.test.test;

public class IceClientTest {
	public static void main(String[] args) {
//		int status = 0;
//		Ice.Communicator ic = null;
//		try {
//			ic = Ice.Util.initialize(args);
//			Ice.ObjectPrx base = ic.stringToProxy("Default_Service_OID:tcp -h  192.168.0.249 -p 10301");
//			IBattleServicePrx battleService = IBattleServicePrxHelper.checkedCast(base);
//			if (battleService == null) {
//				throw new Error("Invalid proxy");
//			}
//
//			System.out.println("___________1___________");
//			BattleInfo createBattle = battleService.createBattle(2, 1, new ArrayList<>());
//			System.out.printf("%s::%s::%s\n", createBattle.battleId, createBattle.host, createBattle.port);
//			System.out.println("___________2___________");
//
//		} catch (Ice.LocalException e) {
//			e.printStackTrace();
//			status = 1;
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//			status = 1;
//		}
//		if (ic != null) {
//			try {
//				ic.destroy();
//			} catch (Exception e) {
//				System.err.println(e.getMessage());
//				status = 1;
//			}
//		}
//		System.exit(status);
	}
}