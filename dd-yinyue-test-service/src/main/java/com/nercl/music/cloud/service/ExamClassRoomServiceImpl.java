package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.ExamClassRoomDao;
import com.nercl.music.cloud.entity.ExamClassRoom;

@Service
@Transactional
public class ExamClassRoomServiceImpl implements ExamClassRoomService {

	@Autowired
	private ExamClassRoomDao examClassRoomDao; 
	
	@Override
	public String save(ExamClassRoom ecr) {
		examClassRoomDao.save(ecr);
		return ecr.getId();
	}

	@Override
	public List<ExamClassRoom> getByExamId(String eid) {
		return examClassRoomDao.getByExamId(eid);
	}


}
