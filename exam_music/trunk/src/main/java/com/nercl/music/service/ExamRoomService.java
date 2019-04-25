package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.ExamRoom;

public interface ExamRoomService {

	List<ExamRoom> getByExamPoint(String examPointId);

	void addRoom(String id, String title);

	String update(String id, String title);

	ExamRoom getRoom(String id);

}
