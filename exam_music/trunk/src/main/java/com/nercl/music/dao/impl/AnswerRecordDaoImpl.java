package com.nercl.music.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.nercl.music.dao.AnswerRecordDao;
import com.nercl.music.entity.AnswerRecord;
import com.nercl.music.entity.user.Examinee;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class AnswerRecordDaoImpl extends AbstractBaseDaoImpl<AnswerRecord, String> implements AnswerRecordDao {

	@Override
	public List<AnswerRecord> list(Integer questionType) {
		String jpql = "from AnswerRecord ar where ar.questionType = ?1";
		List<AnswerRecord> answerRecords = this.executeQueryWithoutPaging(jpql, questionType);
		return answerRecords;
	}

	@Override
	public List<AnswerRecord> list(String examineeId, int page) {
		String jpql = "from AnswerRecord ar where ar.examineeId = ?1";
		int count = this.executeCountQuery("select count(*) " + jpql, examineeId);
		List<AnswerRecord> answerRecords = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, examineeId);
		return PaginateSupportUtil.pagingList(answerRecords, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<AnswerRecord> list(String examId, String examPaperId, Integer questionType) {
		String jpql = "from AnswerRecord ar where ar.examId = ?1 and ar.examPaperId = ?2 and ar.questionType = ?3";
		List<AnswerRecord> answerRecords = this.executeQueryWithoutPaging(jpql, examId, examPaperId, questionType);
		return answerRecords;
	}

	@Override
	public List<AnswerRecord> listNoScore(String examId) {
		String jpql = "from AnswerRecord ar where ar.examId = ?1 and ar.score is NULL";
		List<AnswerRecord> answerRecords = this.executeQueryWithoutPaging(jpql, examId);
		return answerRecords;
	}

	@Override
	public List<AnswerRecord> list(String name, String examNo, String examId, String examPaperId, Integer questionType,
	        int page) {
		List<Object> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from AnswerRecord ar where 1=1");
		if (StringUtils.isNotBlank(name)) {
			jpql.append(" and ar.examinee.person.name like ?" + (++paramCount));
			params.add("%" + name + "%");
		}
		if (StringUtils.isNotBlank(examNo)) {
			jpql.append(" and ar.examinee.examNo like ?" + (++paramCount));
			params.add("%" + examNo + "%");
		}
		if (StringUtils.isNotBlank(examId)) {
			jpql.append(" and ar.examId = ?" + (++paramCount));
			params.add(examId);
		}
		if (StringUtils.isNotBlank(examPaperId)) {
			jpql.append(" and ar.examPaperId = ?" + (++paramCount));
			params.add(examPaperId);
		}
		if (questionType > 0) {
			jpql.append(" and ar.questionType = ?" + (++paramCount));
			params.add(questionType);
		}
		int count = this.executeCountQuery("select count(*) " + jpql, params.toArray());
		List<AnswerRecord> answerRecords = this.executeQueryWithPaging("select ar " + jpql.toString(), page,
		        DEFAULT_PAGESIZE, params.toArray());
		return PaginateSupportUtil.pagingList(answerRecords, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public AnswerRecord get(String examId, String examPaperId, String examineeId, String examQuestionId) {
		String jpql = "from AnswerRecord ar where ar.examId = ?1 and ar.examPaperId = ?2 and ar.examineeId = ?3 and ar.examQuestionId = ?4";
		List<AnswerRecord> answerRecords = this.executeQueryWithoutPaging(jpql, examId, examPaperId, examineeId,
		        examQuestionId);
		return null == answerRecords || answerRecords.isEmpty() ? null : answerRecords.get(0);
	}

	@Override
	public List<AnswerRecord> getByExam(String examId) {
		String jpql = "from AnswerRecord ar where ar.examId = ?1";
		List<AnswerRecord> answerRecords = this.executeQueryWithoutPaging(jpql, examId);
		return answerRecords;
	}

	@Override
	public List<AnswerRecord> getByExamAndExaminee(String examId, String examPaperId, String examieeId) {
		String jpql = "from AnswerRecord ar where ar.examId = ?1 and examPaperId = ?2 and examineeId = ?3";
		List<AnswerRecord> answerRecords = this.executeQueryWithoutPaging(jpql, examId, examPaperId, examieeId);
		return answerRecords;
	}

	@Override
	public List<AnswerRecord> getByExamPaper(String examId, String examPaperId) {
		String jpql = "from AnswerRecord ar where ar.examId = ?1 and examPaperId = ?2 order by ar.examQuestionId asc";
		List<AnswerRecord> answerRecords = this.executeQueryWithoutPaging(jpql, examId, examPaperId);
		return answerRecords;
	}

	@Override
	public List<AnswerRecord> getByQuestion(String examId, String questionId) {
		String jpql = "from AnswerRecord ar where ar.examId = ?1 and examQuestionId = ?2";
		List<AnswerRecord> answerRecords = this.executeQueryWithoutPaging(jpql, examId, questionId);
		return answerRecords;
	}

	@Override
	public List<AnswerRecord> getByExamAndExaminees(String examId, String examPaperId, List<Examinee> examinees) {
		List<String> params = Lists.newArrayList();
		params.add(examId);
		params.add(examPaperId);
		int paramCount = 2;
		StringBuilder jpql = new StringBuilder("from AnswerRecord ar where ar.examId = ?1 and examPaperId = ?2");
		for (Examinee examinee : examinees) {
			paramCount++;
			if (paramCount == 3) {
				jpql.append(" and (ar.examineeId = ?" + paramCount);
			} else {
				jpql.append(" or ar.examineeId = ?" + paramCount);
			}
			if (paramCount - 2 == examinees.size()) {
				jpql.append(")");
			}
			params.add(examinee.getId());
		}
		List<AnswerRecord> records = this.executeQueryWithoutPaging(jpql.toString(), params.toArray());
		return records;
	}

	@Override
	public boolean hasAnswerRecord(String examId, String examPaperId, String examineeId) {
		String jpql = "from AnswerRecord ar where ar.examId = ?1 and ar.examPaperId = ?2 and ar.examineeId = ?3";
		int count = this.executeCountQuery("select count(*) " + jpql, examId, examPaperId, examineeId);
		return count > 0;
	}

}
