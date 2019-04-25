package com.nercl.music.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.nercl.music.dao.ExamineeDao;
import com.nercl.music.entity.user.Examinee;
import com.nercl.music.entity.user.ExamineeGroup;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ExamineeDaoImpl extends AbstractBaseDaoImpl<Examinee, String> implements ExamineeDao {

	@Override
	public List<Examinee> list() {
		String jpql = "from Examinee ex";
		List<Examinee> examinees = executeQueryWithoutPaging(jpql);
		return examinees;
	}

	@Override
	public List<Examinee> list(int page) {
		String jpql = "from Examinee ex";
		int count = this.executeCountQuery("select count(*) " + jpql);
		List<Examinee> examinees = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE);
		return PaginateSupportUtil.pagingList(examinees, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<Examinee> getByExam(String examId, int page) {
		String jpql = "select ex from Enrollment en.examineeId inner join en.examinee ex where en.examId = ?1";
		int count = this.executeCountQuery("select count(*) " + jpql, examId);
		List<Examinee> examinees = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, examId);
		return PaginateSupportUtil.pagingList(examinees, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public Examinee getByPerson(String personId) {
		String jpql = "from Examinee ex where ex.personId = ?1";
		List<Examinee> examinees = executeQueryWithoutPaging(jpql, personId);
		return null != examinees && !examinees.isEmpty() ? examinees.get(0) : null;
	}

	@Override
	public List<Examinee> list(String school, String idcard, String name, String phone, String email, String examNo,
	        int page) {
		List<String> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from Examinee ex where 1=1");
		if (StringUtils.isNotBlank(name)) {
			jpql.append(" and ex.person.name like ?" + (++paramCount));
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(idcard)) {
			jpql.append(" and ex.idcard like ?" + (++paramCount));
			params.add("%" + idcard + "%");
		}
		if (StringUtils.isNotBlank(examNo)) {
			jpql.append(" and ex.examNo like ?" + (++paramCount));
			params.add("%" + examNo + "%");
		}
		if (StringUtils.isNotBlank(school)) {
			jpql.append(" and ex.school like ?" + (++paramCount));
			params.add("%" + school + "%");
		}
		if (StringUtils.isNotBlank(phone)) {
			jpql.append(" and ex.person.phone like ?" + (++paramCount));
			params.add("%" + phone + "%");
		}
		if (StringUtils.isNotBlank(email)) {
			jpql.append(" and ex.person.email like ?" + (++paramCount));
			params.add("%" + email + "%");
		}
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
		List<Examinee> examinees = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE,
		        params.toArray());
		return PaginateSupportUtil.pagingList(examinees, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<Examinee> getByGroup(List<ExamineeGroup> groups) {
		List<String> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from Examinee ex where 1=1");
		for (ExamineeGroup group : groups) {
			paramCount++;
			if (paramCount == 1) {
				jpql.append(" and (ex.groupId = ?" + paramCount);
			} else {
				jpql.append(" or ex.groupId = ?" + paramCount);
			}
			if (paramCount == groups.size()) {
				jpql.append(")");
			}
			params.add(group.getId());
		}
		List<Examinee> examinees = this.executeQueryWithoutPaging(jpql.toString(), params.toArray());
		return examinees;
	}

}
