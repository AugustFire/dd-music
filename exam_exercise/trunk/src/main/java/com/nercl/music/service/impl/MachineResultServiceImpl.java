package com.nercl.music.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.MachineResultDao;
import com.nercl.music.entity.question.MachineResult;
import com.nercl.music.service.MachineResultService;

@Service
@Transactional
public class MachineResultServiceImpl implements MachineResultService {

	@Autowired
	private MachineResultDao machineResultDao;

	@Override
	public boolean save(String answerRecordId, String topicId, String exerciserId, String examQuestionId,
	        Integer score) {
		MachineResult machineResult = new MachineResult();
		machineResult.setAnswerRecordId(answerRecordId);
		machineResult.setExamQuestionId(examQuestionId);
		machineResult.setScore(score);
		this.machineResultDao.save(machineResult);
		return true;
	}

	@Override
	public MachineResult get(String answerRecordId, String exerciserId, String examQuestionId) {
		return machineResultDao.get(answerRecordId, exerciserId, examQuestionId);
	}

	@Override
	public List<Object[]> getMachineAvgScore() {
		return machineResultDao.getMachineAvgScore();
	}

	@Override
	public MachineResult get(String answerRecordId) {
		return machineResultDao.get(answerRecordId);
	}

	@Override
	public void update(MachineResult machineResult) {
		machineResultDao.update(machineResult);
	}

}
