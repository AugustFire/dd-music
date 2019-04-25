package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.LoginDao;
import com.nercl.music.entity.user.Login;

@Repository
public class LoginDaoImpl extends AbstractBaseDaoImpl<Login, String> implements LoginDao {

	@Override
	public Login getByLogin(String login) {
		String jpql = "from Login lg where lg.login = ?1";
		List<Login> logins = executeQueryWithoutPaging(jpql, login);
		return null != logins && !logins.isEmpty() ? logins.get(0) : null;
	}

	@Override
	public Login getByPerson(String personId) {
		String jpql = "from Login lg where lg.personId = ?1";
		List<Login> logins = executeQueryWithoutPaging(jpql, personId);
		return null != logins && !logins.isEmpty() ? logins.get(0) : null;
	}

	@Override
	public Login getByUUIDAndName(String uuid, String name) {
		String jpql = "from Login lg where lg.id = ?1 and lg.person.name = ?2";
		List<Login> logins = executeQueryWithoutPaging(jpql, uuid, name);
		return null != logins && !logins.isEmpty() ? logins.get(0) : null;
	}
}
