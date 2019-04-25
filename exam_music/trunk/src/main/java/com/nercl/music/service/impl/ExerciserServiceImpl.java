package com.nercl.music.service.impl;

import static com.nercl.music.util.Encryptor.encrypte2;
import static com.nercl.music.util.Encryptor.initSalt;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ExerciserDao;
import com.nercl.music.entity.user.Exerciser;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Person;
import com.nercl.music.entity.user.Person.Gender;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.ExerciserService;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.PersonService;

@Service
@Transactional
public class ExerciserServiceImpl implements ExerciserService {

	@Autowired
	private LoginService loginService;

	@Autowired
	private PersonService personService;

	@Autowired
	private ExerciserDao exerciserDao;

	@Value("${exam_music.exerciser.photo}")
	private String photoPath;

	@Override
	public boolean register(String phone, String password) {
		Person person = new Person();
		person.setName("");
		person.setPhone(phone);
		person.setEmail("");
		person.setGender(Person.Gender.MAN);
		this.personService.save(person);

		Login lgin = new Login();
		lgin.setLogin(phone);
		String salt = initSalt(16);
		lgin.setSalt(salt);
		lgin.setEncryptedPassword(encrypte2(password, salt));
		lgin.setPersonId(person.getId());
		lgin.setRole(Role.EXERCISER);
		lgin.setCreateAt(System.currentTimeMillis());
		this.loginService.save(lgin);

		Exerciser exerciser = new Exerciser();
		exerciser.setPersonId(person.getId());
		exerciser.setCreateAt(System.currentTimeMillis());
		this.exerciserDao.save(exerciser);
		return true;
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
	public boolean complete(String eid, String name, String email, String gender, Integer age, String school,
	        String grade, String photo) {
		Exerciser exerciser = this.getById(eid);
		if (null != exerciser) {
			Person person = exerciser.getPerson();
			person.setName(name);
			person.setEmail(email);
			if (StringUtils.isNotBlank(gender)) {
				person.setGender(Gender.valueOf(gender));
			}
			person.setAge(age);
			this.personService.update(person);
			exerciser.setSchool(school);
			exerciser.setGrade(grade);
			if (StringUtils.isNotBlank(photo)) {
				String photoName = UUID.randomUUID().toString() + ".png";
				byte[] bytes = Base64.getDecoder().decode(photo);
				try {
					FileUtils.copyInputStreamToFile(new ByteArrayInputStream(bytes),
					        new File(this.photoPath + File.separator + photoName));
				} catch (IOException e) {
					e.printStackTrace();
				}
				exerciser.setPhoto(photoName);
			}
			this.exerciserDao.update(exerciser);
			return true;
		}
		return false;
	}

	@Override
	public Exerciser getById(String id) {
		return this.exerciserDao.findByID(id);
	}

}
