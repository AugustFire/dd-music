package com.nercl.music.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.nercl.music.dao.EnrollmentDao;
import com.nercl.music.entity.Enrollment;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class EnrollmentDaoImpl extends AbstractBaseDaoImpl<Enrollment, String> implements EnrollmentDao {

	@Override
	public List<Enrollment> list(String examId, int page) {
		String jpql = "from Enrollment en where en.examId = ?1";
		int count = this.executeCountQuery("select count(*) " + jpql, examId);
		List<Enrollment> enrollments = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, examId);
		return PaginateSupportUtil.pagingList(enrollments, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<Enrollment> listByStatus(String examId, Enrollment.Status status, int page) {
		String jpql = "from Enrollment en where en.examId = ?1";
		int count = this.executeCountQuery("select count(*) " + jpql);
		List<Enrollment> enrollments = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE);
		return PaginateSupportUtil.pagingList(enrollments, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public Enrollment get(String exid, String eeid) {
		String jpql = "from Enrollment en where en.examId = ?1 and en.examineeId = ?2";
		List<Enrollment> enrollments = this.executeQueryWithoutPaging(jpql, exid, eeid);
		return null == enrollments || enrollments.isEmpty() ? null : enrollments.get(0);
	}

	@Override
	public List<Enrollment> getByAttributes(String name, String examPoint, String examRoom, String examNo,String status, int page) {
		List<Object> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from Enrollment en where 1=1");
		if (StringUtils.isNotBlank(name)) {
			paramCount++;
			jpql.append(" and en.examinee.person.name like ?" + paramCount);
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(examRoom)) {
			paramCount++;
			jpql.append(" and en.examRoom.title like ?" + paramCount);
			params.add("%" + examRoom + "%");
		}
		if (StringUtils.isNotBlank(examPoint)) {
			paramCount++;
			jpql.append(" and en.examRoom.examPoint.name like ?" + paramCount);
			params.add("%" + examPoint + "%");
		}
		if (StringUtils.isNotBlank(examNo)) {
			paramCount++;
			jpql.append(" and en.examinee.examNo like ?" + paramCount);
			params.add("%" + examNo + "%");
		}
		if (status.equals("PASSED")) {
			paramCount++;
			jpql.append(" and en.status = ?" + paramCount);
			params.add(Enrollment.Status.PASSED);
		}
		if (status.equals("UN_PASSED")) {
			paramCount++;
			jpql.append(" and en.status = ?" + paramCount);
			params.add(Enrollment.Status.UN_PASSED);
		}
		if (status.equals("FOR_CHECKED")) {
			paramCount++;
			jpql.append(" and en.status = ?" + paramCount);
			params.add(Enrollment.Status.FOR_CHECKED);
		}
		
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
		List<Enrollment> examinees = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE,
		        params.toArray());
		return PaginateSupportUtil.pagingList(examinees, page, DEFAULT_PAGESIZE, count);
	}

}
