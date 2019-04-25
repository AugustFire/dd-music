package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.ExamQuestion;

public interface ExamQuestionDao extends BaseDao<ExamQuestion, String> {

	List<ExamQuestion> getByExamPaper(String examPaperId);

	List<ExamQuestion> getLookSingQuestions(String examId, String examPaperId, int size);

	List<ExamQuestion> getTingYinQuestions(String examId, String examPaperId);

	List<ExamQuestion> getYueLiQuestions(String examId, String examPaperId);

	List<ExamQuestion> getDefaultLookSingQuestions();

	List<ExamQuestion> getDefaultYueLiQuestions();

	List<ExamQuestion> getDefaultTingYinQuestions();

	List<ExamQuestion> list();

	List<ExamQuestion> list(Integer type, String title, Float difficulty, int page, String status);

	List<ExamQuestion> list(Integer type, int page);

	List<ExamQuestion> random(Integer type, int size);

	List<ExamQuestion> random(Integer type, Integer subjectType, int size);

	List<ExamQuestion> query(Integer type, String word, int page);

	List<ExamQuestion> getTrialQuestions(Integer questionType);

}
