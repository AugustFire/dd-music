package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.AnswerRecordMaluation;

public interface AnswerRecordMaluationDao extends BaseDao<AnswerRecordMaluation, String> {

	List<AnswerRecordMaluation> getByRecord(String rid);

	List<AnswerRecordMaluation> getByRecordAndOption(String rid, String option);

}
