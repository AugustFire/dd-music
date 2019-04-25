package com.nercl.music.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ExpertQuestionDao;
import com.nercl.music.entity.ExpertQuestion;
import com.nercl.music.service.ExpertQuestionService;

@Service
@Transactional
public class ExpertQuestionServiceImpl implements ExpertQuestionService {

	@Autowired
	private ExpertQuestionDao expertQuestionDao;

	@Override
	public void delete(String questionId) {
		this.expertQuestionDao.deleteByQuestion(questionId);
	}

	@Override
	public void save(ExpertQuestion eq) {
		this.expertQuestionDao.save(eq);
	}

}
