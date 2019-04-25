package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.MachineResult;

public interface MachineResultDao extends BaseDao<MachineResult, String> {

	MachineResult get(String examId, String examPaperId, String examineeId, String examQuestionId);
	
	List<Object[]> getMachineAvgScore();
}
