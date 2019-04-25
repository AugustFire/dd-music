package com.nercl.music.service;

import com.nercl.music.entity.user.Login;

public interface LoginService {

	boolean save(Login login);

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

	String getToken(String uid);

}
