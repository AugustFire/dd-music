package com.nercl.music.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.entity.AnswerRecord;

public interface AnswerRecordService {

	AnswerRecord get(String id);

	AnswerRecord get(String examId, String examPaperId, String examineeId, String examQuestionId);

	List<AnswerRecord> list(String examineeId, int page);

	List<AnswerRecord> list(String examId, String examPaperId, Integer questionType);

	List<Map<String, Object>> list(String name, String examNo, String examId, String examPaperId, Integer questionType,
	        int page);

	boolean save(String examId, String examPaperId, String examineeId, String examQuestionId, String content,
	        String resPath);

	boolean autoSetScore(String examId);

	String getLookSingData(String examId, String examPaperId, String examineeId, String questionId);

	String getCurveData(String examId, String examPaperId, String examineeId, String questionId);

	void getLookSingParser(String examId, String examPaperId, String examineeId, String questionId);

	List<AnswerRecord> getByExam(String examId);

	List<AnswerRecord> getByExamAndExaminee(String examId, String examPaperId, String examineeId);

	List<AnswerRecord> getByExamPaper(String examId, String examPaperId);

	List<AnswerRecord> getByQuestion(String examId, String questionId);

	List<AnswerRecord> getByExamAndExpertGroup(String examId, String examPaperId, String expertId);

	Map<String, String> getRelations(Integer questionType);

	boolean hasAnswerRecord(String examId, String examPaperId, String examineeId);

}
