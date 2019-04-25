package com.nercl.music.service;

import com.nercl.music.entity.authorize.Topic;

import java.util.List;

public interface TopicService {

	List<Topic> list();

	List<Topic> list(Integer subjectType);

	Topic findByID(String id);

	Topic getNewest(Integer year);

	List<Topic> list(int page);

	List<Topic> getByYear(Integer year);

	List<Topic> getByTitle(Integer subjectType, String title, int page);

	List<Topic> query(String key);

	boolean save(Integer subjectType, String title, int year, String area, Integer fee, String[] sids, Long startAt,
	        Long endAt);

	boolean update(String id, Integer subjectType, String title, Integer year, String area, String[] sids, Long startAt,
	        Long endAt);

	boolean setQuestions(String tid, String[] qids);

	void removeQuestions(String tid);

	List<String> getQuestions(String tid);

	List<Topic> getPayed(String exerciserId);

	Topic get(String topicId);
	
    /**
     * 根据topicId删除该topic
     * 
     * @param id 要删除的topic的id
     */
    void deleteTopicById(String id);
}
