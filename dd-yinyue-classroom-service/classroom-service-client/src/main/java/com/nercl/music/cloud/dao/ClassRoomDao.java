package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.ClassRoom;

public interface ClassRoomDao extends BaseDao<ClassRoom, String> {

	ClassRoom getByCode(String roomCode);

	List<ClassRoom> getFoundedRooms(String uid);

	List<ClassRoom> getJoinedRooms(String uid);

	List<ClassRoom> query(String teacherName, String title);

}
