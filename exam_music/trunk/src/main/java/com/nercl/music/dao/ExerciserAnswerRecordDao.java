package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.ExerciserAnswerRecord;

public interface ExerciserAnswerRecordDao extends BaseDao<ExerciserAnswerRecord, String> {

	List<ExerciserAnswerRecord> get(String examId, String examPaperId, String exerciserId, String examQuestionId);

}
