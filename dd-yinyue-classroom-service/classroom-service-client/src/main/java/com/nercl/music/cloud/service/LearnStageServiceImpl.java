package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.LearnStageDao;
import com.nercl.music.cloud.entity.base.LearnStage;

@Service
@Transactional
public class LearnStageServiceImpl implements LearnStageService {

	@Autowired
	private LearnStageDao learnStageDao;

	@Override
	public List<LearnStage> getAllLearnStages() {
		return learnStageDao.findAll(LearnStage.class);
	}

	@Override
	public List<LearnStage> findByConditions(LearnStage condition) throws Exception {
		return learnStageDao.findByConditions(condition);
	}

	@Override
	public LearnStage findById(String learnStageId) {
		return learnStageDao.findByID(learnStageId);
	}

	@Override
	public LearnStage findByCode(String learnStageCode) {
		return learnStageDao.findByCode(learnStageCode);
	}

	@Override
	public void update(LearnStage learnStage) {
		learnStageDao.update(learnStage);
	}

	@Override
	public void deleteById(String learnStageId) {
		learnStageDao.deleteById(learnStageId);
	}

	@Override
	public void delete(LearnStage learnStage) {
		learnStageDao.delete(learnStage);
	}

	@Override
	public void save(LearnStage learnStage) {
		learnStageDao.save(learnStage);
	}
}
