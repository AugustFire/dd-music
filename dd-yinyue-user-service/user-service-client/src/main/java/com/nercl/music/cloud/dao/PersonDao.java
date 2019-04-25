package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.user.Person;

public interface PersonDao extends BaseDao<Person, String> {

	List<Person> list();

	Person getByEmail(String email);

	Person getByPhone(String phone);

	List<Person> query(String key, int page);

	Person getByLogin(String login);

}
