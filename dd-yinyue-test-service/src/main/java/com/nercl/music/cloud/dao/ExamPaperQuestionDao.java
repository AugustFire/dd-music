package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.ExamPaperQuestion;

public interface ExamPaperQuestionDao extends BaseDao<ExamPaperQuestion, String> {

	ExamPaperQuestion getByExamPaperAndQuestion(String examPaperId, String questionId);

	List<ExamPaperQuestion> getByExamPaper(String examPaperId);
}
