package com.nercl.music.cloud.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.nercl.music.cloud.dao.StatisticTimeSliceDao;
import com.nercl.music.cloud.entity.ability.StatisticTimeSlice;

@Service
@Transactional
public class StatisticTimeSliceServiceImpl implements StatisticTimeSliceService {

	@Autowired
	private StatisticTimeSliceDao statisticTimeSliceDao;

	@Override
	public boolean save(StatisticTimeSlice statisticTimeSlice) {
		statisticTimeSliceDao.save(statisticTimeSlice);
		return !Strings.isNullOrEmpty(statisticTimeSlice.getId());
	}

	@Override
	public StatisticTimeSlice save(long startAt, long endAt) {
		StatisticTimeSlice sts = new StatisticTimeSlice();
		sts.setStartAt(startAt);
		sts.setEndAt(endAt);
		statisticTimeSliceDao.save(sts);
		return !Strings.isNullOrEmpty(sts.getId()) ? sts : null;
	}

	@Override
	public StatisticTimeSlice getLatest() {
		return statisticTimeSliceDao.getLatest();
	}

}
