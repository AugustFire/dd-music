package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.base.ClassUser;

public interface ClassUserDao extends BaseDao<ClassUser, String> {

	/**
	 * 根据classId和uid，并删除
	 * 
	 * @param classId
	 *            班级Id
	 * @param uid
	 *            用户Id
	 */
	void delete(String classId, String uid) throws Exception;

	int getStudentNum(String cid);

	List<ClassUser> getSchoolStudent(String sid);

	List<String> getStudentsByClass(String cid);

	List<ClassUser> getStudents(String cid);

	List<ClassUser> getClassTeachers(String sid, String gid);

	List<ClassUser> getSchoolTeachers(String sid);

	int getStudentsAmountBySchoolId(String sid);

}
