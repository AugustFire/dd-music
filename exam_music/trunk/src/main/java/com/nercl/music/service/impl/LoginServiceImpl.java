package com.nercl.music.service.impl;

import static com.nercl.music.util.Encryptor.encrypte2;
import static com.nercl.music.util.Encryptor.initSalt;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.LoginDao;
import com.nercl.music.entity.user.Examinee;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Person;
import com.nercl.music.service.ExamineeService;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.PersonService;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private PersonService personService;

	@Autowired
	private ExamineeService examineeService;

	@Override
	public boolean save(Login login) {
		this.loginDao.save(login);
		return true;
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
		Person person = this.personService.getByEmail(email);
		return null != person;
	}

	@Override
	public Login getByLogin(String login) {
		return this.loginDao.getByLogin(login);
	}

	@Override
	public Login getByEmail(String email) {
		Person person = this.personService.getByEmail(email);
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
		Person person = this.personService.getByPhone(phone);
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

	@Override
	public String getToken(String uid) {
		Examinee examinee = examineeService.getById(uid);
		if (null != examinee) {
			Login login = getByPerson(examinee.getPersonId());
			if (null != login) {
				return login.getLoginToken();
			}
		}
		return null;
	}
}
