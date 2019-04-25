package com.nercl.music.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.WorkDurationDao;
import com.nercl.music.entity.WorkDuration;
import com.nercl.music.service.WorkDurationService;

@Service
@Transactional
public class WorkDurationServiceImpl implements WorkDurationService {
	
	@Autowired
	private WorkDurationDao workDurationDao;

	@Override
	public void save(WorkDuration wd) {
		this.workDurationDao.save(wd);
	}

	@Override
	public List<WorkDuration> list(int year) {
		return this.workDurationDao.list(year);
	}

}
