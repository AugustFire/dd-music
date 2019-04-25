package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.base.School;

public interface SchoolService {

	void save(School school);

	void deleteById(String id) throws Exception;

	School findById(String sid);

	void update(School chool);

	List<School> findByConditions(School school) throws Exception;

	/**
	 * 根据学校编码查询学校
	 * 
	 * @param schoolCode
	 *            学校编码
	 */
	School findByCode(String schoolCode);


}
