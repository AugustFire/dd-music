package com.nercl.music.service;

import com.nercl.music.entity.MailRetrieve;

public interface MailRetrieveService {

	void findPassword(String login,String email, String url);

	MailRetrieve getByEmail(String account);

	boolean authentication(String login, String sid);

	void deleteByEmail(String account);

	void checkMail(String userId, String url, String email);

}
