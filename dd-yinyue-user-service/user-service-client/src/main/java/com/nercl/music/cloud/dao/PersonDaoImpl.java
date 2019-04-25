package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.user.Person;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class PersonDaoImpl extends AbstractBaseDaoImpl<Person, String> implements PersonDao {

	@Override
	public List<Person> list() {
		String jpql = "from Person p";
		List<Person> persons = executeQueryWithoutPaging(jpql);
		return persons;
	}

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

	@Override
	public List<Person> query(String key, int page) {
		String jpql = "from Person p where p.name like ?1 or p.email like ?2";
		List<Person> persons = executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, "%" + key + "%", "%" + key + "%");
		int count = this.executeCountQuery("select count(*) " + jpql, "%" + key + "%", "%" + key + "%");
		return PaginateSupportUtil.pagingList(persons, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public Person getByLogin(String login) {
		String jpql = "from Person p where p.name = ?1 or p.email = ?2";
		List<Person> persons = executeQueryWithoutPaging(jpql, login, login);
		return null == persons || persons.isEmpty() ? null : persons.get(0);
	}

}
