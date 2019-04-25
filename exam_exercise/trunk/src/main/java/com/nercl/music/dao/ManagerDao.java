package com.nercl.music.dao;

import com.nercl.music.entity.user.Manager;

public interface ManagerDao extends BaseDao<Manager, String> {

	Manager getByPerson(String personId);

	Manager getSuperManager();

}
