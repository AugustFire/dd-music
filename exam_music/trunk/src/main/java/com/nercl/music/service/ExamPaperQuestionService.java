package com.nercl.music.service;

import java.util.Map;

import com.nercl.music.entity.ExamPaperQuestion;

public interface ExamPaperQuestionService {

	void setQuestionsToExamPaper(String examPaperId, String[] examQuestionIds, Integer score);

	void removeQuestions(String examPaperId, String examQuestionId);

	ExamPaperQuestion getByExamPaperAndQuestion(String examId, String questionId);

	boolean setScore(String examPaperId, String questionId, Integer score);

	boolean auto(String title, Integer score, Integer resolvedTime, Integer subjectType, Integer singleNum,
	        Integer singleScore, Integer multiNum, Integer multiScore, Integer shortNum, Integer shortScore,
	        Integer lookSingNum, Integer lookSingScore);

	boolean auto(String examId, String yueliTitle, Integer yueliScore, Integer yueliResolvedTime,
	        Integer yueliSingleTotalScore, Integer yueliSingleNum, Integer yueliSingleScore,
	        Integer yueliMultiTotalScore, Integer yueliMultiNum, Integer yueliMultiScore, Integer yueliShortTotalScore,
	        Integer yueliShortNum, Integer yueliShortScore, String tingyinTitle, Integer tingyinResolvedTime,
	        Integer tingyinShortTotalScore, Integer tingyinShortNum, Integer tingyinScore, String lookSingTitle,
	        Integer lookSingResolvedTime, Integer lookSingTotalScore, Integer lookSingNum, Integer lookSingScore);

	Map<String, Integer> getScore(String examPaperId);

	Integer getScore(String examPaperId, String examQuestionId);

}
