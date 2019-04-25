package com.nercl.music.service.impl;

import static com.nercl.music.entity.user.Person.Gender.MAN;
import static com.nercl.music.entity.user.Role.EXERCISER;
import static com.nercl.music.util.Encryptor.encrypte2;
import static com.nercl.music.util.Encryptor.initSalt;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ExerciserDao;
import com.nercl.music.dao.LoginDao;
import com.nercl.music.dao.PersonDao;
import com.nercl.music.entity.user.Exerciser;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Person;
import com.nercl.music.service.ConsumeService;
import com.nercl.music.service.LoginService;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private ConsumeService consumeService;

	@Autowired
	private PersonDao personDao;

	@Autowired
	ExerciserDao exerciserDao;

	@Override
	public boolean save(Login login) {
		this.loginDao.save(login);
		return true;
	}

	@Transactional(isolation=Isolation.SERIALIZABLE,propagation=Propagation.REQUIRED)
	@Override
	public boolean save(String username, String email, String password) {
		boolean exsit = exsitLogin(email);
		if (exsit) {
			return false;
		}
		Person person = new Person();
		person.setAddress("");
		person.setAge(0);
		person.setEmail(email);
		person.setGender(MAN);
		person.setName(username);
		person.setPhone("");
		this.personDao.save(person);

		Login login = new Login();
		login.setCreateAt(System.currentTimeMillis());
		login.setLastLoginTime(System.currentTimeMillis());
		String salt = initSalt(16);
		login.setSalt(salt);
		login.setEncryptedPassword(encrypte2(password, salt));
		login.setLogin(email);
		login.setPassword(password);
		login.setRole(EXERCISER);
		login.setPersonId(person.getId());
		this.loginDao.save(login);

		Exerciser exerciser = new Exerciser();
		exerciser.setIdcard("");
		exerciser.setSchool("");
		exerciser.setPersonId(person.getId());
		exerciser.setCreateAt(System.currentTimeMillis());
		exerciser.setToken(UUID.randomUUID().toString());
		this.exerciserDao.save(exerciser);

		return true;
	}

	@Override
	public boolean repassword(String email, String sid, String password) {
		Person person = personDao.getByEmail(email);
		if (null != person) {
			Login lo = this.loginDao.getByPerson(person.getId());
			if (null != lo) {
				String salt = initSalt(16);
				lo.setSalt(salt);
				lo.setEncryptedPassword(encrypte2(password, salt));
				lo = this.loginDao.merge(lo);
				return null != lo;
			}
		}

		return false;
	}

	@Override
	public boolean authenticate(String login, String password) {
		Login lgin = this.loginDao.getByLogin(login);
		if (null != lgin) {
			String encryptedPassword = encrypte2(password, lgin.getSalt());
			boolean success = StringUtils.isNotBlank(encryptedPassword)
			        && encryptedPassword.equals(lgin.getEncryptedPassword());
			if (success) {
				lgin.setLastLoginTime(System.currentTimeMillis());
				this.loginDao.update(lgin);
			}
			return success;
		}
		return false;
	}

	@Override
	public Login getByUUIDAndName(String uuid, String name) {
		Login lgin = this.loginDao.getByUUIDAndName(uuid, name);
		return lgin;
	}

	@Override
	public boolean exsitLogin(String login) {
		Login lgin = this.loginDao.getByLogin(login);
		return null != lgin;
	}

	@Override
	public boolean exsitEmail(String email) {
		Person person = this.personDao.getByEmail(email);
		return null != person;
	}

	@Override
	public Login getByLogin(String login) {
		return this.loginDao.getByLogin(login);
	}

	@Override
	public Login getByEmail(String email) {
		Person person = this.personDao.getByEmail(email);
		if (null != person) {
			return this.getByPerson(person.getId());
		}
		return null;
	}

	@Override
	public Login getByPerson(String personId) {
		return this.loginDao.getByPerson(personId);
	}

	@Override
	public boolean rePassword(String user, String passowrd) {
		Login login = this.getByLogin(user);
		if (null != login) {
			String salt = initSalt(16);
			login.setSalt(salt);
			login.setEncryptedPassword(encrypte2(passowrd, salt));
			this.loginDao.update(login);
			return true;
		}
		return false;
	}

	@Override
	public void delete(Login login) {
		loginDao.delete(login);
	}

	@Override
	public boolean exsitPhone(String phone) {
		Person person = this.personDao.getByPhone(phone);
		return null != person;
	}

	@Override
	public boolean logout(String exerciserId, String consumeRecordId) {
		boolean success = consumeService.end(exerciserId, consumeRecordId);
		return success;
	}

	@Override
	public String setLoginToken(String login) {
		Login loin = getByLogin(login);
		if (null == loin) {
			return "";
		}
		String loginToken = UUID.randomUUID().toString();
		loin.setLoginToken(loginToken);
		loginDao.update(loin);
		return loginToken;
	}
}
