package com.nercl.music.cloud.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.HonourDao;
import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.AwardLevel;
import com.nercl.music.cloud.entity.Honour;
import com.nercl.music.util.page.PaginateSupportArray;

@Service
@Transactional
public class HonourServiceImpl implements HonourService {

	@Autowired
	private HonourDao honourDao;

	@Override
	public PaginateSupportArray<Honour> findAllHonours(String sid, ActivityType activityType, AwardLevel awardLevel,
			int page, int pageSize) {
		return honourDao.findAllHonours(sid, activityType, awardLevel, page, pageSize);
	}

	@Override
	public String save(Honour honour) {
		honourDao.save(honour);
		return honour.getId();
	}

	@Override
	public void deleteById(String id) {
		honourDao.deleteById(id);
	}

	@Override
	public Honour findById(String id) {
		return honourDao.findByID(id);
	}

	@Override
	public void update(Honour honour) {
		honourDao.update(honour);
	}

}
