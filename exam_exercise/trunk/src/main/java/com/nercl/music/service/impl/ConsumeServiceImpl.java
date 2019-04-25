package com.nercl.music.service.impl;

import java.util.List;


import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ConsumeDao;
import com.nercl.music.entity.question.ConsumeRecord;
import com.nercl.music.entity.user.Exerciser;
import com.nercl.music.service.ConsumeService;
import com.nercl.music.service.ExerciserService;

@Service
@Transactional
public class ConsumeServiceImpl implements ConsumeService {

	@Autowired
	private ConsumeDao consumeDao;

	@Autowired
	private ExerciserService exerciserService;

	@Override
	public ConsumeRecord get(String consumeRecordId) {
		return consumeDao.findByID(consumeRecordId);
	}

	@Override
	public List<ConsumeRecord> list(String exerciserId, int page) {
		return consumeDao.list(exerciserId, page);
	}

	@Override
	public List<ConsumeRecord> list(String exerciserId) {
		return consumeDao.list(exerciserId);
	}

	@Override
	public List<ConsumeRecord> query(String login, int page) {
		Exerciser exerciser = exerciserService.getByLogin(login);
		if (null == exerciser) {
			return null;
		}
		return consumeDao.list(exerciser.getPersonId(), page);
	}

	@Override
	public ConsumeRecord start(String exerciserId) {
		ConsumeRecord consumeRecord = new ConsumeRecord();
		consumeRecord.setExerciserId(exerciserId);
		consumeRecord.setStartAt(System.currentTimeMillis());
		consumeRecord.setEnded(false);
		consumeDao.save(consumeRecord);
		return Strings.isNullOrEmpty(consumeRecord.getId()) ? null : consumeRecord;
	}

	@Override
	public boolean end(String exerciserId, String consumeRecordId) {
		ConsumeRecord consumeRecord = this.get(consumeRecordId);
		if (null != consumeRecord) {
			consumeRecord.setEndtAt(System.currentTimeMillis());
			consumeRecord.setEnded(true);
			consumeDao.save(consumeRecord);
			return true;
		}
		return false;
	}

	@Override
	public boolean endConsume(String exerciserId) {
		// TODO Auto-generated method stub
		return false;
	}

}
