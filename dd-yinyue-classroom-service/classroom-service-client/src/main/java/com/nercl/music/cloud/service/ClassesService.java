package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Set;

import com.nercl.music.cloud.entity.base.Classes;
import com.nercl.music.cloud.entity.base.Grade;

public interface ClassesService {

	void save(Classes classes);

	void deleteById(String id) throws Exception;

	Classes findById(String cid);

	void update(Classes classes);

	List<Classes> findByConditions(Classes classes) throws Exception;

	List<Classes> findByConditions(Classes classes, int page) throws Exception;

	Set<Grade> getGradesBySchool(String sid);

	List<Classes> getClassBySchoolGrade(String sid, String gid);

	List<Classes> getClassByTeacher(String tid);

	List<Classes> getClassByTeacherGrade(String tid, String gid);

	List<Classes> getClassBySchool(String sid);

}
