package com.nercl.music.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ExamPointDao;
import com.nercl.music.entity.ExamPoint;
import com.nercl.music.service.ExamPointService;

@Service
@Transactional
public class ExamPointServiceImpl implements ExamPointService {

	@Autowired
	private ExamPointDao examPointDao;

	@Override
	public List<ExamPoint> list(int page) {
		return this.examPointDao.list(page);
	}

	@Override
	public List<ExamPoint> list() {
		return this.examPointDao.list();
	}

	@Override
	public ExamPoint get(String examPointId) {
		return examPointDao.get(examPointId);
	}

	@Override
	public boolean save(String name, String address) {
		if (examPointDao.getByName(name) == null) {
			ExamPoint examPoint = new ExamPoint();
			examPoint.setAddress(address);
			examPoint.setName(name);
			examPointDao.save(examPoint);
			return true;
		}
		return false;
	}

	@Override
	public void update(String id, String name, String address) {
		ExamPoint examPoint = examPointDao.get(id);
		examPoint.setAddress(address);
		examPoint.setName(name);
		examPointDao.update(examPoint);

	}

	@Override
	public ExamPoint getByName(String name) {
		return examPointDao.getByName(name);
	}

	@Override
	public void delete(String id) {
		ExamPoint examPoint = examPointDao.get(id);
		examPointDao.delete(examPoint);
	}

	@Override
	public List<ExamPoint> listByAttributes(int page, String name, String address) {
		return examPointDao.listByAttributes(page, name, address);
	}
}
