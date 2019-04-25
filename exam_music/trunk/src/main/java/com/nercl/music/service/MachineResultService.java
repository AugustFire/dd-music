package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.MachineResult;

public interface MachineResultService {

	boolean save(String examId, String examPaperId, String examineeId, String examQuestionId, Integer score);

	MachineResult get(String examId, String examPaperId, String examineeId, String examQuestionId);
	
	List<Object[]> getMachineAvgScore();

}
