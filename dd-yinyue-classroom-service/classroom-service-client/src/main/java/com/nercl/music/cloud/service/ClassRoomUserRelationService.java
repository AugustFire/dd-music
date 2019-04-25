package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.classroom.ClassRoomUserRelation;

public interface ClassRoomUserRelationService {

	boolean save(String id, String userId);

	ClassRoomUserRelation get(String roomId, String uid);

	void removeJoinedStudent(String rid, String[] uids);

	List<Map<String, Object>> getJoinedStudents(String rid);

	/**
	 * 获取未分组成员
	 * @param rid 课堂id
	 * */
	List<Map<String, Object>> getUnGroupedUsers(String rid);

	/**
	 * 根据课堂id，删除课堂中所有成员
	 * */
	int deleteClassroomUsers(String id);
}
