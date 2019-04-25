package com.nercl.music.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.entity.question.ExerciserAnswerRecord;

public interface ExerciserAnswerRecordService {

	ExerciserAnswerRecord get(String id);

	ExerciserAnswerRecord save(String topicId, String personId, String examQuestionId, String content, String resPath);

	ExerciserAnswerRecord save(String topicId, String personId, String examQuestionId, String content, String resPath,
	        int accuracy);

	List<ExerciserAnswerRecord> get(String topicId, String personId, String examQuestionId);

	List<Map<String, Object>> list(String personId, String name, String topicId, Integer questionType, int page);

	String getLookSingData(String recordId);

	String getCurveData(String recordId);

	boolean hasAnswers(String personId, String topicId, String examQuestionId);

	void update(ExerciserAnswerRecord record);
}
