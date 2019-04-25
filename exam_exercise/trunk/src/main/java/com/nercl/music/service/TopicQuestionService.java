package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.authorize.TopicQuestion;

public interface TopicQuestionService {

	void save(String topicId, String[] examQuestionIds);

	TopicQuestion get(String topicId, String examQuestionId);

	List<TopicQuestion> get(String topicId);

	void removeQuestions(String tid);

	void delete(String id);

}
