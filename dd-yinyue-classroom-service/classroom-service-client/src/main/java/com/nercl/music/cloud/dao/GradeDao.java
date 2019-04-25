package com.nercl.music.cloud.dao;

import com.nercl.music.cloud.entity.base.Grade;

public interface GradeDao extends BaseDao<Grade, String>{

	Grade findByCode(String gradeCode);

}
