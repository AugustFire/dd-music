package com.nercl.music.dao;

import com.nercl.music.entity.MailRetrieve;

public interface MailRetrieveDao extends BaseDao<MailRetrieve, Long> {

	MailRetrieve getByEmail(String account);

	void deleteByAccount(String account);

	MailRetrieve getByLoginAndSid(String email, String sid);

	MailRetrieve getByEmailAndUser(String email, String uid);

}
