package com.nercl.music.service;

import com.nercl.music.entity.user.Login;

public interface LoginService {

	boolean save(Login login);

	/**
	 * 注册练习者
	 * 
	 * @param username
	 *            用户姓名
	 * @param email
	 *            邮箱
	 * @param password
	 *            密码
	 * @return
	 */
	boolean save(String username, String email, String password);

	boolean repassword(String email, String sid, String password);

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

	boolean logout(String exerciserId, String consumeRecordId);

	String setLoginToken(String login);

}
