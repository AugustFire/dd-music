package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.TopicQuestionDao;
import com.nercl.music.entity.authorize.TopicQuestion;

@Repository
public class TopicQuestionDaoImpl extends AbstractBaseDaoImpl<TopicQuestion, String> implements TopicQuestionDao {

	@Override
	public TopicQuestion get(String topicId, String examQuestionId) {
		String jpql = "from TopicQuestion tq where tq.topicId = ?1 and tq.examQuestionId = ?2";
		List<TopicQuestion> topicQuestions = executeQueryWithoutPaging(jpql, topicId, examQuestionId);
		return null == topicQuestions || topicQuestions.isEmpty() ? null : topicQuestions.get(0);
	}

	@Override
	public void removeQuestions(String tid) {
		entityManager.createQuery("delete from TopicQuestion where topicId = " + tid).executeUpdate();
	}

	@Override
	public List<TopicQuestion> get(String topicId) {
		String jpql = "from TopicQuestion tq where tq.topicId = ?1";
		List<TopicQuestion> topicQuestions = executeQueryWithoutPaging(jpql, topicId);
		return topicQuestions;
	}

	@Override
	public List<TopicQuestion> list(String examQuestionId) {
		String jpql = "from TopicQuestion tq where tq.examQuestionId = ?1";
		return executeQueryWithoutPaging(jpql, examQuestionId);
	}

}
