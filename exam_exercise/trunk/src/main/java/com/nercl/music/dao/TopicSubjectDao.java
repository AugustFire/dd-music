package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.authorize.TopicSubject;

public interface TopicSubjectDao extends BaseDao<TopicSubject, String> {

	TopicSubject getByTopicSubject(String tid, String sid);

	List<TopicSubject> getBySubject(String sid);

	List<TopicSubject> getByTopic(String tid);

}
