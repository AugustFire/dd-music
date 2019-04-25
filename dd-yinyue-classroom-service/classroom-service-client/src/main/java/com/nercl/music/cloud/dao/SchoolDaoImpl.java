package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.base.School;

@Repository
public class SchoolDaoImpl extends AbstractBaseDaoImpl<School, String> implements SchoolDao {

	@Override
	public School findByCode(String code) {
		String jpql = "from School s where s.code = ?1";
		List<School> schools = this.executeQueryWithoutPaging(jpql, code);
		if(!schools.isEmpty()){
			return schools.get(0);
		}
		return null;
	}

	

}
