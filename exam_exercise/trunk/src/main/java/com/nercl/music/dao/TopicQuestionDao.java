package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.authorize.TopicQuestion;

public interface TopicQuestionDao extends BaseDao<TopicQuestion, String> {

	TopicQuestion get(String topicId, String examQuestionId);

	void removeQuestions(String tid);

	List<TopicQuestion> get(String topicId);

	List<TopicQuestion> list(String examQuestionId);
}
