package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.authorize.Subject;

public interface SubjectDao extends BaseDao<Subject, String> {

	List<Subject> getByTitle(String title);

	List<Subject> list();

}
