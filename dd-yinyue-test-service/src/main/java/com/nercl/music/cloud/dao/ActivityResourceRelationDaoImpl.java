package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.ActivityResourceRelation;

@Repository
public class ActivityResourceRelationDaoImpl extends AbstractBaseDaoImpl<ActivityResourceRelation, String> implements ActivityResourceRelationDao {

	@Override
	public List<ActivityResourceRelation> getResoures(String activityId) {
		String jpql = "from ActivityResourceRelation arr where arr.activityId = ?1";
		return this.executeQueryWithoutPaging(jpql, activityId);
	}

	@Override
	public List<ActivityResourceRelation> getResoures(List<String> activityIds) {
		if(activityIds == null || activityIds.isEmpty()){
			return null;
		}
		String jpql = "from ActivityResourceRelation arr where arr.activityId in ?1";
		return this.executeQueryWithoutPaging(jpql, activityIds);
	}
}
