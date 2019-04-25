package com.nercl.music.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ExerciserDao;
import com.nercl.music.entity.user.Exerciser;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Person;
import com.nercl.music.service.ExerciserService;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.PersonService;

@Service
@Transactional
public class ExerciserServiceImpl implements ExerciserService {

	@Autowired
	private ExerciserDao exerciserDao;

	@Autowired
	private LoginService loginService;

	@Autowired
	private PersonService personService;

	@Override
	public Exerciser get(String id) {
		return exerciserDao.findByID(id);
	}

	@Override
	public Exerciser getByLogin(String login) {
		Login lgin = this.loginService.getByLogin(login);
		if (null != lgin) {
			Person person = lgin.getPerson();
			if (null != person) {
				Exerciser exerciser = this.exerciserDao.getByPerson(person.getId());
				return exerciser;
			}
		}
		return null;
	}

	@Override
	public List<Exerciser> list(int page, String name, String email) {
		return this.exerciserDao.list(page, name, email);
	}

	// @Override
	// public Login save(String password, String name, String phone, String
	// idcard, String school, Integer age,
	// String email, String gender) {
	// Person person = new Person();
	// person.setName(name);
	// person.setAge(age);
	// person.setPhone(phone);
	// person.setEmail(email);
	// person.setGender(Person.Gender.valueOf(gender));
	// this.personService.save(person);
	//
	// Login lgin = new Login();
	// lgin.setLogin(phone);
	// String salt = initSalt(16);
	// lgin.setSalt(salt);
	// lgin.setEncryptedPassword(encrypte2(password, salt));
	// lgin.setPersonId(person.getId());
	// lgin.setRole(Role.EXERCISER);
	// lgin.setCreateAt(System.currentTimeMillis());
	// this.loginService.save(lgin);
	//
	// Login lgin2 = new Login();
	// lgin2.setLogin(email);
	// salt = initSalt(16);
	// lgin2.setSalt(salt);
	// lgin2.setEncryptedPassword(encrypte2(password, salt));
	// lgin2.setPersonId(person.getId());
	// lgin2.setRole(Role.EXERCISER);
	// lgin2.setCreateAt(System.currentTimeMillis());
	// this.loginService.save(lgin2);
	//
	// Exerciser exerciser = new Exerciser();
	// exerciser.setIdcard(idcard);
	// exerciser.setSchool(school);
	// exerciser.setPersonId(person.getId());
	// exerciser.setCreateAt(System.currentTimeMillis());
	// exerciser.setToken(UUID.randomUUID().toString());
	// this.exerciserDao.save(exerciser);
	// return lgin;
	// }

	@Override
	public String getToken(String uid) {
		Person person = personService.get(uid);
		if (null != person) {
			Exerciser exerciser = getByPerson(person.getId());
			if (null != exerciser) {
				return exerciser.getToken();
			}
		}
		return null;
	}

	@Override
	public Exerciser getByPerson(String pid) {
		return exerciserDao.getByPerson(pid);
	}

}
