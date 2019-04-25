package com.nercl.music.cloud.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.ClassRoomDao;
import com.nercl.music.cloud.dao.GradeDao;
import com.nercl.music.cloud.entity.base.Grade;
import com.nercl.music.cloud.entity.classroom.ClassRoom;

@Service
@Transactional
public class GradeServiceImpl implements GradeService {

	@Autowired
	private GradeDao gradeDao;

	@Autowired
	private ClassRoomDao classRoomDao;

	@Override
	public List<Grade> getAllGrades() {
		return gradeDao.findAll(Grade.class);
	}

	@Override
	public List<Grade> findByConditions(Grade condition) throws Exception {
		List<Grade> grades = gradeDao.findByConditions(condition);
		if (null != grades) {
			return grades.stream().sorted((g1, g2) -> g1.getCode().compareTo(g2.getCode()))
					.collect(Collectors.toList());
		}
		return null;
	}

	@Override
	public Grade findById(String gradeId) {
		return gradeDao.findByID(gradeId);
	}

	@Override
	public Grade findByCode(String gradeCode) {
		return gradeDao.findByCode(gradeCode);
	}

	@Override
	public void update(Grade grade) {
		gradeDao.update(grade);
	}

	@Override
	public void deleteById(String gradeId) throws Exception {
		ClassRoom t = new ClassRoom();
		t.setGradeId(gradeId);
		List<ClassRoom> listClassRoom = classRoomDao.findByConditions(t);
		// 当年级被课堂引用后，年级不能再删除
		if (listClassRoom.isEmpty()) {
			gradeDao.deleteById(gradeId);
		} else {
			throw new RuntimeException("年级已被课堂引用，不能删除此年级!");
		}
	}

	@Override
	public void delete(Grade grade) {
		gradeDao.delete(grade);
	}

	@Override
	public void save(Grade grade) {
		gradeDao.save(grade);
	}

}
