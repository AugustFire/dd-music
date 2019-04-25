package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.ClassRoomUserRelation;

public interface ClassRoomUserRelationDao extends BaseDao<ClassRoomUserRelation, String> {

	ClassRoomUserRelation get(String roomId, String uid);

	List<ClassRoomUserRelation> get(String rid);

	/**
	 * 获取未分组成员
	 * @param rid 课堂id
	 * */
	List<ClassRoomUserRelation> getUnGroupedUsers(String rid);
}
