package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.ExpertResult;

public interface ExpertResultService {

	ExpertResult get(String examId, String examPaperId, String examineeId, String examQuestionId, String expertId);

	boolean save(String examId, String examPaperId, String examineeId, String examQuestionId, String expertId,
	        int score, String comment);

	List<ExpertResult> list(String examId, String name, String examNo, String expertName, int page);

	Integer getScore(String examId, String examPaperId, String examineeId);

	List<ExpertResult> get(String examId, String examPaperId, String examQuestionId, String examineeId);

	List<Object[]> getAvgScore();

	List<ExpertResult> getBigDeviationResults();

	List<ExpertResult> getDiffResults();

}
