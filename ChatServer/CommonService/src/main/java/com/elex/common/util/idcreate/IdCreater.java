package com.elex.common.util.idcreate;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 账号id=cid+fid ; 用户id=cid+fid+rid
 * 
 * @author mausmars
 *
 */
public class IdCreater {
	// id创建者
	private AtomicLong idCreater;

	private String fidKey;

	private IdChange cidChange;// 创建id
	private IdChange fidChange;// 创建id
	private IdChange ridChange;// 创建id
	private IdChange aidChange;// 最终id
	private IdChange uidChange;// 最终id

	/**
	 * @param fid
	 *            账号服id
	 * @param count
	 *            创建id开始值
	 * @param fidLength
	 *            账号服id长度
	 * @param cidLength
	 *            创建id值长度
	 */
	public IdCreater(int fidLength, int cidLength, int ridLength) {
		this.fidChange = new IdChange(fidLength);
		this.cidChange = new IdChange(cidLength);
		this.ridChange = new IdChange(ridLength);
		this.aidChange = new IdChange(fidLength + cidLength);
		this.uidChange = new IdChange(fidLength + cidLength + ridLength);
	}

	public void reinit(int fid, long count) {
		this.fidKey = fidChange.id2AccountKey(fid);
		this.idCreater = new AtomicLong(count);
	}

	/**
	 * 创建账号id
	 * 
	 * @return
	 */
	public long[] createAccountId() {
		long id = idCreater.incrementAndGet();
		if (id >= cidChange.getMaxValue()) {
			return null;
		}
		String idStr = cidChange.id2AccountKey(id) + fidKey; // cid+fid
		long finalId = aidChange.accountKey2Id(idStr);
		long[] ids = new long[] { finalId, id };
		return ids;
	}

	/**
	 * 通过账号id和区id生成userId
	 * 
	 * @param accountId
	 * @param rid
	 * @return
	 */
	public long getUserId(long accountId, int rid) {
		String uidStr = aidChange.id2AccountKey(accountId) + ridChange.id2AccountKey(rid);// cid+fid+rid
		return uidChange.accountKey2Id(uidStr);
	}

	/**
	 * 检查userId
	 * 
	 * @param userId
	 * @param accountId
	 * @return
	 */
	public boolean checkUserId(long userId, long accountId) {
		String uidStr = uidChange.id2AccountKey(userId);
		String aidStr = aidChange.id2AccountKey(accountId);
		return uidStr.startsWith(aidStr);
	}

	public long getCidMaxValue() {
		return (long) cidChange.getMaxValue();
	}

	public long getFidMaxValue() {
		return (long) fidChange.getMaxValue();
	}

	public long getRidMaxValue() {
		return (long) ridChange.getMaxValue();
	}

	public int getCidMaxDigit() {
		return cidChange.getMaxDigit();
	}

	public int getFidMaxDigit() {
		return fidChange.getMaxDigit();
	}

	public int getRidMaxDigit() {
		return ridChange.getMaxDigit();
	}

	public static void main(String args[]) {
		test1();
		test2();
	}

	private static void test2() {
		String str1 = "AAAABBCC";
		String str2 = "AAAABB";

		System.out.println(str1.contains(str2));
		System.out.println(str1.startsWith(str2));
		System.out.println(str2.startsWith(str1));
	}

	private static void test1() {
		Set<Long> idList = new HashSet<Long>();
		long a = 32;
		IdCreater idCreater = new IdCreater(2, 4, 2);
		idCreater.reinit(1, a);
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 5; i++) {
			long[] ids = idCreater.createAccountId();
			if (idList.contains(ids[0])) {
				System.out.println("contains id=" + ids[0]);
			} else {
				idList.add(ids[0]);
			}
		}
		System.out.println((System.currentTimeMillis() - startTime) + " ms");
	}
}
