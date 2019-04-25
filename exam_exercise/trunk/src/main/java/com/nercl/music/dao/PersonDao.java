package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.user.Person;
import com.nercl.music.util.page.PaginateSupportArray;

public interface PersonDao extends BaseDao<Person, String> {

	PaginateSupportArray<Person> list(int page);

	Person getByEmail(String email);

	Person getByPhone(String phone);

	List<Person> query(String key);

}
