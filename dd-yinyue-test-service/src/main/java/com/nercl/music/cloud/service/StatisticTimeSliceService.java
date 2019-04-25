package com.nercl.music.cloud.service;

import com.nercl.music.cloud.entity.ability.StatisticTimeSlice;

public interface StatisticTimeSliceService {

	boolean save(StatisticTimeSlice statisticTimeSlice);

	StatisticTimeSlice save(long startAt, long endAt);

	StatisticTimeSlice getLatest();

}
