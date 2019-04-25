package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.base.BasicFacility;

public interface BasicFacilityService {

	boolean save(String title, Integer num, Boolean isInstrument, String schoolId);

	List<BasicFacility> get(String title, String schoolId);

	List<BasicFacility> get(String schoolId);

	boolean edit(String id, String title, Integer num, Boolean isInstrument);

	boolean delete(String id);

}
