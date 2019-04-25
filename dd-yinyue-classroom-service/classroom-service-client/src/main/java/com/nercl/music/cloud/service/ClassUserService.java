package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.base.ClassUser;
import com.nercl.music.cloud.entity.base.Classes;
import com.nercl.music.cloud.entity.base.Grade;

public interface ClassUserService {

	void save(String uid, String name, String classId, String gradeId, String schoolId, boolean isTeacher);

	List<ClassUser> findByCondionts(ClassUser cs) throws Exception;

	int getStudentNum(String cid);

	Map<String, Integer> getManAndWomanStudentNum(String cid);

	int getSchoolStudentNum(String sid);

	List<Grade> getGradesByTeacher(String tid);

	List<Grade> getGradesBySchool(String sid);

	List<Classes> getClassByTeacherGrade(String tid, String gid);

	List<Classes> getClassBySchoolGrade(String sid, String gid);

	List<Classes> getClassBySchool(String sid);

	List<Classes> getClassByTeacher(String tid);

	List<Map<String, Object>> getStudentsByClass(String cid);

	List<Map<String, String>> getClassTeachers(String sid, String gid);

	List<Map<String, Object>> getSchoolTeachers(String sid);

	List<ClassUser> getStudents(String classId);

	int getStudentsAmountBySchoolId(String sid);

}
