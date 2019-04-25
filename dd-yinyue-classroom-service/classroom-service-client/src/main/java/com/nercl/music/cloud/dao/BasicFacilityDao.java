package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.base.BasicFacility;

public interface BasicFacilityDao extends BaseDao<BasicFacility, String> {

	List<BasicFacility> get(String title, String schoolId);

	List<BasicFacility> get(String schoolId);

}
