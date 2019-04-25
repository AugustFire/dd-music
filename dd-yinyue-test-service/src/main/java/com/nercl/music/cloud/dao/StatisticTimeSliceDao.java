package com.nercl.music.cloud.dao;

import com.nercl.music.cloud.entity.ability.StatisticTimeSlice;

public interface StatisticTimeSliceDao extends BaseDao<StatisticTimeSlice, String> {

	StatisticTimeSlice getLatest();

}
