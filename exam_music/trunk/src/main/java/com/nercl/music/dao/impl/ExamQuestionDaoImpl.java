package com.nercl.music.dao.impl;

import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.nercl.music.constant.CList;
import com.nercl.music.dao.ExamQuestionDao;
import com.nercl.music.entity.CheckRecord;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ExamQuestionDaoImpl extends AbstractBaseDaoImpl<ExamQuestion, String> implements ExamQuestionDao {

	@Override
	public List<ExamQuestion> getByExamPaper(String examPaperId) {
		String jpql = "select eq from ExamPaperQuestion epq inner join epq.examQuestion eq where epq.examPaperId = ?1";
		List<ExamQuestion> examQuestions = this.executeQueryWithoutPaging(jpql, examPaperId);
		return examQuestions;
	}

	@Override
	public List<ExamQuestion> getLookSingQuestions(String examId, String examPaperId, int size) {
		String jpql = "from ExamPaperQuestion epq inner join epq.examQuestion eq where epq.examPaperId = ?1 and eq.subjectType = ?2";
		int count = this.executeCountQuery("select count(*) " + jpql, examPaperId, CList.Api.SubjectType.LOOK_SING);
		// int random = RandomUtils.nextInt(0, count) + 1;
		// ExamQuestion question = this.findTop(random, "select eq " + jpql,
		// examPaperId, CList.Api.SubjectType.LOOK_SING);
		// if (null == question) {
		// return null;
		// }
		// return Lists.newArrayList(questions);
		List<ExamQuestion> questions = this.findRandom(size, count, "select eq " + jpql, examPaperId,
		        CList.Api.SubjectType.LOOK_SING);
		return questions;
	}

	@Override
	public List<ExamQuestion> getTingYinQuestions(String examId, String examPaperId) {
		String jpql = "select eq from ExamPaperQuestion epq inner join epq.examQuestion eq where epq.examPaperId = ?1";
		List<ExamQuestion> examQuestions = this.executeQueryWithoutPaging(jpql, examPaperId);
		return examQuestions;
	}

	@Override
	public List<ExamQuestion> getYueLiQuestions(String examId, String examPaperId) {
		String jpql = "select eq from ExamPaperQuestion epq inner join epq.examQuestion eq where epq.examPaperId = ?1";
		List<ExamQuestion> examQuestions = this.executeQueryWithoutPaging(jpql, examPaperId);
		return examQuestions;
	}

	@Override
	public List<ExamQuestion> getDefaultLookSingQuestions() {
		String jpql = "from ExamQuestion eq where eq.subjectType = ?1";
		int count = this.executeCountQuery("select count(*) " + jpql, CList.Api.SubjectType.LOOK_SING);
		int random = RandomUtils.nextInt(0, count) + 1;
		ExamQuestion question = this.findTop(random, jpql, CList.Api.SubjectType.LOOK_SING);
		return Lists.newArrayList(question);
	}

	@Override
	public List<ExamQuestion> getDefaultYueLiQuestions() {
		List<ExamQuestion> questions = Lists.newArrayList();
		questions.addAll(getTenSingleSelectedQuestions());
		questions.addAll(getTenMultiSelectedQuestions());
		questions.addAll(getTenShortAnswerQuestions());
		return questions;
	}

	private List<ExamQuestion> getTenSingleSelectedQuestions() {
		String jpql = "from ExamQuestion eq where eq.subjectType = ?1 and eq.questionType = ?2 order by eq.commitedTime desc";
		List<ExamQuestion> questions = this.executeQueryWithPaging(jpql, 1, 10, CList.Api.SubjectType.YUE_LI,
		        CList.Api.QuestionType.SINGLE_SELECT);
		return questions;
	}

	private List<ExamQuestion> getTenMultiSelectedQuestions() {
		String jpql = "from ExamQuestion eq where eq.subjectType = ?1 and eq.questionType = ?2 order by eq.commitedTime desc";
		List<ExamQuestion> questions = this.executeQueryWithPaging(jpql, 1, 10, CList.Api.SubjectType.YUE_LI,
		        CList.Api.QuestionType.MULTI_SELECT);
		return questions;
	}

	private List<ExamQuestion> getTenShortAnswerQuestions() {
		String jpql = "from ExamQuestion eq where eq.subjectType = ?1 and eq.questionType = ?2 order by eq.commitedTime desc";
		List<ExamQuestion> questions = this.executeQueryWithPaging(jpql, 1, 10, CList.Api.SubjectType.YUE_LI,
		        CList.Api.QuestionType.SHORT_ANSWER);
		return questions;
	}

	@Override
	public List<ExamQuestion> getDefaultTingYinQuestions() {
		String jpql = "from ExamQuestion eq where eq.subjectType = ?1 order by eq.commitedTime desc";
		List<ExamQuestion> questions = this.executeQueryWithPaging(jpql, 1, 5, CList.Api.SubjectType.TING_YIN);
		return questions;
	}

	@Override
	public List<ExamQuestion> list() {
		String jpql = "from ExamQuestion";
		List<ExamQuestion> questions = this.executeQueryWithoutPaging(jpql);
		return questions;
	}

	@Override
	public List<ExamQuestion> list(Integer type, String title, Float difficulty, int page, String status) {
		List<Object> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from ExamQuestion eq where 1=1");
		if (type != null) {
			jpql.append(" and eq.questionType = ?" + (++paramCount));
			params.add(type);
		}
		if (StringUtils.isNotBlank(title)) {
			jpql.append(" and eq.title like ?" + (++paramCount));
			params.add("%" + title + "%");
		}
		if (difficulty != null) {
			jpql.append(" and eq.difficulty = ?" + (++paramCount));
			params.add(difficulty);
		}
		if (status != null) {
			if (status.equals("PASSED")) {
				paramCount++;
				jpql.append(" and eq.checkStatus = ?" + paramCount);
				params.add(CheckRecord.Status.PASSED);
			}
			if (status.equals("UN_PASSED")) {
				paramCount++;
				jpql.append(" and eq.checkStatus = ?" + paramCount);
				params.add(CheckRecord.Status.UN_PASSED);
			}
			if (status.equals("FOR_CHECKED")) {
				paramCount++;
				jpql.append(" and eq.checkStatus = ?" + paramCount);
				params.add(CheckRecord.Status.FOR_CHECKED);
			}
		}
		jpql.append(" order by eq.commitedTime desc");
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
		List<ExamQuestion> examQuestions = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE,
		        params.toArray());
		return PaginateSupportUtil.pagingList(examQuestions, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<ExamQuestion> list(Integer type, int page) {
		String jpql = "from ExamQuestion eq where eq.questionType = ?1 and eq.checkStatus =?2";
		int count = this.executeCountQuery("select count(*) " + jpql, type, CheckRecord.Status.PASSED);
		List<ExamQuestion> examQuestions = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE, type,
		        CheckRecord.Status.PASSED);
		return PaginateSupportUtil.pagingList(examQuestions, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<ExamQuestion> random(Integer type, int size) {
		String jpql = "from ExamQuestion cn where cn.questionType = ?1";
		int count = this.executeCountQuery("select count(*) " + jpql, type);
		return this.findRandom(size, count, jpql, type);
	}

	@Override
	public List<ExamQuestion> random(Integer type, Integer subjectType, int size) {
		String jpql = "from ExamQuestion cn where cn.questionType = ?1 and cn.subjectType = ?2";
		int count = this.executeCountQuery("select count(*) " + jpql, type, subjectType);
		return this.findRandom(size, count, jpql, type, subjectType);
	}

	@Override
	public List<ExamQuestion> query(Integer type, String word, int page) {
		String jpql = "from ExamQuestion eq where eq.questionType = ?1 and eq.title like ?2";
		int count = this.executeCountQuery("select count(*) " + jpql, type, "%" + word + "%");
		List<ExamQuestion> examQuestions = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE, type,
		        "%" + word + "%");
		return PaginateSupportUtil.pagingList(examQuestions, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<ExamQuestion> getTrialQuestions(Integer questionType) {
		String jpql = "from ExamQuestion cn where cn.questionType = ?1";
		int count = this.executeCountQuery("select count(*) " + jpql, questionType);
		return this.findRandom(2, count, jpql, questionType);
	}

}
