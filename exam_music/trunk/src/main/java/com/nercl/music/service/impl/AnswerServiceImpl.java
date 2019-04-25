package com.nercl.music.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.AnswerDao;
import com.nercl.music.entity.Answer;
import com.nercl.music.service.AnswerService;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

	@Autowired
	private AnswerDao answerDao;

	@Override
	public boolean saveAnswer(Answer answer) {
		this.answerDao.save(answer);
		return true;
	}

	@Override
	public Answer getByQuestion(String questionId) {
		return this.answerDao.getByQuestion(questionId);
	}

}
