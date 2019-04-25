package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.ManagerDao;
import com.nercl.music.entity.user.Manager;

@Repository
public class ManagerDaoImpl extends AbstractBaseDaoImpl<Manager, String> implements ManagerDao {

	@Override
	public Manager getByPerson(String personId) {
		String jpql = "from Manager ma where ma.personId = ?1";
		List<Manager> managers = executeQueryWithoutPaging(jpql, personId);
		return null != managers && !managers.isEmpty() ? managers.get(0) : null;
	}

	@Override
	public Manager getSuperManager() {
		String jpql = "from Manager ma where ma.isSuper = ?1";
		List<Manager> managers = executeQueryWithoutPaging(jpql, true);
		return null != managers && !managers.isEmpty() ? managers.get(0) : null;
	}

}
