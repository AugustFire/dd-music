package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.ActivityResourceRelation;

public interface ActivityResourceRelationService {

	/**
	 * 根据活动id查询资源列表
	 */
	List<ActivityResourceRelation> getResoures(String activityId);

	/**
	 * 根据活动id集合查询资源列表
	 */
	List<ActivityResourceRelation> getResoures(List<String> activityIds);

}
