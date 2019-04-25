package com.nercl.music.service.impl;

import static com.nercl.music.util.Encryptor.encrypte2;
import static com.nercl.music.util.Encryptor.initSalt;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ExpertDao;
import com.nercl.music.dao.LoginDao;
import com.nercl.music.dao.PersonDao;
import com.nercl.music.entity.user.AbstractGroup;
import com.nercl.music.entity.user.Expert;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Person;
import com.nercl.music.entity.user.Person.Gender;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.ExpertService;
import com.nercl.music.service.GroupService;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.PersonService;

@Service
@Transactional
public class ExpertServiceImpl implements ExpertService {

	@Autowired
	private ExpertDao expertDao;

	@Autowired
	private PersonDao personDao;

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private PersonService personService;

	@Autowired
	private LoginService loginService;

	@Autowired
	private GroupService groupService;

	@Value("${default.password}")
	private String defaultPass;

	@Override
	public List<Expert> list(int page) {
		return this.expertDao.list(page);
	}

	@Override
	public Expert get(String id) {
		return this.expertDao.get(id);
	}

	@Override
	public List<Expert> query(String key, int page) {
		return this.expertDao.query(key, page);
	}

	@Override
	public boolean save(String login, String name, String jobTitle, String unit, String phone, String email,
	        String intro) {
		Person person = new Person();
		person.setName(name);
		person.setPhone(phone);
		person.setEmail(email);
		person.setGender(Gender.MAN);
		this.personService.save(person);

		Login lgin = new Login();
		lgin.setLogin(login);
		String salt = initSalt(16);
		lgin.setSalt(salt);
		lgin.setEncryptedPassword(encrypte2(defaultPass, salt));
		lgin.setPersonId(person.getId());
		lgin.setRole(Role.EXPERT);
		lgin.setCreateAt(System.currentTimeMillis());
		this.loginService.save(lgin);

		Expert expert = new Expert();
		expert.setUnit(unit);
		expert.setJobTitle(jobTitle);
		expert.setPersonId(person.getId());
		expert.setIntro(intro);
		expert.setCreateAt(System.currentTimeMillis());
		this.expertDao.save(expert);
		return true;
	}

	@Override
	public Expert getByLogin(String login) {
		Login lgin = this.loginService.getByLogin(login);
		if (null != lgin) {
			Person person = lgin.getPerson();
			if (null != person) {
				Expert expert = this.expertDao.getByPerson(person.getId());
				return expert;
			}
		}
		return null;
	}

	@Override
	public String getLoginByExpertId(String id) {
		Expert expert = expertDao.get(id);
		if (expert != null) {
			Login login = loginService.getByPerson(expert.getPersonId());
			if (login != null) {
				return login.getLogin();
			}
		}
		return null;
	}

	@Override
	public boolean update(String id, String login, String name, String jobTitle, String unit, String phone,
	        String email, String intro) {
		Expert expert = expertDao.get(id);
		if (expert != null) {
			expert.setJobTitle(jobTitle);
			expert.setUnit(unit);
			expert.setIntro(intro);
		}
		Person person = expert.getPerson();
		if (person != null) {
			person.setEmail(email);
			person.setPhone(phone);
			person.setName(name);
		}
		Login user = loginService.getByPerson(expert.getPersonId());
		if (user != null) {
			user.setLogin(login);
		}
		expertDao.update(expert);
		personDao.update(person);
		loginDao.update(user);
		return true;
	}

	@Override
	public void delete(String id) {
		Expert expert = expertDao.findByID(id);
		if (expert != null) {
			expertDao.delete(expert);
		}
	}

	@Override
	public List<Expert> listByAttributes(int page, String name, String jobTitle, String unit, String email,
	        String phone) {
		return expertDao.listByAttributes(page, name, jobTitle, unit, email, phone);
	}

	@Override
	public boolean setGroup(String[] eids, String gid) {
		AbstractGroup group = this.groupService.get(gid);
		if (null != group) {
			Arrays.stream(eids).forEach(eid -> {
				Expert expert = this.get(eid);
				if (null != expert) {
					expert.setGroupId(gid);
					this.expertDao.update(expert);
				}
			});
			return true;
		}
		return false;
	}

}
