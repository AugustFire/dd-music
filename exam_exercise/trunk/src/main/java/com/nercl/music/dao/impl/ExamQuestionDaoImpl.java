package com.nercl.music.dao.impl;

import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.nercl.music.dao.ExamQuestionDao;
import com.nercl.music.entity.question.ExamQuestion;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ExamQuestionDaoImpl extends AbstractBaseDaoImpl<ExamQuestion, String> implements ExamQuestionDao {

	static final int DEFAULT_PAGESIZE = 20;

	@Override
	public List<ExamQuestion> getLookSingQuestions(int year) {
		String jpql = "from ExamQuestion eq where eq.year = ?1 and eq.subjectType = 2";
		List<ExamQuestion> questions = this.executeQueryWithoutPaging(jpql, year);
		return questions;
	}

	@Override
	public List<ExamQuestion> getBytopic(String topicId) {
		String jpql = "select tq.examQuestion from TopicQuestion tq where tq.topicId = ?1";
		List<ExamQuestion> questions = this.executeQueryWithoutPaging(jpql, topicId);
		return questions;
	}

	@Override
	public List<ExamQuestion> listLookSingQuestions() {
		String jpql = "from ExamQuestion eq where eq.subjectType = 2";
		List<ExamQuestion> questions = this.executeQueryWithoutPaging(jpql);
		return questions;
	}

	@Override
	public List<ExamQuestion> getBySubjectType(Integer subjectType, int page) {
		String jpql = "from ExamQuestion eq where eq.subjectType = ?1 order by eq.questionType asc , eq.commitedTime desc";
		int count = this.executeCountQuery("select count(*) " + jpql, subjectType);
		List<ExamQuestion> questions = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, subjectType);
		return PaginateSupportUtil.pagingList(questions, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<ExamQuestion> list(Integer type, String title, Float difficulty, int page) {
		List<Object> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from ExamQuestion eq where 1=1");
		if (type != null) {
			jpql.append(" and eq.questionType = ?").append(++paramCount);
			params.add(type);
		}
		if (StringUtils.isNotBlank(title)) {
			jpql.append(" and eq.title like ?").append(++paramCount);
			params.add("%" + title + "%");
		}
		if (difficulty != null) {
			jpql.append(" and eq.difficulty = ?").append(++paramCount);
			params.add(difficulty);
		}
		jpql.append(" order by eq.commitedTime desc");
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
		List<ExamQuestion> examQuestions = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE,
				params.toArray());
		return PaginateSupportUtil.pagingList(examQuestions, page, DEFAULT_PAGESIZE, count);
	}
}
