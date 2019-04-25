package com.nercl.music.cloud.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.base.ClassUser;

@Repository
public class ClassUserDaoImpl extends AbstractBaseDaoImpl<ClassUser, String> implements ClassUserDao {

	@Override
	public void delete(String classId, String uid) throws Exception {
		ClassUser cs = new ClassUser();
		cs.setClassId(classId);
		cs.setUserId(uid);
		List<ClassUser> list = this.findByConditions(cs);
		// 根据classId和uid可以确定唯一一个ClassStudent
		if (!list.isEmpty()) {
			this.delete(list.get(0));
		}
	}

	@Override
	public int getStudentNum(String cid) {
		String jpql = "from ClassUser cu where cu.classId = ?1 and cu.isTeacher = ?2";
		int count = this.executeCountQuery("select count(*) " + jpql, cid, false);
		return count;
	}

	@Override
	public List<ClassUser> getSchoolStudent(String sid) {
		String jpql = "from ClassUser cu where cu.schoolId = ?1 and cu.isTeacher = ?2";
		return this.executeQueryWithoutPaging(jpql, sid, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getStudentsByClass(String cid) {
		String jpql = "select DISTINCT(cu.studentId) from ClassUser cu where cu.classId = ?1 and cu.isTeacher = ?2 ";
		Query query = entityManager.createQuery(jpql);
		query.setParameter(1, cid);
		query.setParameter(2, false);
		return (List<String>) query.getResultList();
	}

	@Override
	public List<ClassUser> getStudents(String cid) {
		String jpql = "from ClassUser cu where cu.classId = ?1 and cu.isTeacher = ?2";
		List<ClassUser> cus = this.executeQueryWithoutPaging(jpql, cid, false);
		return cus;
	}

	@Override
	public List<ClassUser> getClassTeachers(String sid, String gid) {
		String jpql = "from ClassUser cu where cu.schoolId = ?1 and cu.gradeId = ?2 and cu.isTeacher = true";
		return this.executeQueryWithoutPaging(jpql, sid, gid);
	}

	@Override
	public List<ClassUser> getSchoolTeachers(String sid) {
		String jpql = "from ClassUser cu where cu.schoolId = ?1 and cu.isTeacher = true";
		return this.executeQueryWithoutPaging(jpql, sid);
	}

	@Override
	public int getStudentsAmountBySchoolId(String sid) {
		String jpql = "from ClassUser cu where cu.schoolId = ?1 and cu.isTeacher = false";
		int count = this.executeCountQuery("select count(*) " + jpql, sid);
		return count;
	}
}
