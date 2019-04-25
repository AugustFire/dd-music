package com.nercl.music.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.entity.Answer;
import com.nercl.music.entity.ExamPaperQuestion;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.entity.user.Login;

public interface ExamQuestionService {

	List<ExamQuestion> getDefaultLookSingQuestions();

	List<ExamQuestion> getDefaultYueLiQuestions();

	List<ExamQuestion> getDefaultTingYinQuestions();

	List<ExamQuestion> getLookSingQuestions(String examId, String examPaperId, int size);

	Map<Integer, List<ExamQuestion>> getByExamPaper(String examPaperId);

	List<ExamQuestion> getTingYinQuestions(String examId, String examPaperId);

	List<ExamQuestion> getYueLiQuestions(String examId, String examPaperId);

	List<ExamQuestion> get(String examId, String examPaperId);

	ExamQuestion get(String id);

	boolean save(ExamQuestion examQuestion);

	boolean saveAnswer(Answer answer);

	List<Map<String, Object>> json();

	List<ExamQuestion> list(Integer type, String title, Float difficulty, int page, String status);

	void setCheckStatusDefault();

	List<ExamQuestion> list(Integer type, int page);

	List<ExamQuestion> random(Integer type, int size);

	List<ExamQuestion> random(Integer type, Integer subjectType, int size);

	List<ExamQuestion> query(Integer type, String word, int page);

	boolean pass(String qid, Login login);

	boolean unpass(String qid, Login login, String reason);

	List<ExamPaperQuestion> getExamPaperQuestion(String examId, String examPaperId);

	Integer getQuestionScore(String examPaperId, String questionId);

	List<ExamQuestion> getTrialQuestions(Integer questionType);

}
