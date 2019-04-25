package com.nercl.music.cloud.dao;

import com.nercl.music.cloud.entity.user.MailRetrieve;

public interface MailRetrieveDao extends BaseDao<MailRetrieve, Long> {

	MailRetrieve getByEmail(String account);

	void deleteByAccount(String account);

	MailRetrieve getByEmailAndSid(String email, String sid);

	MailRetrieve getByEmailAndUser(String email, Long uid);

}
