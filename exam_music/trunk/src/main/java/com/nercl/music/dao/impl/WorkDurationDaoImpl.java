package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.WorkDurationDao;
import com.nercl.music.entity.WorkDuration;

@Repository
public class WorkDurationDaoImpl extends AbstractBaseDaoImpl<WorkDuration, String> implements WorkDurationDao {

	@Override
	public List<WorkDuration> list(int year) {
		String jpql = "from WorkDuration wd where wd = ?1";
		return this.executeQueryWithoutPaging(jpql, year);
	}

}
