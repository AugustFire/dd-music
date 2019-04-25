package com.nercl.music.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.PersonDao;
import com.nercl.music.entity.user.Person;
import com.nercl.music.service.PersonService;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonDao personDao;

	@Override
	public Person get(String id) {
		return this.personDao.findByID(id);
	}

	@Override
	public Person getByEmail(String email) {
		return this.personDao.getByEmail(email);
	}

	@Override
	public void save(Person person) {
		this.personDao.save(person);
	}

	@Override
	public void update(Person person) {
		this.personDao.update(person);
	}

	@Override
	public void delete(Person person) {
		this.personDao.delete(person);
		;
	}

	@Override
	public Person getByPhone(String phone) {
		return this.personDao.getByPhone(phone);
	}

}
