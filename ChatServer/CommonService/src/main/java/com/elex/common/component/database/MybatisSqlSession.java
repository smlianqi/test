package com.elex.common.component.database;

import org.apache.ibatis.session.SqlSession;

public class MybatisSqlSession implements ISqlSession {
	private SqlSession sqlSession;

	public MybatisSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public void close() {
		sqlSession.close();
	}

	@Override
	public <T> T getSession() {
		return (T) sqlSession;
	}
}
