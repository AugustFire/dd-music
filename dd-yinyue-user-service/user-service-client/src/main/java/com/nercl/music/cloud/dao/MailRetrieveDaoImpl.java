package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.user.MailRetrieve;

@Repository
public class MailRetrieveDaoImpl extends AbstractBaseDaoImpl<MailRetrieve, Long> implements MailRetrieveDao {

	@Override
	public MailRetrieve getByEmail(String account) {
		String jpql = "from MailRetrieve mr where mr.email = ?1";
		List<MailRetrieve> mailRetrieves = executeQueryWithoutPaging(jpql, account);
		return null != mailRetrieves && !mailRetrieves.isEmpty() ? mailRetrieves.get(0) : null;
	}

	@Override
	public void deleteByAccount(String account) {
		String jpql = "delete MailRetrieve mr where mr.account = " + account;
		this.entityManager.createQuery(jpql).executeUpdate();
	}

	@Override
	public MailRetrieve getByEmailAndSid(String email, String sid) {
		String jpql = "from MailRetrieve mr where mr.email = ?1 and mr.sid = ?2";
		List<MailRetrieve> mailRetrieves = executeQueryWithoutPaging(jpql, email, sid);
		return null != mailRetrieves && !mailRetrieves.isEmpty() ? mailRetrieves.get(0) : null;
	}

	@Override
	public MailRetrieve getByEmailAndUser(String email, Long uid) {
		// TODO Auto-generated method stub
		return null;
	}

}
