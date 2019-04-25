package com.nercl.music.cloud.service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.nercl.music.cloud.dao.PersonDao;
import com.nercl.music.cloud.entity.user.Person;
import com.nercl.music.util.CloudFileUtil;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

	@Value("${dd-yinyue.user.photo}")
	private String photoPath;

	@Autowired
	private CloudFileUtil cloudFileUtil;

	@Autowired
	private Gson gson;

	@Override
	public List<Person> list() {
		return personDao.list();
	}

	@Autowired
	private PersonDao personDao;

	@Override
	public Person get(String id) {
		return this.personDao.findByID(id);
	}

	@Override
	public List<Person> get(String[] ids) {
		return Arrays.stream(ids).map(id -> personDao.findByID(id)).collect(Collectors.toList());
	}

	@Override
	public Person getByLogin(String login) {
		return this.personDao.getByLogin(login);
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
	public List<Person> query(String key, int page) {
		return personDao.query(key, page);
	}

	@Override
	public boolean complete(Person person) {
		if (null == person) {
			return false;
		}
		personDao.update(person);
		return true;
	}

	@Override
	public boolean update(String uid, String name, String email, String gender, Integer age) {
		Person person = get(uid);
		if (null == person) {
			return false;
		}
		person.setName(name);
		person.setEmail(email);
		person.setAge(age);
		if (!Strings.isNullOrEmpty(gender)) {
			person.setGender(Person.Gender.valueOf(gender));
		}
		personDao.update(person);
		return true;
	}

	@Override
	public void updatePhone() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updatePhoto(String uid, File photo, String name) {
		Person person = get(uid);
		if (null == person) {
			return false;
		}
		Map<String, String> params = Maps.newHashMap();
		params.put("fileName", name);
		params.put("resourceType", "普通资源");
		String upload = cloudFileUtil.upload(photo, gson.toJson(params));
		person.setPhoto(upload);
		personDao.update(person);
		return true;
	}

	@Override
	public List<Person> findByConditions(Person person) throws Exception {
		return personDao.findByConditions(person);
	}

}
