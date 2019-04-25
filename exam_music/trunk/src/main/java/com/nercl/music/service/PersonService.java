package com.nercl.music.service;

import com.nercl.music.entity.user.Person;

public interface PersonService {

	Person get(String id);

	Person getByEmail(String email);

	Person getByPhone(String phone);

	void save(Person person);

	void update(Person person);

	void delete(Person person);

}
