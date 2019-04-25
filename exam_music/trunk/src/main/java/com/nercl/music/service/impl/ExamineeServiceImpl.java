package com.nercl.music.service.impl;

import static com.nercl.music.util.Encryptor.encrypte2;
import static com.nercl.music.util.Encryptor.initSalt;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.nercl.music.dao.ExamineeDao;
import com.nercl.music.entity.user.AbstractGroup;
import com.nercl.music.entity.user.Examinee;
import com.nercl.music.entity.user.ExamineeGroup;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Person;
import com.nercl.music.entity.user.Person.Gender;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.ExamineeService;
import com.nercl.music.service.GroupService;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.PersonService;

@Service
@Transactional
public class ExamineeServiceImpl implements ExamineeService {

	@Autowired
	private ExamineeDao examineeDao;

	@Autowired
	private LoginService loginService;

	@Autowired
	private PersonService personService;

	@Autowired
	private GroupService groupService;

	@Override
	public List<Examinee> list(int page) {
		return this.examineeDao.list(page);
	}

	@Override
	public List<Examinee> getByExam(String examId, int page) {
		return this.getByExam(examId, page);
	}

	@Override
	public Login save(String login, String password, String name, String phone, String idcard, String examNo,
	        String school, Integer age, String email, String gender, String photo) {
		Person person = new Person();
		person.setName(name);
		person.setAge(age);
		person.setPhone(phone);
		person.setEmail(email);
		person.setGender(Person.Gender.valueOf(gender));
		this.personService.save(person);

		Login lgin = new Login();
		lgin.setLogin(login);
		String salt = initSalt(16);
		lgin.setSalt(salt);
		lgin.setEncryptedPassword(encrypte2(password, salt));
		lgin.setPersonId(person.getId());
		lgin.setRole(Role.EXAMINEE);
		lgin.setCreateAt(System.currentTimeMillis());
		this.loginService.save(lgin);

		Examinee examinee = new Examinee();
		examinee.setPhoto(photo);
		examinee.setExamNo(examNo);
		examinee.setIdcard(idcard);
		examinee.setSchool(school);
		examinee.setPersonId(person.getId());
		examinee.setCreateAt(System.currentTimeMillis());
		this.examineeDao.save(examinee);
		return lgin;
	}

	@Override
	public Examinee getById(String id) {
		return this.examineeDao.findByID(id);
	}

	@Override
	public boolean update(String id, String name, Integer age, String phone, String gender, String email, String school,
	        String idcard) {
		Examinee examinee = this.getById(id);
		if (null != examinee) {
			Person person = examinee.getPerson();
			person.setName(name);
			person.setAge(age);
			person.setPhone(phone);
			person.setEmail(email);
			person.setGender(Gender.valueOf(gender));
			this.personService.update(person);
			examinee.setIdcard(idcard);
			examinee.setSchool(school);
			this.examineeDao.update(examinee);
			return true;
		}
		return false;
	}

	@Override
	public Examinee getByLogin(String login) {
		Login lgin = this.loginService.getByLogin(login);
		if (null != lgin) {
			Person person = lgin.getPerson();
			if (null != person) {
				Examinee examinee = this.examineeDao.getByPerson(person.getId());
				return examinee;
			}
		}
		return null;
	}

	@Override
	public List<Examinee> list(String school, String idcard, String name, String phone, String email, String examNo,
	        int page) {
		return examineeDao.list(school, idcard, name, phone, email, examNo, page);
	}

	@Override
	public void delete(String id) {
		Examinee examinee = examineeDao.findByID(id);
		if (examinee != null) {
			examineeDao.delete(examinee);
		}
	}

	@Override
	public List<Map<String, String>> getAllUUIDAndName() {
		List<Examinee> examinees = this.examineeDao.list();
		List<Map<String, String>> ret = examinees.stream().map(examinee -> {
			Map<String, String> idName = Maps.newHashMap();
			idName.put("id", examinee.getPerson().getId());
			idName.put("name", examinee.getPerson().getName());
			return idName;
		}).collect(Collectors.toList());
		return ret;
	}

	@Override
	public boolean setGroup(String[] eids, String gid) {
		AbstractGroup group = this.groupService.get(gid);
		if (null != group) {
			Arrays.stream(eids).forEach(eid -> {
				Examinee examinee = this.getById(eid);
				if (null != examinee) {
					examinee.setGroupId(gid);
					this.examineeDao.update(examinee);
				}
			});
			return true;
		}
		return false;
	}

	@Override
	public List<Examinee> getByGroup(List<ExamineeGroup> groups) {
		if (null == groups || groups.isEmpty()) {
			return null;
		}
		return this.examineeDao.getByGroup(groups);
	}

}
