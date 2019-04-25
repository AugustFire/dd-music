package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.SubjectDao;
import com.nercl.music.entity.authorize.Subject;

@Repository
public class SubjectDaoImpl extends AbstractBaseDaoImpl<Subject, String> implements SubjectDao {

	@Override
	public List<Subject> getByTitle(String title) {
		String jpql = "from Subject s where s.title = ?1";
		List<Subject> subjects = executeQueryWithoutPaging(jpql, title);
		return subjects;
	}

	@Override
	public List<Subject> list() {
		String jpql = "from Subject s";
		List<Subject> subjects = executeQueryWithoutPaging(jpql);
		return subjects;
	}

}
