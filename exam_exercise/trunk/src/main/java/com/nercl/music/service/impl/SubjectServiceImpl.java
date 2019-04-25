package com.nercl.music.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.SubjectDao;
import com.nercl.music.entity.authorize.Subject;
import com.nercl.music.service.SubjectService;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SubjectDao subjectDao;

	@Override
	public Subject save(String title) {
		List<Subject> subjects = this.getByTitle(title);
		if (null == subjects || subjects.isEmpty()) {
			Subject subject = new Subject();
			subject.setTitle(title);
			subjectDao.save(subject);
			return subject;
		}
		return null;
	}

	@Override
	public List<Subject> list() {
		return subjectDao.list();
	}

	@Override
	public List<Subject> getByTitle(String title) {
		return subjectDao.getByTitle(title);
	}

	@Override
	public Subject get(String sid) {
		return subjectDao.findByID(sid);
	}

	@Override
	public boolean delete(String id) {
		subjectDao.deleteById(id);
		return true;
	}

}
