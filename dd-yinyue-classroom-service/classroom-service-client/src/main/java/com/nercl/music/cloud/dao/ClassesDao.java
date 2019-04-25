package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.base.Classes;

public interface ClassesDao extends BaseDao<Classes, String> {

	List<Classes> getBySchool(String sid);

	List<Classes> getClassBySchoolGrade(String sid, String gid);

	List<Classes> getClassByTeacher(String tid);

	List<Classes> getClassByTeacherGrade(String tid, String gid);

	List<Classes> getClassBySchool(String sid);

}
