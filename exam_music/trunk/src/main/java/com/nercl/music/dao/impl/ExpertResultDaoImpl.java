package com.nercl.music.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.nercl.music.dao.ExpertResultDao;
import com.nercl.music.entity.ExpertResult;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ExpertResultDaoImpl extends AbstractBaseDaoImpl<ExpertResult, String> implements ExpertResultDao {

	@Override
	public ExpertResult get(String examId, String examPaperId, String examineeId, String examQuestionId,
	        String expertId) {
		List<String> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from ExpertResult er where 1=1");
		if (StringUtils.isNotBlank(examId)) {
			jpql.append(" and er.examId = ?" + (++paramCount));
			params.add(examId);
		}
		if (StringUtils.isNotBlank(examPaperId)) {
			jpql.append(" and er.examPaperId = ?" + (++paramCount));
			params.add(examPaperId);
		}
		if (StringUtils.isNotBlank(examineeId)) {
			jpql.append(" and er.examineeId = ?" + (++paramCount));
			params.add(examineeId);
		}
		if (StringUtils.isNotBlank(examQuestionId)) {
			jpql.append(" and er.examQuestionId = ?" + (++paramCount));
			params.add(examQuestionId);
		}
		if (StringUtils.isNotBlank(expertId)) {
			jpql.append(" and er.expertId = ?" + (++paramCount));
			params.add(examQuestionId);
		}
		List<ExpertResult> expertResults = this.executeQueryWithoutPaging(jpql.toString(), params.toArray());
		return null == expertResults || expertResults.isEmpty() ? null : expertResults.get(0);
	}

	@Override
	public List<ExpertResult> get(String examId, String examPaperId, String examineeId) {
		List<String> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from ExpertResult er where 1=1");
		if (StringUtils.isNotBlank(examId)) {
			jpql.append(" and er.examId = ?" + (++paramCount));
			params.add(examId);
		}
		if (StringUtils.isNotBlank(examPaperId)) {
			jpql.append(" and er.examPaperId = ?" + (++paramCount));
			params.add(examPaperId);
		}
		if (StringUtils.isNotBlank(examineeId)) {
			jpql.append(" and er.examineeId = ?" + (++paramCount));
			params.add(examineeId);
		}
		List<ExpertResult> expertResults = this.executeQueryWithoutPaging(jpql.toString(), params.toArray());
		return expertResults;
	}

	@Override
	public List<ExpertResult> get(String examId, String examPaperId, String examQuestionId, String examineeId) {
		List<String> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from ExpertResult er where 1=1");
		if (StringUtils.isNotBlank(examId)) {
			jpql.append(" and er.examId = ?" + (++paramCount));
			params.add(examId);
		}
		if (StringUtils.isNotBlank(examPaperId)) {
			jpql.append(" and er.examPaperId = ?" + (++paramCount));
			params.add(examPaperId);
		}
		if (StringUtils.isNotBlank(examQuestionId)) {
			jpql.append(" and er.examQuestionId = ?" + (++paramCount));
			params.add(examQuestionId);
		}
		if (StringUtils.isNotBlank(examineeId)) {
			jpql.append(" and er.examineeId = ?" + (++paramCount));
			params.add(examineeId);
		}
		List<ExpertResult> expertResults = this.executeQueryWithoutPaging(jpql.toString(), params.toArray());
		return expertResults;
	}

	@Override
	public List<ExpertResult> list(String examId, String name, String examNo, String expertName, int page) {
		List<String> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from ExpertResult er where 1=1");
		if (StringUtils.isNotBlank(examId)) {
			jpql.append(" and er.examId = ?" + (++paramCount));
			params.add(examId);
		}
		if (StringUtils.isNotBlank(name)) {
			jpql.append(" and er.examinee.person.name like ?" + (++paramCount));
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(examNo)) {
			jpql.append(" and er.examinee.examNo like ?" + (++paramCount));
			params.add("%" + examNo + "%");
		}
		if (StringUtils.isNotBlank(expertName)) {
			jpql.append(" and er.expert.person.name like  ?" + (++paramCount));
			params.add("%" + expertName + "%");
		}
		jpql.append(" order by er.examineeId asc");
		int count = this.executeCountQuery("select count(*) " + jpql, params.toArray());
		List<ExpertResult> expertResults = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE,
		        params.toArray());
		return PaginateSupportUtil.pagingList(expertResults, page, DEFAULT_PAGESIZE, count);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAvgScore() {
		String jpql = "select er.examId,er.examPaperId,er.examQuestionId,er.examineeId,AVG(er.score) from ExpertResult er group by er.examId,er.examPaperId,er.examQuestionId,er.examineeId";
		Query query = entityManager.createQuery(jpql);
		List<Object[]> datas = query.getResultList();
		return datas;
	}

	@Override
	public List<ExpertResult> getBigDeviationResults(List<Object[]> avgScores) {
		StringBuilder sb = new StringBuilder("from ExpertResult er where");
		List<Object> params = Lists.newArrayList();
		int count = 0;
		int paramCount = 0;
		for (Object[] data : avgScores) {
			if (count > 0) {
				sb.append(" or");
			}
			sb.append(" (er.examId = ?" + (++paramCount) + " and er.examPaperId = ?" + (++paramCount)
			        + " and er.examQuestionId = ?" + (++paramCount) + " and er.examineeId = ?" + (++paramCount)
			        + " and ((er.score - 20 >= ?" + (++paramCount) + ") or (er.score + 20 <= ?" + (++paramCount)
			        + ")))");
			params.add(data[0]);
			params.add(data[1]);
			params.add(data[2]);
			params.add(data[3]);
			params.add(((Number) data[4]).intValue());
			params.add(((Number) data[4]).intValue());
			count++;
		}
		List<ExpertResult> results = this.executeQueryWithoutPaging(sb.toString(), params.toArray());
		return results;
	}

}
