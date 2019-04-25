package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.user.Person;
import com.nercl.music.util.page.PaginateSupportArray;

public interface PersonService {

	PaginateSupportArray<Person> list(int page);

	Person get(String id);

	Person getByEmail(String email);

	Person getByPhone(String phone);

	void save(Person person);

	void update(Person person);

	void update(Person oldPerson, Person person);

	void delete(Person person);

	List<Person> query(String key);

}
