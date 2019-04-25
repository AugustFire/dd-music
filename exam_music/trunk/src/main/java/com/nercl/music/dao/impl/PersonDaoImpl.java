package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.PersonDao;
import com.nercl.music.entity.user.Person;

@Repository
public class PersonDaoImpl extends AbstractBaseDaoImpl<Person, String> implements PersonDao {

	@Override
	public Person getByEmail(String email) {
		String jpql = "from Person p where p.email = ?1";
		List<Person> persons = executeQueryWithoutPaging(jpql, email);
		return null != persons && !persons.isEmpty() ? persons.get(0) : null;
	}

	@Override
	public Person getByPhone(String phone) {
		String jpql = "from Person p where p.phone = ?1";
		List<Person> persons = executeQueryWithoutPaging(jpql, phone);
		return null != persons && !persons.isEmpty() ? persons.get(0) : null;
	}

}
