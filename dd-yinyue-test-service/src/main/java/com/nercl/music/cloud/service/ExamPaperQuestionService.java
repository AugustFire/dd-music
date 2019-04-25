package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.ExamPaperQuestion;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;

public interface ExamPaperQuestionService {

	void setQuestionsToExamPaper(String examPaperId, List<String> qids, Integer score);

	boolean removeQuestion(String examPaperId, String examQuestionId);

	boolean removeQuestions(String examPaperId, QuestionType questionType);

	ExamPaperQuestion getByExamPaperAndQuestion(String examPaperId, String questionId);

	List<ExamPaperQuestion> getByExamPaper(String examPaperId);

	boolean setScore(String examPaperId, String questionId, Integer score);

	String auto(String examId, String grade, String title, Integer score, Integer resolvedTime, Integer singleNum,
			Integer singleScore, Integer multiNum, Integer multiScore, Integer shortNum, Integer shortScore,
			Float diffculty, Map<String, Integer> knowledges, List<QuestionType> types);

	String auto(String examId, String grade, String title, Integer score, Integer resolvedTime, Integer singNum,
			Integer singScore, Integer behindBackSingNum, Integer behindBackSingScore, Integer behindBackPerformanceNum,
			Integer behindBackPerformanceScore, Integer performanceNum, Integer performanceScore,
			Integer sightSingingNum, Integer sightSingingScore, Float diffculty, Map<String, Integer> knowledges,
			List<QuestionType> types);

	Map<String, Integer> getScore(String examPaperId);

	Integer getScore(String examPaperId, String examQuestionId);

	List<Question> getQuestionByExamPaper(String pid);

	void deleteByExamPaper(String examPaperId);

}
