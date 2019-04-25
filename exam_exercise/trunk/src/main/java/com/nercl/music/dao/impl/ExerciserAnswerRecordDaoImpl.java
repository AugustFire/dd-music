package com.nercl.music.dao.impl;

import java.util.List;

import com.google.common.collect.Lists;
import com.nercl.music.util.page.PaginateSupportUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.nercl.music.dao.ExerciserAnswerRecordDao;
import com.nercl.music.entity.question.ExerciserAnswerRecord;

@Repository
public class ExerciserAnswerRecordDaoImpl extends AbstractBaseDaoImpl<ExerciserAnswerRecord, String>
		implements ExerciserAnswerRecordDao {

	@Override
	public List<ExerciserAnswerRecord> get(String personId, String examQuestionId) {
		String jpql = "from ExerciserAnswerRecord cr where cr.personId = ?1 and cr.examQuestionId = ?2";
		List<ExerciserAnswerRecord> records = this.executeQueryWithoutPaging(jpql, personId, examQuestionId);
		return records;
	}

	@Override
	public List<ExerciserAnswerRecord> list(String name, String topicId, Integer questionType, int page) {
		List<Object> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from ExerciserAnswerRecord ar where 1=1");
		if (StringUtils.isNotBlank(name)) {
			jpql.append(" and ar.person.name like ?").append(++paramCount);
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(topicId)) {
			jpql.append(" and ar.topicId = ?").append(++paramCount);
			params.add(topicId);
		}
		if (questionType > 0) {
			jpql.append(" and ar.questionType = ?").append(++paramCount);
			params.add(questionType);
		}
		jpql.append(" order by ar.creatAt desc");
		int count = this.executeCountQuery("select count(*) " + jpql, params.toArray());
		List<ExerciserAnswerRecord> answerRecords = this.executeQueryWithPaging("select ar " + jpql.toString(), page,
				DEFAULT_PAGESIZE, params.toArray());
		return PaginateSupportUtil.pagingList(answerRecords, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<ExerciserAnswerRecord> listByPerson(String personId, String topicId, Integer questionType, int page) {
		List<Object> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from ExerciserAnswerRecord ar where 1=1");
		if (StringUtils.isNotBlank(personId)) {
			jpql.append(" and ar.personId = ?").append(++paramCount);
			params.add(personId);
		}
		if (StringUtils.isNotBlank(topicId)) {
			jpql.append(" and ar.topicId = ?").append(++paramCount);
			params.add(topicId);
		}
		if (questionType > 0) {
			jpql.append(" and ar.questionType = ?").append(++paramCount);
			params.add(questionType);
		}
		jpql.append(" order by ar.creatAt desc");
		int count = this.executeCountQuery("select count(*) " + jpql, params.toArray());
		List<ExerciserAnswerRecord> answerRecords = this.executeQueryWithPaging("select ar " + jpql.toString(), page,
				DEFAULT_PAGESIZE, params.toArray());
		return PaginateSupportUtil.pagingList(answerRecords, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public ExerciserAnswerRecord get(String topicId, String personId, String examQuestionId) {
		String jpql = "from ExerciserAnswerRecord ar where ar.topicId = ?1 and ar.personId = ?2 and ar.examQuestionId = ?3";
		List<ExerciserAnswerRecord> answerRecords = this.executeQueryWithoutPaging(jpql, topicId, personId, examQuestionId);
		return null == answerRecords || answerRecords.isEmpty() ? null : answerRecords.get(0);
	}

	@Override
	public boolean hasAnswers(String personId, String topicId, String examQuestionId) {
		String jpql = "select count(*) from ExerciserAnswerRecord ar where ar.personId = ?1 and ar.topicId = ?2 and ar.examQuestionId = ?3";
		int count = this.executeCountQuery(jpql, personId, topicId, examQuestionId);
		return count > 0;
	}

	@Override
	public List<ExerciserAnswerRecord> get(String examQuestionId) {
		String jpql = "from ExerciserAnswerRecord cr where cr.examQuestionId = ?1";
		return this.executeQueryWithoutPaging(jpql, examQuestionId);
	}
}
