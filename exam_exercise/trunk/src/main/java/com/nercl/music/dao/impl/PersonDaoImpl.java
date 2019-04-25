package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.PersonDao;
import com.nercl.music.entity.user.Person;
import com.nercl.music.util.page.PaginateSupportArray;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class PersonDaoImpl extends AbstractBaseDaoImpl<Person, String> implements PersonDao {

    @Override
    public PaginateSupportArray<Person> list(int page)
    {
        String jpql = "from Person p";
        int count = this.executeCountQuery("select count(*) " + jpql);
        List<Person> persons = executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE);
        PaginateSupportArray<Person> pagingList = PaginateSupportUtil.pagingList(persons, page, DEFAULT_PAGESIZE,count);
        return pagingList;
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
	public List<Person> query(String key) {
		String jpql = "from Person p where p.name like ?1 or p.email like ?2";
		List<Person> persons = executeQueryWithoutPaging(jpql, "%" + key + "%", "%" + key + "%");
		return persons;
	}

}
