package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.ExerciserAnswerRecord;

public interface ExerciserAnswerRecordService {

	ExerciserAnswerRecord get(String id);

	List<ExerciserAnswerRecord> get(String examId, String examPaperId, String exerciserId, String examQuestionId);

	ExerciserAnswerRecord save(String examId, String examPaperId, String exerciserId, String examQuestionId, String content,
	        String resPath);

}
