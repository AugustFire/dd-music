package com.nercl.music.dao;

import com.nercl.music.entity.user.MailRetrieve;

public interface MailRetrieveDao extends BaseDao<MailRetrieve, Long> {

	MailRetrieve getByEmail(String account);

	void deleteByAccount(String account);

	MailRetrieve getByEmailAndSid(String email, String sid);

	MailRetrieve getByEmailAndUser(String email, Long uid);

}
