package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.AnswerRecordMaluation;
import com.nercl.music.cloud.entity.Maluation;

public interface AnswerRecordMaluationService {

	List<AnswerRecordMaluation> getByRecordAndOption(String rid, String option);

	boolean save(String json);

	boolean save(String recordId, String option, Integer score);

	List<AnswerRecordMaluation> getByRecord(String rid);

	Integer getScoreAtOneRecord(String rid);

	Maluation.Level getLevel(String rid);

}
