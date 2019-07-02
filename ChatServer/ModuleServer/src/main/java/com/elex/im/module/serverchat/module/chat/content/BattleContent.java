package com.elex.im.module.serverchat.module.chat.content;

import com.elex.im.module.serverchat.module.chat.type.ContentType;

/**
 * 战报内容
 * 
 * {"msg":"xxxx","attack":{"uid":"xxx"},"defense":{"uid":"xxx"}}
 * 
 * @author mausmars
 *
 */
public class BattleContent extends AContent {
	private Camp attack;
	private Camp defense;

	@Override
	public ContentType contentType() {
		return ContentType.Battle;
	}

	public Camp getAttack() {
		return attack;
	}

	public void setAttack(Camp attack) {
		this.attack = attack;
	}

	public Camp getDefense() {
		return defense;
	}

	public void setDefense(Camp defense) {
		this.defense = defense;
	}

}

class Camp {
	private String uid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}