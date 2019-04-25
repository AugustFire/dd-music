package com.nercl.music.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.nercl.music.dao.ResultDao;
import com.nercl.music.entity.Result;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ResultDaoImpl extends AbstractBaseDaoImpl<Result, String> implements ResultDao {

	@Override
	public List<Result> list(int page, String exid) {
		String jpql = "from Result re where re.examId = ?1";
		int count = this.executeCountQuery("select count(*) " + jpql, exid);
		List<Result> results = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, exid);
		return PaginateSupportUtil.pagingList(results, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<Result> list(String exid, String name, String examNo, int page) {
		List<String> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from Result re where 1=1");
		if (StringUtils.isNotBlank(exid)) {
			jpql.append(" and re.examId = ?" + (++paramCount));
			params.add(exid);
		}
		if (StringUtils.isNotBlank(name)) {
			jpql.append(" and re.examinee.person.name like ?" + (++paramCount));
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(examNo)) {
			jpql.append(" and re.examinee.examNo like ?" + (++paramCount));
			params.add("%" + examNo + "%");
		}
		jpql.append(" order by re.examineeId, re.examPaperId");
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
		List<Result> results = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE, params.toArray());
		return PaginateSupportUtil.pagingList(results, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<Result> list(String exid, String name, String examNo) {
		List<String> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from Result re where 1=1");
		if (StringUtils.isNotBlank(exid)) {
			jpql.append(" and re.examId = ?" + (++paramCount));
			params.add(exid);
		}
		if (StringUtils.isNotBlank(name)) {
			jpql.append(" and re.examinee.person.name like ?" + (++paramCount));
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(examNo)) {
			jpql.append(" and re.examinee.examNo like ?" + (++paramCount));
			params.add("%" + examNo + "%");
		}
		jpql.append(" order by re.examineeId, re.examPaperId");
		List<Result> results = this.executeQueryWithoutPaging(jpql.toString(), params.toArray());
		return results;
	}

	@Override
	public Result get(String examineeId, String examId, String examPaperId) {
		String jpql = "from Result re where re.examineeId = ?1 and re.examId = ?2 and re.examPaperId = ?3";
		List<Result> results = this.executeQueryWithoutPaging(jpql, examineeId, examId, examPaperId);
		return null == results || results.isEmpty() ? null : results.get(0);
	}

	@Override
	public List<Result> getResultsByAttributes(String name, String idcard, String examNo, int page) {
		List<String> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from Result re where 1=1");
		if (StringUtils.isNotBlank(name)) {
			jpql.append(" and re.examinee.person.name like ?" + (++paramCount));
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(idcard)) {
			jpql.append(" and re.examinee.idcard like ?" + (++paramCount));
			params.add("%" + idcard + "%");
		}
		if (StringUtils.isNotBlank(examNo)) {
			jpql.append(" and re.examinee.examNo like ?" + (++paramCount));
			params.add("%" + examNo + "%");
		}
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
		List<Result> examinees = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE, params.toArray());
		return PaginateSupportUtil.pagingList(examinees, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<Result> getExamineeResults(String examineeId, String examId) {
		String jpql = "from Result re where re.examineeId = ?1 and re.examId = ?2";
		List<Result> results = this.executeQueryWithoutPaging(jpql, examineeId, examId);
		return results;
	}

	@Override
	public void deleteAll() {
		this.entityManager.createQuery("delete from Result re").executeUpdate();
	}

}
