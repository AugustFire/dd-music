package com.nercl.music.cloud.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.group.Activity;
import com.nercl.music.cloud.entity.group.Group;
import com.nercl.music.cloud.service.ClassRoomUserRelationService;
import com.nercl.music.cloud.service.GroupService;
import com.nercl.music.constant.CList;

@RestController
public class GroupController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private GroupService groupService;

	@Autowired
	private ClassRoomUserRelationService classRoomUserRelationService;

	@PostMapping(value = "/group/activity", produces = JSON_PRODUCES)
	public Map<String, Object> save(String classRoomId, String title) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(classRoomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classRoomId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		Activity activity = groupService.addActivity(classRoomId, title);
		if (null != activity) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("activity_id", activity.getId());
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "add activity failed");
		}
		return ret;
	}

	@GetMapping(value = "/groups", produces = JSON_PRODUCES)
	public Map<String, Object> get(String classRoomId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(classRoomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classRoomId is null");
			return ret;
		}
		List<Group> groups = groupService.get(classRoomId);
		ret.put("code", CList.Api.Client.OK);
		if (null == groups) {
			return ret;
		}
		List<Map<String, Object>> list = Lists.newArrayList();
		groups.forEach(group -> {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", group.getId());
			map.put("title", group.getTitle());
			list.add(map);
		});
		ret.put("groups", list);
		return ret;
	}

	@GetMapping(value = "/group/{gid}/students", produces = JSON_PRODUCES)
	public Map<String, Object> getStudents(@PathVariable String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		List<Map<String, Object>> students = groupService.getStudents(gid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("students", students);
		return ret;
	}

	@PostMapping(value = "/group", produces = JSON_PRODUCES)
	public Map<String, Object> save(String classRoomId, String activityId, String title, String[] students) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(classRoomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classRoomId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(activityId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "activityId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		Group group = groupService.add(title, classRoomId, activityId, students);
		if (null != group) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("group_id", group.getId());
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "add failed");
		}
		return ret;
	}

	@PutMapping(value = "/group/{gid}", produces = JSON_PRODUCES)
	public Map<String, Object> update(@PathVariable String gid, String title) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		boolean success = groupService.update(gid, title);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "update failed");
		}
		return ret;
	}

	@DeleteMapping(value = "/group/{gid}", produces = JSON_PRODUCES)
	public Map<String, Object> delete(@PathVariable String gid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(gid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "gid is null");
			return ret;
		}
		boolean success = groupService.delete(gid);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "delete failed");
		}
		return ret;
	}

	@PutMapping(value = "/group/exchange_group", produces = JSON_PRODUCES)
	public Map<String, Object> exchange(String[] students, String classRoomId, String sourceGroupId,
			String destGroupId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == students || students.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "students is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(classRoomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classRoomId is null");
			return ret;
		}
		boolean success = groupService.exchange(students, classRoomId, sourceGroupId, destGroupId);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "exchange group failed");
		}
		return ret;
	}

	@GetMapping(value = "/ungrouped", produces = JSON_PRODUCES)
	public Map<String, Object> getUngroupedUsers(String classRoomId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(classRoomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classRoomId is null");
			return ret;
		}
		List<Map<String, Object>> users = classRoomUserRelationService.getUnGroupedUsers(classRoomId);
		ret.put("code", CList.Api.Client.OK);
		ret.put("users", users);
		return ret;
	}


	@PostMapping(value = "/group/set_leader", produces = JSON_PRODUCES)
	public Map<String, Object> setLeader(String groupId, String studentId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(groupId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "groupId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(studentId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "studentId is null");
			return ret;
		}
		boolean success = groupService.setLeader(groupId, studentId);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "set leader failed");
		}
		return ret;
	}
}
