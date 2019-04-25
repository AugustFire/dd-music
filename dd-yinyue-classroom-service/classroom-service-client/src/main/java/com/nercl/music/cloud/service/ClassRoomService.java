package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.ClassRoom;
import com.nercl.music.cloud.entity.classroom.VersionDesc;

public interface ClassRoomService {

	ClassRoom get(String rid);

	ClassRoom joined(String uid, String roomCode);

	List<ClassRoom> getJoinedRooms(String uid, String gid);

	List<ClassRoom> getFoundedRooms(String uid, String gid);

	ClassRoom found(String uid, String gradeId, VersionDesc version, String classId, boolean isFirstVolume);

	/**
	 * 更新课堂
	 * 
	 * @param id
	 *            课堂id
	 * @param title
	 *            课堂标题
	 * @param intro
	 *            课堂介绍
	 */
	void update(String id, String title, String intro);

	/**
	 * 删除课堂
	 * 
	 * @param id
	 *            课堂id
	 */
	void deleteClassroom(String id);

	List<ClassRoom> query(String teacherName, String title);

	ClassRoom findById(String classRoomId);

	/**
	 * 新增课堂
	 */
	ClassRoom newClassroom(ClassRoom classroom);

	/**
	 * 根据课堂编号查询课堂
	 */
	ClassRoom getByCode(String roomCode);

}
