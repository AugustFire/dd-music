package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.ActivityMember;

@Repository
public class ActivityMemberDaoImpl extends AbstractBaseDaoImpl<ActivityMember, String> implements ActivityMemberDao {

	@Override
	public List<ActivityMember> getActivityMembers(String cid, String activityId) {
		String jpql = "from ActivityMember where activityId = ?1 and classId = ?2";
		return this.executeQueryWithoutPaging(jpql, activityId, cid);
	}

	@Override
	public List<ActivityMember> findByActivityId(String id) {
		String jpql = "from ActivityMember where activityId = ?1";
		return this.executeQueryWithoutPaging(jpql, id);
	}

}
