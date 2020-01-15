package com.elex.im.module.servertest.test.script.bean;

import java.util.Map;

import com.elex.im.module.servertest.test.script.IHeroBean;

public class TestBean implements IHeroBean {
	private int id;
	private int level;
	private String name;
	private String test;
	private Map<Integer, String> map;

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
}
