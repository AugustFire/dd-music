package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.ActivityResourceRelation;

public interface ActivityResourceRelationDao extends BaseDao<ActivityResourceRelation, String> {

	List<ActivityResourceRelation> getResoures(String activityId);
	
	List<ActivityResourceRelation> getResoures(List<String> activityIds);
}
