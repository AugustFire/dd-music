package com.nercl.music.dao.impl;

import java.util.List;

import com.google.common.collect.Lists;
import com.nercl.music.util.page.PaginateSupportUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.nercl.music.dao.ExerciserDao;
import com.nercl.music.entity.user.Exerciser;

@Repository
public class ExerciserDaoImpl extends AbstractBaseDaoImpl<Exerciser, String> implements ExerciserDao {

	@Override
	public Exerciser getByPerson(String personId) {
		String jpql = "from Exerciser ex where ex.personId = ?1";
		List<Exerciser> exercisers = executeQueryWithoutPaging(jpql, personId);
		return null != exercisers && !exercisers.isEmpty() ? exercisers.get(0) : null;
	}

	@Override
	public List<Exerciser> list(int page, String name, String email) {
		String jpql = "from Exerciser ex where 1=1";
		List<Object> params = Lists.newArrayList();
		int i = 1;
		if (StringUtils.isNotBlank(name)) {
			jpql += " and ex.person.name like ?" + i;
			params.add("%" + name + "%");
			i++;
		}
		if (StringUtils.isNotBlank(email)) {
			jpql += " and ex.person.email like ?" + i;
			params.add("%" + email + "%");
		}
		jpql += " order by ex.createAt desc";

		int count = this.executeCountQuery("select count(*) " + jpql, params.toArray());
		List<Exerciser> tasks = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, params.toArray());
		return PaginateSupportUtil.pagingList(tasks, page, DEFAULT_PAGESIZE, count);
	}

}
