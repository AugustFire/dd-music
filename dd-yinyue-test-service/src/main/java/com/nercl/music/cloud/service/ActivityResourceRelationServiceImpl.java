package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.ActivityResourceRelationDao;
import com.nercl.music.cloud.entity.ActivityResourceRelation;

@Service
@Transactional
public class ActivityResourceRelationServiceImpl implements ActivityResourceRelationService {

	@Autowired
	private ActivityResourceRelationDao activityResourceRelationDao;
	
	@Override
	public List<ActivityResourceRelation> getResoures(String activityId) {
		return activityResourceRelationDao.getResoures(activityId);
	}

	@Override
	public List<ActivityResourceRelation> getResoures(List<String> activityIds) {
		return activityResourceRelationDao.getResoures(activityIds);
	}


}
