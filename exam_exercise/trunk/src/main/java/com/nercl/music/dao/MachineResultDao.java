package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.question.MachineResult;

public interface MachineResultDao extends BaseDao<MachineResult, String> {

	MachineResult get(String answerRecordId, String exerciserId, String examQuestionId);

	List<Object[]> getMachineAvgScore();

	MachineResult get(String answerRecordId);

	List<MachineResult> list(String examQuestionId);
}
