package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.nercl.music.cloud.dao.ClassRoomDao;
import com.nercl.music.cloud.dao.ClassesDao;
import com.nercl.music.cloud.entity.base.Classes;
import com.nercl.music.cloud.entity.base.Grade;
import com.nercl.music.cloud.entity.classroom.ClassRoom;

@Service
@Transactional
public class ClassesServiceImpl implements ClassesService {

	@Autowired
	private ClassesDao classesDao;

	@Autowired
	private ClassRoomDao classRoomDao;

	@Override
	public void save(Classes classes) {
		classesDao.save(classes);
	}

	@Override
	public void deleteById(String id) throws Exception {
		ClassRoom t = new ClassRoom();
		t.setClassesId(id);
		List<ClassRoom> listClassRoom = classRoomDao.findByConditions(t);
		// 当班级被课堂引用后，班级不能再删除
		if (listClassRoom.isEmpty()) {
			classesDao.deleteById(id);
		} else {
			throw new RuntimeException("班级已被课堂引用，不能删除此班级!");
		}
	}

	@Override
	public Classes findById(String cid) {
		return classesDao.findByID(cid);
	}

	@Override
	public void update(Classes classes) {
		classesDao.update(classes);
	}

	@Override
	public List<Classes> findByConditions(Classes classes) throws Exception {
		return classesDao.findByConditions(classes);
	}

	@Override
	public List<Classes> findByConditions(Classes classes, int page) throws Exception {
		return classesDao.findByConditionsWithPaging(classes, page);
	}

	@Override
	public Set<Grade> getGradesBySchool(String sid) {
		List<Classes> classes = classesDao.getBySchool(sid);
		if (null == classes) {
			return null;
		}
		Set<Grade> grades = Sets.newHashSet();
		classes.forEach(c -> {
			grades.add(c.getGrade());
		});
		return grades;
	}

	@Override
	public List<Classes> getClassBySchoolGrade(String sid, String gid) {
		return classesDao.getClassBySchoolGrade(sid, gid);
	}

	@Override
	public List<Classes> getClassByTeacher(String tid) {
		return classesDao.getClassByTeacher(tid);
	}

	@Override
	public List<Classes> getClassByTeacherGrade(String tid, String gid) {
		return classesDao.getClassByTeacherGrade(tid, gid);
	}

	@Override
	public List<Classes> getClassBySchool(String sid) {
		return classesDao.getClassBySchool(sid);
	}


}
