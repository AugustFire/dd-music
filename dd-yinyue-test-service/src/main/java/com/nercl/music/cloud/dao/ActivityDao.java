package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.Activity;
import com.nercl.music.cloud.entity.ActivityType;
import com.nercl.music.cloud.entity.CheckStatus;
import com.nercl.music.cloud.entity.UserRole;
import com.nercl.music.util.page.PaginateSupportArray;

public interface ActivityDao extends BaseDao<Activity, String> {

	PaginateSupportArray<Activity> findByUserId(String userId, int page, int pageSize);

	PaginateSupportArray<Activity> findByStudentIds(List<String> studentIds, CheckStatus checkStatus,
			ActivityType activityType, int page, int pageSize);

	List<Activity> findByType(ActivityType activityType, String awardLevel,UserRole userRole);

}
