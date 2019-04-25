package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.question.MachineResult;

public interface MachineResultService {

	boolean save(String answerRecordId, String topicId, String exerciserId, String examQuestionId, Integer score);

	MachineResult get(String answerRecordId, String exerciserId, String examQuestionId);

	MachineResult get(String answerRecordId);

	List<Object[]> getMachineAvgScore();
	
	void update(MachineResult machineResult);

}
