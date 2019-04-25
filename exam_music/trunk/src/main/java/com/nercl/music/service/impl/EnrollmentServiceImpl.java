package com.nercl.music.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.EnrollmentDao;
import com.nercl.music.entity.Enrollment;
import com.nercl.music.entity.Enrollment.Status;
import com.nercl.music.service.EnrollmentService;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

	@Autowired
	private EnrollmentDao enrollmentDao;

	@Override
	public List<Enrollment> list(String examId, int page) {
		return this.enrollmentDao.list(examId, page);
	}

	@Override
	public List<Enrollment> list(String examId, Enrollment.Status status, int page) {
		return this.enrollmentDao.listByStatus(examId, status, page);
	}

	@Override
	public Enrollment get(String exid, String eeid) {
		return this.enrollmentDao.get(exid, eeid);
	}

	@Override
	public boolean pass(String enrollmentId) {
		if(!enrollmentId.contains(",")){
			Enrollment enrollment = this.enrollmentDao.findByID(enrollmentId);
			if (null != enrollment) {
				enrollment.setStatus(Status.PASSED);
				enrollment.setPassAt(System.currentTimeMillis());
				this.enrollmentDao.update(enrollment);
				return true;
			}	
		}
		else{
			String[] toPass = enrollmentId.split(",");
			for (String id : toPass) {
				this.pass(id);
			}
			return true;
		}	
		return false;	
	}

	@Override
	public boolean unpass(String enId, String reason) {
		if(!enId.contains(",")){
			Enrollment enrollment = this.enrollmentDao.findByID(enId);
			if (null != enrollment) {
				enrollment.setStatus(Status.UN_PASSED);
				enrollment.setPassAt(System.currentTimeMillis());
				enrollment.setUnPassedReason(reason);
				this.enrollmentDao.update(enrollment);
				return true;
			}
		}	
		else{
			String[] toUnPass = enId.split(",");
			for (String id : toUnPass) {
				this.unpass(id, reason);
			}
			return true;
		}	
		return false;
	}

	@Override
	public boolean join(String examineeId, String examId) {
		Enrollment enrollment = this.get(examId, examineeId);
		if (null != enrollment) {
			return false;
		}
		enrollment = new Enrollment();
		enrollment.setExamineeId(examineeId);
		enrollment.setExamId(examId);
		enrollment.setCreatAt(System.currentTimeMillis());
		enrollment.setStatus(Status.FOR_CHECKED);
		this.enrollmentDao.save(enrollment);
		return true;
	}

	@Override
	public List<Enrollment> getByAttributes(String name, String examPoint, String examRoom, String examNo,String status, int page) {
		return enrollmentDao.getByAttributes(name, examPoint, examRoom, examNo,status, page);
	}

//	@Override
//	public boolean batchPass(String ids) {
//		String[] toPass = ids.split(",");
//		for (int i = 0; i < toPass.length; i++) {
//			this.pass(toPass[i]);
//		}
//		return true;
//	}
//
//	@Override
//	public boolean batchUnpass(String ids,String reason) {
//		String[] toUnPass = ids.split(",");
//		for (int i = 0; i < toUnPass.length; i++) {
//			this.unpass(toUnPass[i],reason);
//		}
//		return true;
//	}

}
