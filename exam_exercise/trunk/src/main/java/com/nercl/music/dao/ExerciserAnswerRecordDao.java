package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.question.ExerciserAnswerRecord;

public interface ExerciserAnswerRecordDao extends BaseDao<ExerciserAnswerRecord, String> {

	List<ExerciserAnswerRecord> get(String personId, String examQuestionId);

	List<ExerciserAnswerRecord> list(String name, String topicId, Integer questionType, int page);

	List<ExerciserAnswerRecord> listByPerson(String personId, String topicId, Integer questionType, int page);

	ExerciserAnswerRecord get(String topicId, String personId, String examQuestionId);

	boolean hasAnswers(String personId, String topicId, String examQuestionId);

	List<ExerciserAnswerRecord> get(String examQuestionId);
}
