package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.WorkDuration;

public interface WorkDurationDao extends BaseDao<WorkDuration, String> {

	List<WorkDuration> list(int year);

}
