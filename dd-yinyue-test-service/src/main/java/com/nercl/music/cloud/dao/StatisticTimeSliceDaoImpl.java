package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.ability.StatisticTimeSlice;

@Repository
public class StatisticTimeSliceDaoImpl extends AbstractBaseDaoImpl<StatisticTimeSlice, String>
		implements StatisticTimeSliceDao {

	@Override
	public StatisticTimeSlice getLatest() {
		String jpql = "from StatisticTimeSlice sts order by sts.endAt desc";
		List<StatisticTimeSlice> slices = this.executeQueryWithoutPaging(jpql);
		return null == slices || slices.isEmpty() ? null : slices.get(0);
	}

}
