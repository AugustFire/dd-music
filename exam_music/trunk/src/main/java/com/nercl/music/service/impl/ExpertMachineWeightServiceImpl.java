package com.nercl.music.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ExpertMachineWeightDao;
import com.nercl.music.entity.ExpertMachineWeight;
import com.nercl.music.service.ExpertMachineWeightService;

@Service
@Transactional
public class ExpertMachineWeightServiceImpl implements ExpertMachineWeightService {

	@Autowired
	private ExpertMachineWeightDao expertMachineWeightDao;

	@Override
	public boolean save(String examId, String examPaperId, Integer expertWeight, Integer machineWeight) {
		ExpertMachineWeight weight = this.get(examId);
		if (null != weight) {
			weight.setExpertWeight(expertWeight);
			weight.setMachineWeight(machineWeight);
			this.expertMachineWeightDao.update(weight);
			return true;
		}
		weight = new ExpertMachineWeight();
		weight.setExamId(examId);
		weight.setExamPaperId(examPaperId);
		weight.setExpertWeight(expertWeight);
		weight.setMachineWeight(machineWeight);
		this.expertMachineWeightDao.save(weight);
		return true;
	}

	@Override
	public ExpertMachineWeight get(String examId) {
		return this.expertMachineWeightDao.get(examId);
	}

}
