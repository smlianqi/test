package com.elex.im.module.servertest.test.script;

public interface IScriptTest {
	HeroBean printTest(int threadId, int count, IHeroBean heroBean);

	HeroBean createHeroBean();
}
