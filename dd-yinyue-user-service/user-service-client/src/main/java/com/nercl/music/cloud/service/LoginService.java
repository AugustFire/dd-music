package com.nercl.music.cloud.service;

import com.nercl.music.cloud.entity.user.Login;
import com.nercl.music.cloud.entity.user.Person;

public interface LoginService {

	boolean save(Login login);

	boolean save(String phone, String password, Boolean isTeacher);

	Person saveByEmail(String email, String password, Boolean isTeacher);

	Person saveByEmail(String email, String name, String password, Boolean isTeacher);

	boolean repassword(String email, String password);

	boolean authenticate(String login, String password);

	Login getByUUIDAndName(String uuid, String name);

	Login getByLogin(String login);

	Login getByEmail(String email);

	boolean exsitLogin(String login);

	boolean exsitEmail(String email);

	boolean exsitPhone(String phone);

	Login getByPerson(String personId);

	boolean rePassword(String login, String passowrd);

	void delete(Login login);

	String setLoginToken(String login);

}
