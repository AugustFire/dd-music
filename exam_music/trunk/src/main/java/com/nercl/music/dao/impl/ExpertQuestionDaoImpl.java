package com.nercl.music.dao.impl;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.ExpertQuestionDao;
import com.nercl.music.entity.ExpertQuestion;

@Repository
public class ExpertQuestionDaoImpl extends AbstractBaseDaoImpl<ExpertQuestion, String> implements ExpertQuestionDao {

	@Override
	public void deleteByQuestion(String questionId) {
		String jpql = "delete from ExpertQuestion eq where eq.examQuestionId = " + questionId;
		this.entityManager.createQuery(jpql).executeUpdate();
	}

}
