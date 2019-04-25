package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.Enrollment;

public interface EnrollmentService {

	Enrollment get(String exid, String eeid);

	List<Enrollment> list(String examId, int page);

	List<Enrollment> list(String examId, Enrollment.Status status, int page);

	boolean pass(String id);

	boolean unpass(String id, String reason);
	
//	boolean batchPass(String ids);
//	
//	boolean batchUnpass(String ids,String reason);

	boolean join(String eid, String exid);
	
	List<Enrollment> getByAttributes(String name,String examPoint,String examRoom,String examNo,String status,int page);

}
