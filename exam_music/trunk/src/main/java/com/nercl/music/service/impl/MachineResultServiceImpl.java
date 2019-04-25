package com.nercl.music.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.MachineResultDao;
import com.nercl.music.entity.MachineResult;
import com.nercl.music.service.MachineResultService;

@Service
@Transactional
public class MachineResultServiceImpl implements MachineResultService {

	@Autowired
	private MachineResultDao machineResultDao;

	@Override
	public boolean save(String examId, String examPaperId, String examineeId, String examQuestionId, Integer score) {
		MachineResult machineResult = this.get(examId, examPaperId, examineeId, examQuestionId);
		if (null != machineResult) {
			return false;
		}
		machineResult = new MachineResult();
		machineResult.setExamId(examId);
		machineResult.setExamPaperId(examPaperId);
		machineResult.setExamineeId(examineeId);
		machineResult.setExamQuestionId(examQuestionId);
		machineResult.setScore(score);
		this.machineResultDao.save(machineResult);
		return true;
	}

	@Override
	public MachineResult get(String examId, String examPaperId, String examineeId, String examQuestionId) {
		return this.machineResultDao.get(examId, examPaperId, examineeId, examQuestionId);
	}

	@Override
	public List<Object[]> getMachineAvgScore() {
		List<Object[]> list = machineResultDao.getMachineAvgScore();
		return list;
	}

}
