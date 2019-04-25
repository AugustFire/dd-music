package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.ExamClassRoom;

@Repository
public class ExamClassRoomDaoImpl extends AbstractBaseDaoImpl<ExamClassRoom, String> implements ExamClassRoomDao {

	@Override
	public List<ExamClassRoom> getByExamId(String eid) {
		String jpql = "from ExamClassRoom exr where exr.examId = ?1";
		return this.executeQueryWithoutPaging(jpql,eid);
	}


}
