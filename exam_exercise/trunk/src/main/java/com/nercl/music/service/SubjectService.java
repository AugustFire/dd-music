package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.authorize.Subject;

public interface SubjectService {

	Subject save(String title);

	List<Subject> list();

	List<Subject> getByTitle(String title);

	Subject get(String sid);

	boolean delete(String id);

}
