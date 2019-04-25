package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.group.Activity;
import com.nercl.music.cloud.entity.group.Group;
import com.nercl.music.cloud.entity.group.GroupUser;

public interface GroupService {

	Activity addActivity(String classRoomId, String title);

	List<Group> get(String classRoomId);

	List<Map<String, Object>> getStudents(String groupId);

	GroupUser get(String userId, String groupId);

	Group add(String title, String classRoomId, String activityId, String[] students);

	boolean exchange(String[] students, String classRoomId, String sourceGroupId, String destGroupId);

	boolean delete(String groupId);

	boolean update(String groupId, String title);

	Group findById(String gid);

	GroupUser getLeader(String groupId);

	boolean setLeader(String groupId, String studentId);
}
