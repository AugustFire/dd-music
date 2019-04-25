package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.ExamRoom;

public interface ExamRoomDao extends BaseDao<ExamRoom, String> {

	List<ExamRoom> getByExamPoint(String examPointId);

	ExamRoom getRoom(String examRoomId);
}
