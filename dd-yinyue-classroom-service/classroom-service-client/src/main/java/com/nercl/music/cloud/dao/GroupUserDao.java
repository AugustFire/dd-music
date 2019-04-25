package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.group.GroupUser;

public interface GroupUserDao extends BaseDao<GroupUser, String> {

	List<GroupUser> getStudents(String groupId);

	GroupUser get(String userId, String groupId);

	GroupUser getLeader(String groupId);

}
