package com.nercl.music.cloud.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.cloud.dao.AnswerDao;
import com.nercl.music.cloud.entity.Answer;


@Service
@Transactional
public class AnswerServiceImpl implements AnswerService{

	@Autowired
	private AnswerDao answerDao;
 	
	@Override
	public void save(Answer answer) {
		answerDao.save(answer);
	}

	@Override
	public Answer getByQuestion(String qid) {
		return answerDao.getByQuestion(qid);
	}


}
