package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.ActivityMember;

public interface ActivityMemberService {

	int save(String activityId, List<ActivityMember> activityMemberList);

	String save(ActivityMember activityMember);

	List<ActivityMember> findByActivityId(String id);
}
