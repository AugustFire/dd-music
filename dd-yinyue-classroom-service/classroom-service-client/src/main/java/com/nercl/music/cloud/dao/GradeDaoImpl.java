package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.base.Grade;

@Repository
public class GradeDaoImpl extends AbstractBaseDaoImpl<Grade, String> implements GradeDao {

	@Override
	public Grade findByCode(String gradeCode) {
		String jpql = "from Grade grd where grd.code = ?1";
		List<Grade> gradeStages = this.executeQueryWithoutPaging(jpql, gradeCode);
		return null == gradeStages || gradeStages.isEmpty() ? null : gradeStages.get(0);
	}

}
