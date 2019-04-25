package com.nercl.music.cloud.dao;

import com.nercl.music.cloud.entity.base.School;

public interface SchoolDao extends BaseDao<School, String>{

	/**
	 * 根据学校编码查询学校
	 * 
	 * @param schoolCode
	 *            学校编码
	 */
	School findByCode(String schoolCode);


}
