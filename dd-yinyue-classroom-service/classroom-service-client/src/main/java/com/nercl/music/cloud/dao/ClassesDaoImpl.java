package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.base.Classes;

@Repository
public class ClassesDaoImpl extends AbstractBaseDaoImpl<Classes, String> implements ClassesDao {

	@Override
	public List<Classes> getBySchool(String sid) {
		String jpql = "from Classes cs where cs.schoolId = ?1";
		List<Classes> classes = this.executeQueryWithoutPaging(jpql, sid);
		return classes;
	}

	@Override
	public List<Classes> getClassBySchoolGrade(String sid, String gid) {
		String jpql = "from Classes cs where cs.schoolId = ?1 and cs.gradeId = ?2 order by cs.orderBy";
		List<Classes> classes = this.executeQueryWithoutPaging(jpql, sid, gid);
		return classes;
	}

	@Override
	public List<Classes> getClassByTeacher(String tid) {
		String jpql = "from Classes cs where cs.teacherId = ?1";
		List<Classes> classes = this.executeQueryWithoutPaging(jpql, tid);
		return classes;
	}

	@Override
	public List<Classes> getClassByTeacherGrade(String tid, String gid) {
		String jpql = "from Classes cs where cs.teacherId = ?1 and cs.gradeId = ?2";
		List<Classes> classes = this.executeQueryWithoutPaging(jpql, tid, gid);
		return classes;
	}

	@Override
	public List<Classes> getClassBySchool(String sid) {
		String jpql = "from Classes cs where cs.schoolId = ?1";
		List<Classes> classes = this.executeQueryWithoutPaging(jpql, sid);
		return classes;
	}

}
