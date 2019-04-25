package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.WorkDuration;

public interface WorkDurationService {
	
	void save(WorkDuration wd);

	List<WorkDuration> list(int year);

}
