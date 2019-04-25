package com.nercl.music.cloud.service;

import static com.nercl.music.util.Encryptor.encrypte2;
import static com.nercl.music.util.Encryptor.initSalt;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.nercl.music.cloud.dao.LoginDao;
import com.nercl.music.cloud.dao.PersonDao;
import com.nercl.music.cloud.entity.user.Login;
import com.nercl.music.cloud.entity.user.Person;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private PersonDao personDao;

	@Override
	public boolean save(Login login) {
		this.loginDao.save(login);
		return true;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	@Override
	public boolean save(String phone, String password, Boolean isTeacher) {
		boolean exsit = exsitLogin(phone);
		if (exsit) {
			return false;
		}
		Person person = new Person();
		person.setGender(Person.Gender.MAN);
		person.setPhone(phone);
		person.setEmail("");
		this.personDao.save(person);

		Login login = new Login();
		login.setCreateAt(System.currentTimeMillis());
		login.setLastLoginTime(System.currentTimeMillis());
		String salt = initSalt(16);
		login.setSalt(salt);
		login.setEncryptedPassword(encrypte2(password, salt));
		login.setLogin(phone);
		login.setPassword(password);
		login.setPersonId(person.getId());
		this.loginDao.save(login);

		return true;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	@Override
	public Person saveByEmail(String email, String password, Boolean isTeacher) {
		boolean exsit = exsitEmail(email);
		if (exsit) {
			return null;
		}
		Person person = new Person();
		person.setGender(Person.Gender.MAN);
		person.setPhone("");
		person.setEmail(email);
		person.setName("");
		person.setIsTeacher(isTeacher);
		this.personDao.save(person);

		Login login = new Login();
		login.setCreateAt(System.currentTimeMillis());
		login.setLastLoginTime(System.currentTimeMillis());
		String salt = initSalt(16);
		login.setSalt(salt);
		login.setEncryptedPassword(encrypte2(password, salt));
		login.setLogin(email);
		login.setPassword(password);
		login.setPersonId(person.getId());
		this.loginDao.save(login);

		return person;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	@Override
	public Person saveByEmail(String email, String name, String password, Boolean isTeacher) {
		boolean exsit = exsitEmail(email);
		if (exsit) {
			return null;
		}
		Person person = new Person();
		person.setGender(Person.Gender.MAN);
		person.setPhone("");
		person.setEmail(email);
		person.setName(name);
		person.setIsTeacher(isTeacher);
		this.personDao.save(person);

		Login login = new Login();
		login.setCreateAt(System.currentTimeMillis());
		login.setLastLoginTime(System.currentTimeMillis());
		String salt = initSalt(16);
		login.setSalt(salt);
		login.setEncryptedPassword(encrypte2(password, salt));
		login.setLogin(email);
		login.setPassword(password);
		login.setPersonId(person.getId());
		this.loginDao.save(login);

		return person;
	}

	@Override
	public boolean repassword(String email, String password) {
		Person person = personDao.getByEmail(email);
		if (null == person) {
			return false;
		}
		Login lo = this.loginDao.getByPerson(person.getId());
		if (null == lo) {
			return false;
		}
		String salt = initSalt(16);
		lo.setSalt(salt);
		lo.setEncryptedPassword(encrypte2(password, salt));
		this.loginDao.update(lo);
		return true;
	}

	@Override
	public boolean authenticate(String login, String password) {
		Login lgin = this.loginDao.getByLogin(login);
		if (null != lgin) {
			String encryptedPassword = encrypte2(password, lgin.getSalt());
			boolean success = !Strings.isNullOrEmpty(encryptedPassword)
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
