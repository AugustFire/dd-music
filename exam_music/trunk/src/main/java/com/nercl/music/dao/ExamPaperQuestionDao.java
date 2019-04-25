package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.ExamPaperQuestion;

public interface ExamPaperQuestionDao extends BaseDao<ExamPaperQuestion, String> {

	void removeQuestion(String examPaperId, String examQuestionId);

	ExamPaperQuestion getByExamPaperAndQuestion(String examPaperId, String questionId);

	List<ExamPaperQuestion> getByExamPaper(String examPaperId);

}
