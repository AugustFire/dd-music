package com.nercl.music.cloud.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.nercl.music.cloud.dao.ActivityDao;
import com.nercl.music.cloud.dao.GroupDao;
import com.nercl.music.cloud.dao.GroupUserDao;
import com.nercl.music.cloud.entity.group.Activity;
import com.nercl.music.cloud.entity.group.Group;
import com.nercl.music.cloud.entity.group.GroupUser;
import com.nercl.music.constant.ApiClient;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

	@Autowired
	private ActivityDao activityDao;

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private GroupUserDao groupUserDao;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Activity addActivity(String classRoomId, String title) {
		Activity activity = new Activity();
		activity.setClassRoomId(classRoomId);
		activity.setTitle(title);
		activity.setCreateAt(System.currentTimeMillis());
		activityDao.save(activity);
		return Strings.isNullOrEmpty(activity.getId()) ? null : activity;
	}

	@Override
	public List<Group> get(String classRoomId) {
		return groupDao.get(classRoomId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getStudents(String groupId) {
		List<GroupUser> groupUsers = groupUserDao.getStudents(groupId);
		if (null == groupUsers || groupUsers.isEmpty()) {
			return null;
		}
		List<String> ids = groupUsers.stream().map(groupUser -> {
			return groupUser.getUserId();
		}).collect(Collectors.toList());
		Map<String, Object> users = restTemplate.getForObject(ApiClient.GET_USERS, Map.class, Joiner.on(",").join(ids));
		if (null == users) {
			return null;
		}
		if ((int) users.get("code") == 200) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) users.get("users");
			return list;
		} else {
			return null;
		}
	}

	@Override
	public GroupUser get(String userId, String groupId) {
		GroupUser groupUser = groupUserDao.get(userId, groupId);
		return groupUser;
	}

	@Override
	public Group add(String title, String classRoomId, String activityId, String[] students) {
		Group group = new Group();
		group.setTitle(title);
		group.setClassRoomId(classRoomId);
		group.setActivityId(activityId);
		groupDao.save(group);
		if (null != students && students.length > 0) {
			Arrays.stream(students).forEach(student -> {
				GroupUser groupUser = new GroupUser();
				groupUser.setUserId(student);
				groupUser.setGroupId(group.getId());
				groupUser.setClassRoomId(classRoomId);
				groupUserDao.save(groupUser);
			});
		}
		return Strings.isNullOrEmpty(group.getId()) ? null : group;
	}

	@Override
	public boolean exchange(String[] students, String classRoomId, String sourceGroupId, String destGroupId) {
		Arrays.stream(students).forEach(student -> {
			if (!Strings.isNullOrEmpty(sourceGroupId)) {
				GroupUser sourceGroupUser = groupUserDao.get(student, sourceGroupId);
				if (null != sourceGroupUser) {
					groupUserDao.deleteById(sourceGroupUser.getId());
				}
			}
			GroupUser destGroupUser = groupUserDao.get(student, destGroupId);
			if (null == destGroupUser && !Strings.isNullOrEmpty(destGroupId)) {
				GroupUser groupUser = new GroupUser();
				groupUser.setUserId(student);
				groupUser.setGroupId(destGroupId);
				groupUser.setClassRoomId(classRoomId);
				groupUserDao.save(groupUser);
			}
		});
		return true;
	}

	@Override
	public boolean delete(String groupId) {
		List<GroupUser> students = groupUserDao.getStudents(groupId);
		students.forEach(s -> {
			groupUserDao.delete(s);
		});
		groupDao.deleteById(groupId);
		return null == groupDao.findByID(groupId);
	}

	@Override
	public boolean update(String groupId, String title) {
		Group group = groupDao.findByID(groupId);
		if (null != group) {
			group.setTitle(title);
			return true;
		}
		return false;
	}

	@Override
	public Group findById(String gid) {
		return groupDao.findByID(gid);
	}

	@Override
	public boolean setLeader(String groupId, String studentId) {
		GroupUser groupUser = groupUserDao.get(studentId, groupId);
		if (null == groupUser) {
			return false;
		}
		GroupUser leader = getLeader(groupId);
		if (null != leader) {
			leader.setIsLeader(false);
			groupUserDao.update(leader);
		}
		groupUser.setIsLeader(true);
		groupUserDao.update(groupUser);
		return true;
	}

	@Override
	public GroupUser getLeader(String groupId) {
		return groupUserDao.getLeader(groupId);
	}

}
