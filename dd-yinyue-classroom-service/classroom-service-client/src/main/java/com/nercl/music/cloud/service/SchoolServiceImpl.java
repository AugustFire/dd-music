package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.ClassesDao;
import com.nercl.music.cloud.dao.SchoolDao;
import com.nercl.music.cloud.entity.base.Classes;
import com.nercl.music.cloud.entity.base.School;

@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {

	@Autowired
	private SchoolDao schoolDao;

	@Autowired
	private ClassesDao classesDao;

	@Override
	public void save(School school) {
		schoolDao.save(school);
	}

	@Override
	public void deleteById(String id) throws Exception {
		Classes c = new Classes();
		c.setSchoolId(id);
		List<Classes> classList = classesDao.findByConditions(c);
		if (classList.isEmpty()) {
			schoolDao.deleteById(id);
		} else {
			throw new RuntimeException("学校已被年级或班级引用，不能删除此学校！");
		}
	}

	@Override
	public School findById(String sid) {
		return schoolDao.findByID(sid);
	}

	@Override
	public void update(School school) {
		schoolDao.update(school);
	}

	@Override
	public List<School> findByConditions(School school) throws Exception {
		return schoolDao.findByConditions(school);
	}

	@Override
	public School findByCode(String code) {
		return schoolDao.findByCode(code);
	}

}
