package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.ActivityMember;

public interface ActivityMemberDao extends BaseDao<ActivityMember, String> {

	List<ActivityMember> getActivityMembers(String cid, String activityId);

	List<ActivityMember> findByActivityId(String id);


}
