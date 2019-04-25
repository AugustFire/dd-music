package com.nercl.music.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ExamRoomDao;
import com.nercl.music.entity.ExamRoom;
import com.nercl.music.service.ExamRoomService;

@Service
@Transactional
public class ExamRoomServiceImpl implements ExamRoomService {

	@Autowired
	private ExamRoomDao examRoomDao;

	@Override
	public List<ExamRoom> getByExamPoint(String examPointId) {
		return examRoomDao.getByExamPoint(examPointId);
	}

	@Override
	public void addRoom(String id, String title) {
		ExamRoom examRoom = new ExamRoom();
		examRoom.setExamPointId(id);
		examRoom.setTitle(title);
		examRoomDao.save(examRoom);
	}

	@Override
	public String update(String id, String title) {
		ExamRoom examRoom = examRoomDao.getRoom(id);
		if (null != examRoom) {
			examRoom.setTitle(title);
			examRoomDao.update(examRoom);
			return examRoom.getExamPointId();
		}
		return null;
	}

	@Override
	public ExamRoom getRoom(String id) {
		return examRoomDao.getRoom(id);
	}

}
