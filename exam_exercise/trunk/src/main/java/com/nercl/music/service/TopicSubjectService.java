package com.nercl.music.service;

import java.util.List;

import com.nercl.music.entity.authorize.TopicSubject;

public interface TopicSubjectService {

	boolean save(String tid, String ttitle, String sid, String stitle);

	boolean deleteByTopic(String tid);

	List<TopicSubject> getBySubject(String sid);

	List<TopicSubject> getByTopic(String tid);

	TopicSubject getByTopicSubject(String tid, String sid);

}
