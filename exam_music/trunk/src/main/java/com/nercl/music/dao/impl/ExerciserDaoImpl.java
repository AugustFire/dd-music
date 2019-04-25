package com.nercl.music.dao.impl;

import java.util.List;

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

}
