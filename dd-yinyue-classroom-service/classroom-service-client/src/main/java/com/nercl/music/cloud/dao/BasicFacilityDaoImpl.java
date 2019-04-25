package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.base.BasicFacility;

@Repository
public class BasicFacilityDaoImpl extends AbstractBaseDaoImpl<BasicFacility, String> implements BasicFacilityDao {

	@Override
	public List<BasicFacility> get(String title, String schoolId) {
		String jpql = "from BasicFacility bf where bf.title = ?1 and bf.schoolId = ?2";
		return this.executeQueryWithoutPaging(jpql, title, schoolId);
	}

	@Override
	public List<BasicFacility> get(String schoolId) {
		String jpql = "from BasicFacility bf where bf.schoolId = ?1";
		return this.executeQueryWithoutPaging(jpql, schoolId);
	}

}
