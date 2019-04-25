package com.nercl.music.dao;

import com.nercl.music.entity.user.Person;

public interface PersonDao extends BaseDao<Person, String> {

	Person getByEmail(String email);

	Person getByPhone(String phone);

}
