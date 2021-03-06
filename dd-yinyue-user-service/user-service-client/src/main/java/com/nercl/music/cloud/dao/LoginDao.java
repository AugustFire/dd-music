package com.nercl.music.cloud.dao;

import com.nercl.music.cloud.entity.user.Login;

public interface LoginDao extends BaseDao<Login, String> {

	Login getByLogin(String login);

	Login getByPerson(String personId);

	Login getByUUIDAndName(String uuid, String name);

}
