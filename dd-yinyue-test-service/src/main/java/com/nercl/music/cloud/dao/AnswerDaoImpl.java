package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.Answer;

@Repository
public class AnswerDaoImpl extends AbstractBaseDaoImpl<Answer, String> implements AnswerDao {

	@Override
	public List<Answer> list() {
		String jpql = "from Answer ans";
		List<Answer> answers = this.executeQueryWithoutPaging(jpql);
		return answers;
	}

	@Override
	public Answer getByQuestion(String questionId) {
		String jpql = "from Answer ans where ans.questionId = ?1";
		List<Answer> answers = this.executeQueryWithoutPaging(jpql, questionId);
		return null == answers || answers.isEmpty() ? null : answers.get(0);
	}

}
