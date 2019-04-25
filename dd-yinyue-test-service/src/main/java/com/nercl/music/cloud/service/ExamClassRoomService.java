package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.ExamClassRoom;

public interface ExamClassRoomService {

	String save(ExamClassRoom ecr);

	List<ExamClassRoom> getByExamId(String eid);

}
