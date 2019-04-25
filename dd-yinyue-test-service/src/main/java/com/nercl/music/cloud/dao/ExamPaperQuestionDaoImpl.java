package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.ExamPaperQuestion;

@Repository
public class ExamPaperQuestionDaoImpl extends AbstractBaseDaoImpl<ExamPaperQuestion, String>
		implements ExamPaperQuestionDao {

	@Override
	public ExamPaperQuestion getByExamPaperAndQuestion(String examPaperId, String questionId) {
		String jpql = "from ExamPaperQuestion epq where epq.examPaperId = ?1 and epq.questionId = ?2";
		List<ExamPaperQuestion> examPaperQuestions = this.executeQueryWithoutPaging(jpql, examPaperId, questionId);
		return examPaperQuestions == null || examPaperQuestions.isEmpty() ? null : examPaperQuestions.get(0);
	}

	@Override
	public List<ExamPaperQuestion> getByExamPaper(String examPaperId) {
		String jpql = "from ExamPaperQuestion epq where epq.examPaperId = ?1";
		List<ExamPaperQuestion> examPaperQuestions = this.executeQueryWithoutPaging(jpql, examPaperId);
		return examPaperQuestions;
	}

}
