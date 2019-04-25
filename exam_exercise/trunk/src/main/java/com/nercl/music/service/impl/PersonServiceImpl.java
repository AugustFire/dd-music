package com.nercl.music.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.PersonDao;
import com.nercl.music.entity.user.Person;
import com.nercl.music.service.PersonService;
import com.nercl.music.util.page.PaginateSupportArray;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

	
	@Override
	public PaginateSupportArray<Person> list(int page) {
		return personDao.list(page);
	}

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
	public void update(Person oldPerson, Person person) {
		BeanUtils.copyProperties(person, oldPerson, "id", "email", "name");
		this.personDao.update(oldPerson);
	}

	@Override
	public void delete(Person person) {
		this.personDao.delete(person);
	}

	@Override
	public Person getByPhone(String phone) {
		return this.personDao.getByPhone(phone);
	}

	@Override
	public List<Person> query(String key) {
		return personDao.query(key);
	}

}
