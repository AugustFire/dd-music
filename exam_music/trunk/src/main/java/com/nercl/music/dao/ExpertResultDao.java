package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.ExpertResult;

public interface ExpertResultDao extends BaseDao<ExpertResult, String> {

	ExpertResult get(String examId, String examPaperId, String examineeId, String examQuestionId, String expertId);

	List<ExpertResult> get(String examId, String examPaperId, String examineeId);

	List<ExpertResult> list(String examId, String name, String examNo, String expertName, int page);

	List<Object[]> getAvgScore();

	List<ExpertResult> getBigDeviationResults(List<Object[]> avgScores);

	List<ExpertResult> get(String examId, String examPaperId, String examQuestionId, String examineeId);

}
