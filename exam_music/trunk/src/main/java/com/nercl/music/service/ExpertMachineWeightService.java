package com.nercl.music.service;

import com.nercl.music.entity.ExpertMachineWeight;

public interface ExpertMachineWeightService {

	boolean save(String examId, String examPaperId, Integer expertValue, Integer machineValue);

	ExpertMachineWeight get(String examId);

}
