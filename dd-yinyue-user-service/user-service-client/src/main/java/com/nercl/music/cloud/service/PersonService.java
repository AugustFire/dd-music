package com.nercl.music.cloud.service;

import java.io.File;
import java.util.List;

import com.nercl.music.cloud.entity.user.Person;

public interface PersonService {

	List<Person> list();

	Person get(String id);

	List<Person> get(String[] ids);

	Person getByLogin(String login);

	Person getByEmail(String email);

	Person getByPhone(String phone);

	void save(Person person);

	void update(Person person);

	void update(Person oldPerson, Person person);

	void delete(Person person);

	List<Person> query(String key, int page);

	boolean complete(Person p);

	boolean update(String uid, String name, String email, String gender, Integer age);

	void updatePhone();

	boolean updatePhoto(String uid, File photo, String name);

	List<Person> findByConditions(Person person) throws Exception;

}
