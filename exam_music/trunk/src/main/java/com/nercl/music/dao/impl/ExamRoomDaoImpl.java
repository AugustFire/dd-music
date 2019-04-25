package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.ExamRoomDao;
import com.nercl.music.entity.ExamRoom;

@Repository
public class ExamRoomDaoImpl extends AbstractBaseDaoImpl<ExamRoom, String> implements ExamRoomDao {

	@Override
	public List<ExamRoom> getByExamPoint(String examPointId) {
		String jpql = "from ExamRoom er where er.examPointId = ?1";
		return this.executeQueryWithoutPaging(jpql, examPointId);
	}

	@Override
	public ExamRoom getRoom(String examRoomId) {
		String jpql = "from ExamRoom er where er.id = ?1";
		List<ExamRoom> examRooms = this.executeQueryWithoutPaging(jpql, examRoomId);
		return null == examRooms || examRooms.isEmpty() ? null : examRooms.get(0);
	}

}
