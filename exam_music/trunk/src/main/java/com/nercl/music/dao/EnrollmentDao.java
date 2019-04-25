package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.Enrollment;

public interface EnrollmentDao extends BaseDao<Enrollment, String> {

	List<Enrollment> list(String examId, int page);

	List<Enrollment> listByStatus(String examId, Enrollment.Status status, int page);

	Enrollment get(String exid, String eeid);
	
	List<Enrollment> getByAttributes(String name,String examPoint,String examRoom,String examNo,String status,int page);
}
