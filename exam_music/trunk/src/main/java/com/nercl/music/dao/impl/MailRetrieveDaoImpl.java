package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.MailRetrieveDao;
import com.nercl.music.entity.MailRetrieve;

@Repository
public class MailRetrieveDaoImpl extends AbstractBaseDaoImpl<MailRetrieve, Long> implements MailRetrieveDao {

	@Override
	public MailRetrieve getByEmail(String email) {
		String jpql = "from MailRetrieve mr where mr.email = ?1";
		List<MailRetrieve> mailRetrieves = executeQueryWithoutPaging(jpql, email);
		return null != mailRetrieves && !mailRetrieves.isEmpty() ? mailRetrieves.get(0) : null;
	}

	@Override
	public void deleteByAccount(String account) {
		String jpql = "delete MailRetrieve mr where mr.account = " + account;
		this.entityManager.createQuery(jpql).executeUpdate();
	}

	@Override
	public MailRetrieve getByLoginAndSid(String login, String sid) {
		String jpql = "from MailRetrieve mr where mr.userId = ?1 and mr.sid = ?2";
		List<MailRetrieve> mailRetrieves = executeQueryWithoutPaging(jpql, login, sid);
		return null != mailRetrieves && !mailRetrieves.isEmpty() ? mailRetrieves.get(0) : null;
	}

	@Override
	public MailRetrieve getByEmailAndUser(String email, String uid) {
		// TODO Auto-generated method stub
		return null;
	}

}
