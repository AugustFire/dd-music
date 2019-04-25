package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.ExamClassRoom;

public interface ExamClassRoomDao extends BaseDao<ExamClassRoom, String> {

	List<ExamClassRoom> getByExamId(String eid);


}
