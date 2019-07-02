package com.elex.im.module.servertest.test.script;

import java.util.HashMap;
import java.util.Map;

import com.elex.common.net.message.MessageConfig;
import com.elex.im.module.servertest.test.script.bean.TestBean;

public class HeroBean implements IHeroBean {
	private int id;
	private int level;
	private String name;
	private String test;
	private Map<Integer, String> map = new HashMap<>();
	private TestBean testBean;
	private MessageConfig messageConfig;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public Map<Integer, String> getMap() {
		return map;
	}

	public void setMap(Map<Integer, String> map) {
		this.map = map;
	}

	public TestBean getTestBean() {
		return testBean;
	}

	public void setTestBean(TestBean testBean) {
		this.testBean = testBean;
	}

	public MessageConfig getMessageConfig() {
		return messageConfig;
	}

	public void setMessageConfig(MessageConfig messageConfig) {
		this.messageConfig = messageConfig;
	}
}
